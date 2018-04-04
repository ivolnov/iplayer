package com.intech.player.android.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * An intent service that runs in foreground and plays a track.
 * Driven by intents that impersonate player state.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class PlayerForegroundService extends IntentService {

    public static final String SERVICE_NAME = "PlayerController";
    public static final String CHANNEL_ID = "PlayerController";

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_STOP = "ACTION_STOP";

    public static final String EXTRA_ARTIST = "extra_artist";
    public static final String EXTRA_TRACK = "extra_track";

    public static final int FOREGROUND_ID = 1;
    public static final int NOTIFICATION_ID = 1;

    public static final int MAX_PROGRESS = 100;

    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManagerCompat mNotificationManager;

    private int mProgress;
    private String mArtist;
    private String mTrack;

    public PlayerForegroundService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_START:
                    startForeground(intent);
                case ACTION_PLAY:
                    playPlayer();
                    break;
                case ACTION_PAUSE:
                    pausePlayer();
                    break;
                case ACTION_STOP:
                    stopPlayer();
                    break;
            }
        }

    }

    private void startForeground(@NonNull Intent intent) {
        startForeground(FOREGROUND_ID, buildPlayNotification());
        mArtist = intent.getStringExtra(EXTRA_ARTIST);
        mTrack = intent.getStringExtra(EXTRA_TRACK);
        playPlayer();
    }

    private void playPlayer() {
        //play it...
    }

    private void pausePlayer() {
        //pause it
        getNotificationManager().notify(NOTIFICATION_ID, buildPauseNotification());
    }

    private void stopPlayer() {
        //stop it
        stopForeground(true);
    }

    private void onTrackProgress(int progress) {
        mProgress = progress;
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
}