package com.intech.player.controller;

import android.support.annotation.VisibleForTesting;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.intech.player.App;
import com.intech.player.clean.interactors.boundaries.PlayerController;
import com.intech.player.di.DaggerPlayerComponent;

import javax.inject.Inject;

import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 03.04.18
 */
public class ITunesPlayerController implements PlayerController {

    @Inject
    SimpleExoPlayer player;

    private Observer<? super Event> mObserver;

    @Override
    public CompletableSource init(String url) {
        return completableSourceFrom(() -> {
            DaggerPlayerComponent
                    .builder()
                    .context(App.getAppComponent().getContext())
                    .uri(url)
                    .build()
                    .inject(this);
        });
    }

    @Override
    public CompletableSource start() {
        return completableSourceFrom(() ->  player.setPlayWhenReady(true));
    }

    @Override
    public CompletableSource seek(long position) {
        return completableSourceFrom(() ->  player.seekTo(position));
    }

    @Override
    public CompletableSource pause() {
        return completableSourceFrom(() ->  player.setPlayWhenReady(false));
    }

    @Override
    public CompletableSource stop() {
        return completableSourceFrom(() ->  player.release());
    }

    @Override
    public Observable<Event> getPlayerEvents() {
        return new Observable<Event>() {
            @Override
            protected void subscribeActual(Observer<? super Event> observer) {
                mObserver = observer;
            }
        }.doOnDispose(()-> mObserver = null);
    }

    @VisibleForTesting
    SimpleExoPlayer getPlayer() {
        return player;
    }

    private CompletableSource completableSourceFrom(Runnable todo) {
        return subscriber -> {
            try {
                todo.run();
                subscriber.onComplete();
            } catch (Throwable e) {
                subscriber.onError(e);
            }
        };
    }
}
