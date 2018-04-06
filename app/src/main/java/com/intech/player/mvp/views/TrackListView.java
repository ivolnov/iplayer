package com.intech.player.mvp.views;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.intech.player.clean.entities.Artwork;
import com.intech.player.clean.entities.Track;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public interface TrackListView extends MvpView {
    void addTrack(@NonNull Track track);
    void addArtwork(@NonNull Artwork artwork);
    void showError(@NonNull String message);
}
