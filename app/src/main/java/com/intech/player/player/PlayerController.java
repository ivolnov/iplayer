package com.intech.player.player;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 03.04.18
 */
public class PlayerController {

    BandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    void play() {


        /*Util.getDrmUuid(intent.getStringExtra(drmSchemeExtra));

        DefaultRenderersFactory renderersFactory
                = new DefaultRenderersFactory(this, drmSessionManager, extensionRendererMode);

        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        TrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);

        ExoPlayer player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);*/
    }
}
