package com.intech.player.mvp.views;

import com.arellomobile.mvp.MvpView;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public interface PlayerView extends MvpView {
    void showError(String error);
    void setButtonPlay();
    void setButtonPause();
    void setProgress(double progress);
}
