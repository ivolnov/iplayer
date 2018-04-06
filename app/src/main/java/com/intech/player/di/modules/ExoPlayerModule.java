package com.intech.player.di.modules;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.intech.player.BuildConfig;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 05.04.18
 */
@Module
public class ExoPlayerModule {

    @Provides
    SimpleExoPlayer provideSimpleExoPlayer(DefaultRenderersFactory factory,
                                           MappingTrackSelector selector,
                                           ExtractorMediaSource source,
                                           DefaultLoadControl control) {
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(factory, selector, control);
        player.prepare(source);
        return player;
    }

    @Provides
    ExtractorMediaSource provideExtractorMediaSource(@Named("uri") String uri,
                                                     EventLogger logger,
                                                     ExtractorMediaSource.Factory factory) {
        return factory.createMediaSource(Uri.parse(uri), new Handler(), logger);
    }

    @Provides
    EventLogger provideEventLogger(MappingTrackSelector selector) {
        return new EventLogger(selector);
    }

    @Provides
    DefaultLoadControl provideDefaultLoadControl() {
        return new DefaultLoadControl();
    }

    @Provides
    @Reusable
    DefaultRenderersFactory provideDefaultRenderersFactory(@Named("context") Context context) {
        return new DefaultRenderersFactory(context);
    }

    @Provides
    @Reusable
    ExtractorMediaSource.Factory provideExtractorMediaSourceFactory(HttpDataSource.Factory factory) {
        return new ExtractorMediaSource.Factory(factory);
    }

    @Provides
    @Reusable
    MappingTrackSelector provideTrackSelector(TrackSelection.Factory factory) {
        return new DefaultTrackSelector(factory);
    }

    @Provides
    @Reusable
    TrackSelection.Factory provideTrackSelectionFactory(DefaultBandwidthMeter meter) {
        return new AdaptiveTrackSelection.Factory(meter);
    }

    @Provides
    @Reusable
    HttpDataSource.Factory provideHttpDataSourceFactory(@Named("context") Context context, DefaultBandwidthMeter meter) {
        final String userAgent = Util.getUserAgent(context, BuildConfig.USER_AGENT);
        return new DefaultHttpDataSourceFactory(userAgent, meter);
    }

    @Provides
    @Reusable
    DefaultBandwidthMeter provideBandWidthMeter() {
        return new DefaultBandwidthMeter();
    }
}
