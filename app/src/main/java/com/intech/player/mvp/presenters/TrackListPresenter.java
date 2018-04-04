package com.intech.player.mvp.presenters;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.intech.player.App;
import com.intech.player.clean.entities.Track;
import com.intech.player.clean.interactors.TrackListUseCase;
import com.intech.player.mvp.views.TrackListView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
@InjectViewState
public class TrackListPresenter extends MvpPresenter<TrackListView> {

    @Inject
    TrackListUseCase trackListUseCase;

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
        mTrackListDisposable = trackListUseCase
                .getTracks(query)
                .subscribeWith(trackListHandler());

    }

    private void dispose() {
        if (mTrackListDisposable != null) {
            mTrackListDisposable.dispose();
        }
    }

    private DisposableObserver<Track> trackListHandler() {
        return new DisposableObserver<Track>() {

            @Override
            public void onNext(Track track) {
                //TODO: something to get an image...
            }

            @Override
            public void onError(Throwable t) {
                //TODO: compile an error message and ask the view to show it
            }

            @Override
            public void onComplete() {
                //TODO: time to refresh view's track list...
            }
        };
    }
}
