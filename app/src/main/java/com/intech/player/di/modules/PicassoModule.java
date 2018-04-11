package com.intech.player.di.modules;

import android.content.Context;

import com.intech.player.BuildConfig;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.OkHttpClient;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 09.04.18
 */
@Module(includes = OkHttpModule.class)
public class PicassoModule {

    @Provides
    @Reusable
    Picasso providePicasso(OkHttpClient client, Context context) {
        final Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();
        if (BuildConfig.DEBUG) {
            picasso.setLoggingEnabled(true);
        }
        return picasso;
    }
}
