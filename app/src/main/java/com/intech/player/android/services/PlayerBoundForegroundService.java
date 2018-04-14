package com.intech.player.android.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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
import com.intech.player.R;
import com.intech.player.android.activities.PlayerActivity;
import com.intech.player.android.activities.TrackListActivity;
import com.intech.player.clean.boundaries.PlayerController;
import com.intech.player.clean.boundaries.model.EventRequestModel;
import com.intech.player.controllers.ITunesPlayerController;
import com.intech.player.di.DaggerPlayerComponent;
import com.intech.player.mvp.models.TrackViewModel;
import com.intech.player.mvp.presenters.utils.UserMessageCompiler;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

import static com.intech.player.App.INVALID_ID;
import static com.intech.player.android.fragments.PlayerFragment.EXTRA_TRACK;
import static com.intech.player.android.fragments.TrackListFragment.EXTRA_SELECTED_TRACK;
import static com.intech.player.android.utils.AndroidUtils.oreo;
import static com.intech.player.mvp.models.utils.ModelConverter.asTrackRequestModel;
import static com.intech.player.mvp.models.utils.ModelConverter.isVideo;

/**
 * A started by {@link com.intech.player.android.fragments.PlayerFragment} service.
 * Keeps running in foreground until explicitly stopped, hopefully.
 * The idea is that when we leave a player through the back button it'll be stopped
 * otherwise we believe that current track is still relevant.
 * During rebind gets necessary data via {@link UiConsumer} interface implemented by
 * {@link LocalBinder} to configure controller, notifications...
 *
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class PlayerBoundForegroundService extends Service {

    private static final String TAG = PlayerBoundForegroundService.class.getSimpleName();

    public static final String CHANNEL_ID = "IPlayer";

    public static final int FOREGROUND_ID = 1;
    public static final int NOTIFICATION_ID = 1;

    public static final int MAX_PROGRESS = 100;

    public interface UiComponent {
        TrackViewModel getTrack();
        SurfaceView getSurface();
        void startListening();
    }

    public interface UiConsumer {
        void plugIn(UiComponent ui);
    }

    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;

    private int mProgress;
    private TrackViewModel mTrack;

    private Disposable mEventsDisposable;
    private LocalBinder mBinder;
    private WeakReference<UiComponent> mUi;

    private class LocalBinder extends Binder implements UiConsumer {
        public void plugIn(UiComponent ui) {
            mUi = new WeakReference<>(ui);
        }
    }

    @Inject
    PlayerController playerController;

    public PlayerBoundForegroundService() {
        App.getAppComponent().inject(this);
        mBinder = new LocalBinder();
        mUi = new WeakReference<>(null);
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
        handleTrack(mUi.get().getTrack());
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        return true;
    }

    @Override
    public void onDestroy() {
        disposeEvents();
        stopPlayer();
        stopForeground(true);
        super.onDestroy();
    }

    private void handleTrack(TrackViewModel track) {
        if (isNew(track)) {
            mTrack = track;

            disposeEvents();

            stopForeground(true);
            startForeground();

            stopPlayer();
            initPlayer(track);

            mEventsDisposable = subscribeOnPlayerEvents();
        }
    }

    private void initPlayer(TrackViewModel track) {
        DaggerPlayerComponent
                .builder()
                .context(App.getAppComponent().getContext())
                .track(asTrackRequestModel(track))
                .build()
                .inject((ITunesPlayerController) playerController);
    }

    private void stopPlayer() {
        playerController
                .stop()
                .blockingAwait();
    }

    private Disposable subscribeOnPlayerEvents() {
        return mEventsDisposable = playerController
                .getPlayerEvents()
                .subscribe(
                        this::handleEvent,
                        this::handleError
                );
    }

    private void handleEvent(EventRequestModel event) {
        switch (event.getType()) {
            case Play:
                onPlayNotification();
                break;
            case Pause:
                onPauseNotification();
                break;
            case Progress:
                onProgressNotification(event);
                pingUi();
                break;
        }
    }

    private void handleError(Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, UserMessageCompiler.from(e));
        }
    }

    private void startForeground() {
        startForeground(FOREGROUND_ID, buildPauseNotification());
    }

    private void onPlayNotification() {
        getNotificationManager().notify(NOTIFICATION_ID, buildPlayNotification());
    }

    private void onPauseNotification() {
        getNotificationManager().notify(NOTIFICATION_ID, buildPauseNotification());
    }

    private void onProgressNotification(EventRequestModel event) {
        mProgress = (int) (event.getProgress() * MAX_PROGRESS);
        getNotificationManager().notify(NOTIFICATION_ID, buildOnProgressNotification());
    }

    private Notification buildPlayNotification() {
        return getNotificationBuilder()
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build();
    }

    private Notification buildPauseNotification() {
        return getNotificationBuilder()
                .setSmallIcon(android.R.drawable.ic_media_pause)
                .build();
    }

    private Notification buildOnProgressNotification() {
        return getNotificationBuilder()
                .setProgress(MAX_PROGRESS, mProgress, false)
                .build();
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        if (mNotificationBuilder == null) {
            mNotificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
					.setContentIntent(contentIntent());
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
                    getString(R.string.player_notifications_channel),
                    NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            getNotificationManager()
                    .createNotificationChannel(notificationChannel);
        }
    }

    private boolean isNew(TrackViewModel track) {
        return mTrack == null || track == null || !track.getPreviewUrl().equals(mTrack.getPreviewUrl());
    }

    private PendingIntent contentIntent() {
		final Intent main = new Intent(this, TrackListActivity.class);
    	final Intent player = new Intent(this, PlayerActivity.class)
				.putExtra(EXTRA_SELECTED_TRACK, mTrack);

		return PendingIntent
				.getActivities(
						this,
						INVALID_ID,
						new Intent[] {main, player},
						PendingIntent.FLAG_CANCEL_CURRENT);
	}

	private void disposeEvents() {
        if (mEventsDisposable != null) {
            mEventsDisposable.dispose();
        }
    }

    private void pingUi() {
        final UiComponent ui = mUi.get();
        if (ui != null) {
            if (isVideo(mTrack)) {
                setSurface(ui.getSurface());
            }
            ui.startListening();
            mUi.clear();
        }
    }

    private void setSurface(SurfaceView surface) {
        if (playerController instanceof ITunesPlayerController) {
            final ITunesPlayerController controller = (ITunesPlayerController) playerController;
            controller.setVideoSurface(surface);
        }
    }

}