package com.intech.player.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * An {@link MvpView} that can show two type of errors.
 *
 * @author Ivan Volnov
 * @since 15.04.18
 */
public interface ErrorView extends MvpView {
    @StateStrategyType(SkipStrategy.class)
    void showError(String error);
    @StateStrategyType(SkipStrategy.class)
    void showSevereError(String error);
}
