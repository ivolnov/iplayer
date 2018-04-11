package com.intech.player.android.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
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
import com.intech.player.mvp.presenters.utils.UserMessageCompiler;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.intech.player.android.fragments.PlayerFragment.EXTRA_TRACK;
import static com.intech.player.android.services.PlayerBoundForegroundService.PlayerState.Paused;
import static com.intech.player.android.services.PlayerBoundForegroundService.PlayerState.Playing;
import static com.intech.player.android.utils.AndroidUtils.oreo;
import static com.intech.player.mvp.models.utils.ModelConverter.asTrackRequestModel;

/**
 * A started by {@link com.intech.player.android.activities.PlayerActivity} service.
 * Stops itself during unBind when player is not playing otherwise keeps running in foreground.
 * During rebind gets necessary data through {@link DependenciesConsumer} interface implemented by
 * {@link LocalBinder} and configures controller, notifications and etc...
 *
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class PlayerBoundForegroundService extends android.app.Service {

    private static final String TAG = PlayerBoundForegroundService.class.getSimpleName();

    public static final String CHANNEL_ID = "IPlayer";
    public static final String CHANNEL_NAME = "IPlayer background notifications."; //TODO: i11n

    public static final int FOREGROUND_ID = 1;
    public static final int NOTIFICATION_ID = 1;

    public static final int MAX_PROGRESS = 100;

    public enum PlayerState {Playing, Paused}

    public interface DependenciesConsumer {
        void setSurfaceView(SurfaceView view);
        void setTrack(TrackViewModel track);
    }

    private PlayerState mState = Paused;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;

    private int mProgress;
    private TrackViewModel mTrack;

    private Disposable mEventsDisposable;
    private LocalBinder mBinder;

    private class LocalBinder extends Binder implements DependenciesConsumer {

        private TrackViewModel track;

        @Override
        public void setSurfaceView(SurfaceView view) {
            PlayerBoundForegroundService.this.playerController.setVideoSurface(view);
        }

        @Override
        public void setTrack(TrackViewModel track) {
            this.track = track;
        }
    }

    @Inject
    ITunesPlayerController playerController;

    public PlayerBoundForegroundService() {
        App.getAppComponent().inject(this);
        mBinder = new LocalBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
		createNotificationChannelForOreo();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        final TrackViewModel track = intent.getParcelableExtra(EXTRA_TRACK);
        handleTrack(track);
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        handleTrack(mBinder.track);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        playerController.clearVideoSurface();
        if (mState != Playing) {
            mEventsDisposable.dispose();
            stopPlayer();
			stopSelf();
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void handleTrack(TrackViewModel track) {

        if (isNew(track)) {
            mTrack = track;
            mState = Paused;

            stopPlayer();
            initPlayer(track);

            stopForeground(true);
            startForeground();

            mEventsDisposable = subscribeOnPlayerEvents();
        }
    }

    private void initPlayer(TrackViewModel track) {
        DaggerPlayerComponent
                .builder()
                .context(App.getAppComponent().getContext())
                .track(asTrackRequestModel(track))
                .build()
                .inject(playerController);
    }

    private void stopPlayer() {
        playerController
                .stop()
                .blockingAwait();
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

    private void handleEvent(EventRequestModel event) {
        switch (event.getType()) {
            case Start:
                startForeground();
                mState = Playing;
                break;
            case Pause:
                mState = Paused;
                break;
            case Progress:
                onTrackProgress(event);
                break;
        }
    }

    private void handleError(Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, UserMessageCompiler.from(e));
        }
    }

    private void onTrackProgress(EventRequestModel event) {
        mProgress = (int) (event.getProgress() * MAX_PROGRESS);
        getNotificationManager().notify(NOTIFICATION_ID, buildOnProgressNotification());
    }

    private void startForeground() {
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
/*            mNotificationBuilder
                    .setDeleteIntent()
                    .setContentIntent();*/
        }
        mNotificationBuilder
                .setContentTitle(mTrack.getArtist())
                .setContentText(mTrack.getTrackName())
                .setProgress(MAX_PROGRESS, mProgress, false);

        return mNotificationBuilder;
    }

    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    private void createNotificationChannelForOreo() {
        if (oreo()) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            getNotificationManager()
                    .createNotificationChannel(notificationChannel);
        }
    }

    private boolean isNew(TrackViewModel track) {
        return mTrack == null || track == null || !track.getPreviewUrl().equals(mTrack.getPreviewUrl());
    }
}