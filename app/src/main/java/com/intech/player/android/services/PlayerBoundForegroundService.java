package com.intech.player.android.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.SurfaceView;

import com.intech.player.App;
import com.intech.player.BuildConfig;
import com.intech.player.clean.boundaries.model.EventRequestModel;
import com.intech.player.controller.ITunesPlayerController;
import com.intech.player.di.DaggerPlayerComponent;
import com.intech.player.mvp.models.TrackViewModel;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.intech.player.android.fragments.PlayerFragment.EXTRA_TRACK;
import static com.intech.player.android.services.PlayerBoundForegroundService.PlayerState.Paused;
import static com.intech.player.android.services.PlayerBoundForegroundService.PlayerState.Playing;
import static com.intech.player.mvp.models.utils.ModelConverter.asTrackRequestModel;
import static com.intech.player.mvp.models.utils.ModelConverter.asTrackViewModel;

/**
 * A bound service that moves to foreground when player is on and returns to background on pause.
 * Assumed to be released with player on unbind during pause or by swiping out notification.
 * Observes player events to update notifications.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class PlayerBoundForegroundService extends android.app.Service {

    private static final String TAG = PlayerBoundForegroundService.class.getSimpleName();

    public static final String CHANNEL_ID = "IPlayer";
    public static final String CHANNEL_NAME = "IPlayer notifications when in background.";

    public static final int FOREGROUND_ID = 1;
    public static final int NOTIFICATION_ID = 1;

    public static final int MAX_PROGRESS = 100;

    public enum PlayerState {Playing, Paused}

    public interface SurfaceConsumer {
        void setSurfaceView(SurfaceView view);
    }

    private PlayerState mState = Paused;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;

    private int mProgress;
    private TrackViewModel mTrack;

    private Disposable mEventsDisposable;
    private Disposable mStopDisposable;

    private class LocalBinder extends Binder implements SurfaceConsumer {
        @Override
        public void setSurfaceView(SurfaceView view) {
            PlayerBoundForegroundService.this.playerController.setVideoSurface(view);
        }
    }

    @Inject
    ITunesPlayerController playerController;

    public PlayerBoundForegroundService() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        final TrackViewModel track = intent.getParcelableExtra(EXTRA_TRACK);

        if (isNew(track)) {
            stopForeground(true);
            DaggerPlayerComponent
                    .builder()
                    .context(App.getAppComponent().getContext())
                    .track(asTrackRequestModel(track))
                    .build()
                    .inject(playerController);

            mEventsDisposable = subscribeOnPlayerEvents();
        }

        return new LocalBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        playerController.clearVideoSurface();
        if (mState != Playing) {
            mEventsDisposable.dispose();
            mStopDisposable = playerController
                    .stop()
                    .subscribeOn(Schedulers.single())
                    .observeOn(Schedulers.single())
                    .subscribe(
                            () -> mStopDisposable.dispose(),
                            this::handleError);
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void handleEvent(EventRequestModel event) {
        switch (event.getType()) {
            case Start:
                startForeground(event);
                mState = Playing;
                break;
            case Pause:
                stopForeground(true);
                mState = Paused;
                break;
            case Progress:
                onTrackProgress(event);
                break;
        }
    }

    private Disposable subscribeOnPlayerEvents() {
        return mEventsDisposable = playerController
                .getPlayerEvents()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe(
                        this::handleEvent,
                        this::handleError
                );
    }

    private void handleError(Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, e.getMessage());
        }
    }

    private boolean isNew(TrackViewModel track) {
        return mTrack == null || !track.getPreviewUrl().equals(mTrack.getPreviewUrl());
    }


    private void onTrackProgress(EventRequestModel event) {
        mProgress = (int) (event.getProgress() * MAX_PROGRESS);
        getNotificationManager().notify(NOTIFICATION_ID, buildOnProgressNotification());
    }

    private void startForeground(EventRequestModel event) {
        mTrack = asTrackViewModel(event.getTrack());
        startForeground(FOREGROUND_ID, buildPlayNotification());
    }

    private Notification buildPlayNotification() {
        return getNotificationBuilder()
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build();
    }

    private Notification buildOnProgressNotification() {
        return getNotificationBuilder()
                .setProgress(MAX_PROGRESS, mProgress, true)
                .build();
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        if (mNotificationBuilder == null) {
            mNotificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
            mNotificationBuilder
                    .setContentTitle(mTrack.getArtist())
                    .setContentText(mTrack.getTrackName())
                    .setProgress(MAX_PROGRESS, mProgress, false);
                    //.setDeleteIntent()
                    //.setContentIntent();
        }
        return mNotificationBuilder;
    }

    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            getNotificationManager()
                    .createNotificationChannel(notificationChannel);
        }
    }
}