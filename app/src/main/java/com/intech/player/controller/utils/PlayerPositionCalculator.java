package com.intech.player.controller.utils;

import android.support.annotation.NonNull;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;

/**
 * A simplified version of:
 * com.google.android.exoplayer2.ui.PlayerControlView.updateProgress()
 * from 'com.google.android.exoplayer:exoplayer-ui:2.X.X' dependency
 * without all those fancy bells and whistles...
 *
 * @author Ivan Volnov
 * @since 07.04.18
 */
public class PlayerPositionCalculator {

    /**
     * Gets current progress of the give player.
     *
     * @param player an {@link ExoPlayer} instance;
     * @return a floating point number representing progress ratio
     */
    public static double calculate(@NonNull ExoPlayer player) {

        long currentWindowTimeBarOffsetUs = 0;
        long durationUs = 0;
        long position;
        long duration;

        for (int i=0; i < player.getCurrentTimeline().getWindowCount(); i ++) {
            final Timeline.Window window = new Timeline.Window();
            player.getCurrentTimeline().getWindow(i, window);

            if (i == player.getCurrentWindowIndex()) {
                currentWindowTimeBarOffsetUs = durationUs;
            }

            durationUs += window.durationUs;
        }

        duration = C.usToMs(durationUs);
        position = C.usToMs(currentWindowTimeBarOffsetUs);

        return position / duration;
    }
}
