package com.intech.player.mvp.presenters;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.intech.player.App;
import com.intech.player.BuildConfig;
import com.intech.player.clean.boundaries.model.EventRequestModel;
import com.intech.player.clean.interactors.GetPlayerEventsUseCase;
import com.intech.player.clean.interactors.PausePlayerUseCase;
import com.intech.player.clean.interactors.StartPlayerUseCase;
import com.intech.player.mvp.models.TrackViewModel;
import com.intech.player.mvp.presenters.utils.UserMessageCompiler;
import com.intech.player.mvp.views.PlayerView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.intech.player.mvp.presenters.PlayerPresenter.ButtonState.Pause;
import static com.intech.player.mvp.presenters.PlayerPresenter.ButtonState.Play;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 08.04.18
 */
@InjectViewState
public class PlayerPresenter extends MvpPresenter<PlayerView> {

    private static final String TAG = PlayerPresenter.class.getSimpleName();
    private static final String VIDEO_EXTENTION = "m4v";

    enum ButtonState {Play, Pause}

    @Inject
    StartPlayerUseCase startPlayerUseCase;
    @Inject
    PausePlayerUseCase pausePlayerUseCase;
    @Inject
    GetPlayerEventsUseCase getPlayerEventsUseCase;

    private ButtonState mButtonState = Play;

    private Disposable mPlayerEventsDisposable;

    private TrackViewModel mTrack;

    public PlayerPresenter() {
        App.getAppComponent().inject(this);
    }

    public boolean hasTrack() {
        return mTrack != null;
    }

    public void setTrack(TrackViewModel track) {
        mTrack = track;
    }

    public TrackViewModel getTrack() {
        return mTrack;
    }

    public boolean isVideo() {
        return hasTrack() && getTrack().getPreviewUrl().endsWith(VIDEO_EXTENTION);
    }

    public boolean isPaused() {
        return mButtonState == Play;
    }

    public void listenToPlayer(boolean listen) {
        disposeEvents();
        if (listen) {
            mPlayerEventsDisposable = getPlayerEventsUseCase
                    .execute()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::handleEvent,
                            this::handleError);
        }
    }

    public void buttonCLicked() {
        switch(mButtonState) {
            case Play:
                startPlayerUseCase
                        .execute()
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::toggleButton,
                                this::handleError);

                break;
            case Pause:
                pausePlayerUseCase
                        .execute()
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::toggleButton,
                                this::handleError);
                break;
        }
    }

    private void toggleButton() {
        switch(mButtonState) {
            case Play:
                mButtonState = Pause;
                getViewState().setButtonPause();
                break;
            case Pause:
                mButtonState = Play;
                getViewState().setButtonPlay();
                break;
        }
    }

    private void handleEvent(EventRequestModel event) {
        switch (event.getType()) {
            case Start:
                if (mButtonState == Play) {
                    toggleButton();
                }
                break;
            case Pause:
                if (mButtonState == Pause) {
                    toggleButton();
                }
                break;
            case Progress:
                getViewState().setProgress(event.getProgress());
                break;
        }
    }

    private void handleError(Throwable error) {
        final String message = UserMessageCompiler.from(error);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message);
        }
        getViewState().showError(message);
    }

    private void disposeEvents() {
        if (mPlayerEventsDisposable != null) {
            mPlayerEventsDisposable.dispose();
        }
    }
}
