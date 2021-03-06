package com.intech.player.mvp.presenters.utils;

import com.intech.player.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * A <b>static</b> utility to map exceptions to something more human readable
 * for potential usage in UI.
 *
 * @author Ivan Volnov
 * @since 08.04.18
 */
public class UserMessageCompiler {

    private static final String EMPTY_STRING = "";
    private static final String MESSAGE_PATTERN = "Oops...%s";

    public static String from(Throwable error) {
        final String message = error.getMessage() != null
                ? error.getMessage()
                : BuildConfig.DEBUG
                ? stackTraceOf(error)
                : EMPTY_STRING;
        return String.format(MESSAGE_PATTERN, message);
    }

    /**
     * From <a href="https://stackoverflow.com/a/4812589/4003403">here</a>.
     */
    private static String stackTraceOf(Throwable e) {
        final StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
