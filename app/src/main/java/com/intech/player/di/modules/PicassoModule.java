package com.intech.player.di.modules;

import android.content.Context;

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
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();
    }
}
