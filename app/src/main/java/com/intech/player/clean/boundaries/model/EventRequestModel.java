package com.intech.player.clean.boundaries.model;

/**
 *  A DTO passed to lower layers to represent a player's event.
 *
 * @author Ivan Volnov
 * @since 08.04.18
 */
public class EventRequestModel {

    public enum Type {
        Play,
        Pause,
        Progress
    }

    private Type type;
    private TrackRequestModel track;
    private double progress;

    public EventRequestModel(Type type, TrackRequestModel track, double progress) {
        this.type = type;
        this.track = track;
        this.progress = progress;
    }

    public Type getType() {
        return type;
    }

    public TrackRequestModel getTrack() {
        return track;
    }

    public double getProgress() {
        return progress;
    }
}
