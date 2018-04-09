package com.intech.player.mvp.presenters;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.intech.player.App;
import com.intech.player.clean.interactors.GetTrackListUseCase;
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

    @Inject
    GetTrackListUseCase getTrackListUseCase;

    private Disposable mTrackListDisposable;

    public TrackListPresenter() {
        App.getAppComponent().inject(this);
    }

    public void onEnterQuery(@NonNull String query) {
        dispose();

        mTrackListDisposable = getTrackListUseCase
                .execute(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        track -> getViewState().addTrack(asTrackViewModel(track)),
                        error -> getViewState().showError(getMessage(error))
                );

    }

    private void dispose() {
        if (mTrackListDisposable != null) {
            mTrackListDisposable.dispose();
        }
    }

    private String getMessage(Throwable error) {
        return UserMessageCompiler.from(error);
    }
}
