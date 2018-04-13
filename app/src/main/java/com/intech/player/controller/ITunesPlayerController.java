package com.intech.player.controller;

import android.support.annotation.VisibleForTesting;
import android.view.SurfaceView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.intech.player.clean.boundaries.PlayerController;
import com.intech.player.clean.boundaries.model.EventRequestModel;
import com.intech.player.controller.utils.PlayerPositionCalculator;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 03.04.18
 */
public class ITunesPlayerController implements PlayerController {

    private static final int POLL_PROGRESS_EACH = 200;
    private static final int POLL_PROGRESS_AFTER = 0;
    private static final TimeUnit POLL_PROGRESS_UNIT = TimeUnit.MILLISECONDS;

    @Inject
    SimpleExoPlayer player;

    @Inject
    PlayerListener listener;

    private Disposable progressDisposable;

    @Override
    public Completable start() {
        return completableFrom(() ->  {
            player.setPlayWhenReady(true);
            player.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        });
    }

    @Override
    public Completable seek(long position) {
        return completableFrom(() ->  player.seekTo(position));
    }

    @Override
    public Completable pause() {
        return completableFrom(() ->  player.setPlayWhenReady(false));
    }

    @Override
    public Completable stop() {
        return completableFrom(this::clearState);
    }

    @Override
    public Observable<EventRequestModel> getPlayerEvents() {
        final String key = UUID.randomUUID().toString();
        return new Observable<EventRequestModel>() {
            @Override
            protected void subscribeActual(Observer<? super EventRequestModel> observer) {
                listener.putObserver(key, observer);
                player.addListener(listener);
                observer.onNext(player.getPlayWhenReady()
                        ? listener.getStartEvent()
                        : listener.getPauseEvent());
            }
        }
        .subscribeOn(Schedulers.single())
        .observeOn(Schedulers.single())
        .doOnSubscribe(this::startProgressPolling)
        .doOnDispose(()-> {
            stopProgressPolling();
            listener.removeObserver(key);
            player.removeListener(listener);
        });
    }

    public void setVideoSurface(SurfaceView view) {
        player.setVideoSurfaceView(view);
		player.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
    }

    public void clearVideoSurface() {
        player.clearVideoSurface();
    }

    @VisibleForTesting
    SimpleExoPlayer getPlayer() {
        return player;
    }

    private Completable completableFrom(Runnable todo) {
        return new Completable() {
            @Override
            protected void subscribeActual(CompletableObserver s) {
                try {
                    todo.run();
                    s.onComplete();
                } catch (Throwable e) {
                    s.onError(e);
                }
            }
        };
    }

    private void startProgressPolling(Disposable noop) {
        progressDisposable = Observable
                .interval(
                        POLL_PROGRESS_AFTER,
                        POLL_PROGRESS_EACH,
                        POLL_PROGRESS_UNIT,
                        Schedulers.computation())
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe(
                        s -> listener.onProgressChanged(PlayerPositionCalculator.calculate(player))
                );
    }

    private void stopProgressPolling() {
        progressDisposable.dispose();
    }

    private void clearState() {
        if (player != null) {
            player.release();
        }
    }
}
