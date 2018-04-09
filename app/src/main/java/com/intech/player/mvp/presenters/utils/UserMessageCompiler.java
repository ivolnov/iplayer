package com.intech.player.mvp.presenters.utils;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 08.04.18
 */
public class UserMessageCompiler {

    public static String from(Throwable error) {
        return String.format("Oops...%s", error.getMessage());
    }
}
