package com.intech.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
@RunWith(AndroidJUnit4.class)
public class CrazyResearchLabInstrumentedTest {

    @Test
    public void name() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(appContext),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        Uri videoUri = Uri.parse("http://video.itunes.apple.com/apple-assets-us-std-000001/Video117/v4/26/61/36/26613688-6caf-f658-b461-5a01d61d7e79/mzvf_4101194284929287983.640x458.h264lc.U.p.m4v");


        player.setPlayWhenReady(true);

        MediaSource mediaSource = buildMediaSource(appContext, videoUri);

        player.prepare(mediaSource);
        player.release();

    }


    private MediaSource buildMediaSource(Context context, Uri uri) {

        Looper.prepare();

        Handler eventHandler = new Handler();

        DefaultBandwidthMeter meter = new DefaultBandwidthMeter();

        HttpDataSource.Factory mediaDataSourceFactory = buildHttpDataSourceFactory(context, meter);

        TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(meter);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);

        EventLogger eventLogger = new EventLogger(trackSelector);

        ExtractorMediaSource source = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(uri, eventHandler, eventLogger);

        return source;
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(Context context, DefaultBandwidthMeter meter) {

        String userAgent = Util.getUserAgent(context, BuildConfig.APPLICATION_ID);

        return new DefaultHttpDataSourceFactory(userAgent, meter);
    }

    //https://itunes.apple.com/search?term=Hendrix&entity=musicTrack&attribute=artistTerm

    //Context appContext = InstrumentationRegistry.getTargetContext();
}
