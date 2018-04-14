package com.intech.player.mvp.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.intech.player.App;
import com.intech.player.BuildConfig;
import com.intech.player.clean.interactors.GetLastSearchQueryUseCase;
import com.intech.player.clean.interactors.GetTrackListUseCase;
import com.intech.player.clean.interactors.SetLastSearchQueryUseCase;
import com.intech.player.mvp.presenters.utils.UserMessageCompiler;
import com.intech.player.mvp.views.TrackListView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.intech.player.mvp.models.utils.ModelConverter.asTrackViewModel;

/**
 * Self explanatory.
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
                        this::handleError);
    }

    public void onEnterQuery(@NonNull String query) {
        dispose();

        mTrackListDisposable = setLastSearchQueryUseCase
                .execute(query)
                .flatMapObservable(persistedQuery -> getTrackListUseCase.execute(persistedQuery))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        track -> getViewState().addTrack(asTrackViewModel(track)),
                        this::handleError
                );

    }

    private void dispose() {
        if (mTrackListDisposable != null) {
            mTrackListDisposable.dispose();
        }
    }

    private void handleError(Throwable error) {
        final String message = UserMessageCompiler.from(error);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message);
        }
        getViewState().showError(message);
    }
}
