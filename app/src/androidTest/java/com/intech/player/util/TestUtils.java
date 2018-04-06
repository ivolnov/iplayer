package com.intech.player.util;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

import static junit.framework.Assert.fail;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 06.04.18
 */
public class TestUtils {

    public static CompletableObserver completableObserver() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                fail(e.getMessage());
            }
        };
    }
}
