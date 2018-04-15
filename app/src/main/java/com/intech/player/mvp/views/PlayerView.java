package com.intech.player.mvp.views;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public interface PlayerView extends ErrorView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setButtonPlay();
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setButtonPause();
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setProgress(double progress);
}
