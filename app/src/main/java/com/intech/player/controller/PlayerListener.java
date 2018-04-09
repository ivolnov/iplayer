package com.intech.player.controller;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.intech.player.clean.boundaries.model.EventRequestModel;
import com.intech.player.clean.boundaries.model.TrackRequestModel;

import io.reactivex.Observer;

import static com.intech.player.clean.boundaries.model.utils.ModelConverter.asPauseEvent;
import static com.intech.player.clean.boundaries.model.utils.ModelConverter.asProgressEvent;
import static com.intech.player.clean.boundaries.model.utils.ModelConverter.asStartEvent;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 07.04.18
 */
public class PlayerListener implements Player.EventListener {

    private Observer<? super EventRequestModel> observer;
    private final EventRequestModel startEvent;
    private final EventRequestModel pauseEvent;

    public PlayerListener(TrackRequestModel track)  {
        this.startEvent = asStartEvent(track);
        this.pauseEvent = asPauseEvent(track);
    }

    public void removeObserver() {
        if (observer != null) {
            observer.onComplete();
        }
        observer = null;
    }

    public void addObserver(Observer<? super EventRequestModel> observer) {
        this.observer = observer;
    }

    public void onProgressChanged(double progress) {
        if (observer != null) {
            observer.onNext(asProgressEvent(progress));
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (observer != null) {
            observer.onNext(playWhenReady ? startEvent : pauseEvent);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (observer != null) {
            observer.onError(error);
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    public EventRequestModel getStartEvent() {
        return startEvent;
    }

    public EventRequestModel getPauseEvent() {
        return pauseEvent;
    }
}
