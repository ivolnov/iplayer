package com.intech.player.mvp.presenters.utils;

import android.util.Log;

import com.intech.player.BuildConfig;
import com.intech.player.mvp.views.ErrorView;

import static com.intech.player.App.TOLERABLE_ERROR_AMOUNT;

/**
 * A <b>static</b> utility to help an {@link ErrorView} to determine how it should behave.
 *
 * @author Ivan Volnov
 * @since 15.04.18
 */
public class ErrorHandler {

    private static int errorCount;

    public static void handleError(String tag, ErrorView view, Throwable error) {
        final String message = UserMessageCompiler.from(error);

        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }

        if (tooMuchErrors()) {
            view.showSevereError(message);
        } else {
            view.showError(message);
        }
    }

    private static boolean tooMuchErrors() {
        if (++errorCount > TOLERABLE_ERROR_AMOUNT) {
            errorCount = 0;
            return true;
        } else {
            return false;
        }
    }
}
