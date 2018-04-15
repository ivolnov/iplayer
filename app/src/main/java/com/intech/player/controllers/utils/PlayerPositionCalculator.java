package com.intech.player.controllers.utils;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;

import io.reactivex.annotations.NonNull;

/**
 * A <b>static</b> utility that calculates track's progress.
 *
 * A simplified version of:
 * com.google.android.exoplayer2.ui.PlayerControlView.updateProgress()
 * from 'com.google.android.exoplayer:exoplayer-ui:2.X.X' ui library.
 * without all those fancy bells and whistles...
 *
 * @author Ivan Volnov
 * @since 07.04.18
 */
public class PlayerPositionCalculator {

    /**
     * Gets current progress of the given player.
     *
     * @param player an {@link ExoPlayer} instance;
     * @return a floating point number representing progress ratio.
     */
    public static double calculate(@NonNull ExoPlayer player) {

        long currentWindowTimeBarOffsetUs = 0L;
        long durationUs = 0L;


        for (int i=0; i < player.getCurrentTimeline().getWindowCount(); i ++) {
            final Timeline.Window window = new Timeline.Window();
            player.getCurrentTimeline().getWindow(i, window);

            if (i == player.getCurrentWindowIndex()) {
                currentWindowTimeBarOffsetUs = durationUs;
            }

            durationUs += window.durationUs;
        }

        long duration = C.usToMs(durationUs);
        Long position = C.usToMs(currentWindowTimeBarOffsetUs) + player.getCurrentPosition();

        return position.doubleValue() / duration;
    }
}
