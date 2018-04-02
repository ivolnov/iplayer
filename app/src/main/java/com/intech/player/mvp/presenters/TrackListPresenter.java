package com.intech.player.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.intech.player.mvp.views.TrackListView;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
@InjectViewState
public class TrackListPresenter extends MvpPresenter<TrackListView> {

    void foo() {
        getViewState();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }
}
