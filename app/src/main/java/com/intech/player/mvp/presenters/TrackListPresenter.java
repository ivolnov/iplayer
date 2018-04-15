package com.intech.player.mvp.presenters;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.intech.player.App;
import com.intech.player.clean.interactors.GetLastSearchQueryUseCase;
import com.intech.player.clean.interactors.GetTrackListUseCase;
import com.intech.player.clean.interactors.SetLastSearchQueryUseCase;
import com.intech.player.mvp.views.TrackListView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.intech.player.mvp.models.utils.ModelConverter.asTrackViewModel;
import static com.intech.player.mvp.presenters.utils.ErrorHandler.handleError;

/**
 * A {@link TrackListView} presenter that delegates a search query to the use case
 * and updates the view with incoming track view models.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
@InjectViewState
public class TrackListPresenter extends MvpPresenter<TrackListView> {

    private static final String TAG = TrackListPresenter.class.getSimpleName();

    @Inject
    GetTrackListUseCase getTrackListUseCase;
    @Inject
    GetLastSearchQueryUseCase getLastSearchQueryUseCase;
    @Inject
    SetLastSearchQueryUseCase setLastSearchQueryUseCase;

    private Disposable mTrackListDisposable;
    private Disposable mLastQueryDisposable;

    public TrackListPresenter() {
        App.getAppComponent().inject(this);
    }

    public void onGetLastQuery() {
        mLastQueryDisposable = getLastSearchQueryUseCase
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        query -> {
                            getViewState().applySearchQuery(query);
                            mLastQueryDisposable.dispose();
                        },
                        this::delegateError);
    }

    public void onEnterQuery(@NonNull String query) {
        setLastSearchQueryUseCase
                .execute(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this::getTracks)
                .doOnError(this::delegateError)
                .subscribe();
        getViewState().showPlaceholder();
    }

    private void getTracks(String query) {
        dispose();
        mTrackListDisposable = getTrackListUseCase
                .execute(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        track -> {
                            getViewState().showList();
                            getViewState().addTrack(asTrackViewModel(track));
                        },
                        this::delegateError
                );
    }

    private void dispose() {
        if (mTrackListDisposable != null) {
            mTrackListDisposable.dispose();
        }
    }

    private void delegateError(Throwable error) {
        handleError(TAG, getViewState(), error);
    }
}
