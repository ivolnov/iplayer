package com.intech.player.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 15.04.18
 */
public interface ErrorView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showError(String error);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSevereError(String error);
}
