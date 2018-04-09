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
}
