package com.intech.player.mvp.views;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.intech.player.mvp.models.TrackViewModel;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public interface TrackListView extends MvpView {
    void addTrack(@NonNull TrackViewModel track);
    void showError(@NonNull String message);
}
