package com.intech.player.mvp.views;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.intech.player.mvp.models.TrackViewModel;

/**
 * An {@link ErrorView} representing a list of tracks or a placeholder and a search field.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public interface TrackListView extends ErrorView {
    @StateStrategyType(SkipStrategy.class)
    void addTrack(@NonNull TrackViewModel track);
    @StateStrategyType(SkipStrategy.class)
    void applySearchQuery(String query);
    @StateStrategyType(SkipStrategy.class)
    void showPlaceholder();
    @StateStrategyType(SkipStrategy.class)
    void showList();
}
