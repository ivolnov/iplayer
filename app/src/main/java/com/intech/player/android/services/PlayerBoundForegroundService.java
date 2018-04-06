package com.intech.player.android.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.intech.player.App;
import com.intech.player.clean.interactors.boundaries.PlayerController;
import com.intech.player.controller.ITunesPlayerController;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

/**
 * A bound service that moves to foreground when player is on and returns to background on pause.
 * Assumed to be released with player on unbind during pause or by swiping out notification.
 * Learns info about the world by observing player events.
 *
 * Don't start explicitly.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class PlayerBoundForegroundService extends Service {

    public static final String CHANNEL_ID = "ITunesPlayer";

    public static final int FOREGROUND_ID = 1;
    public static final int NOTIFICATION_ID = 1;

    public static final int MAX_PROGRESS = 100;

    private enum PlayerState {Playing, Paused}

    private PlayerState mState = PlayerState.Paused;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManagerCompat mNotificationManager;

    private int mProgress;
    private String mArtist;
    private String mTrack;

    private Disposable mDisposable;

    @Inject
    ITunesPlayerController mPlayerController;

    public PlayerBoundForegroundService() {
        App.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mDisposable = mPlayerController
                .getPlayerEvents()
                .subscribe(
                        this::handleEvent,
                        this::handleError
                );
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mState != PlayerState.Playing) {
            mDisposable.dispose();
            mPlayerController
                    .stop()
                    .subscribe(completableHandler());
        }
        return false;
    }

    private void handleEvent(PlayerController.Event event) {
        switch (event) {
            case Start:
                startForeground(event);
                break;
            case Pause:
                stopForeground(true);
                break;
            case Progress:
                onTrackProgress(event);
                break;
        }
    }

    private void startForeground(PlayerController.Event event) {
        mArtist = event.getTrack().getArtist();
        mTrack = event.getTrack().getTrackName();
        startForeground(FOREGROUND_ID, buildPlayNotification());
    }

    private void onTrackProgress(PlayerController.Event event) {
        mProgress = event.getProgress();
        getNotificationManager().notify(NOTIFICATION_ID, buildOnProgressNotification());
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
                    .setContentTitle(mArtist)
                    .setContentText(mTrack)
                    .setProgress(MAX_PROGRESS, mProgress, false);
                    //.setDeleteIntent()
                    //.setContentIntent();
        }
        return mNotificationBuilder;
    }

    private NotificationManagerCompat getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = NotificationManagerCompat.from(this);
        }
        return mNotificationManager;
    }

    private void handleError(Throwable e) {
        e.printStackTrace(); //TODO: something smarter...
    }

    private CompletableObserver completableHandler() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                handleError(e);
            }
        };
    }
}