package com.intech.player.mvp.models.utils;

import com.intech.player.clean.boundaries.model.TrackRequestModel;
import com.intech.player.mvp.models.TrackViewModel;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 08.04.18
 */
public class ModelConverter {

    private static final String VIDEO_EXTENSION = "m4v";

    public static TrackViewModel asTrackViewModel(TrackRequestModel track) {
        return new TrackViewModel()
                .setArtist(track.getArtist())
                .setTrackName(track.getTrackName())
                .setArtworkUrl(track.getArtworkUrl())
                .setPreviewUrl(track.getPreviewUrl());
    }

    public static TrackRequestModel asTrackRequestModel(TrackViewModel track) {
        return new TrackRequestModel()
                .setArtist(track.getArtist())
                .setTrackName(track.getTrackName())
                .setArtworkUrl(track.getArtworkUrl())
                .setPreviewUrl(track.getPreviewUrl());
    }

    public static boolean isVideo(TrackViewModel track) {
        return track.getPreviewUrl().endsWith(VIDEO_EXTENSION);
    }
}
