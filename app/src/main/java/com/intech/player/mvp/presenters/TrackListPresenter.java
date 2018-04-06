package com.intech.player.mvp.presenters;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.intech.player.App;
import com.intech.player.clean.interactors.GetTrackListUseCase;
import com.intech.player.mvp.views.TrackListView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
@InjectViewState
public class TrackListPresenter extends MvpPresenter<TrackListView> {

    @Inject
    GetTrackListUseCase getTrackListUseCase;

    private Disposable mTrackListDisposable;

    public TrackListPresenter() {
        App.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    public void onEnterQuery(@NonNull String query) {
        dispose();
        mTrackListDisposable = getTrackListUseCase
                .getTracks(query)
                .subscribe(
                        track -> getViewState().addTrack(track),
                        error -> getViewState().showError(getMessage(error))
                );

    }

    public void loadArtworks(String url) {
        //TODO: observe some use case... getViewState().addArtwork();
    }

    private void dispose() {
        if (mTrackListDisposable != null) {
            mTrackListDisposable.dispose();
        }
    }

    private String getMessage(Throwable error) {
        //TODO: compile an error
        return String.format("Oops...%s", error.getMessage());
    }
}
