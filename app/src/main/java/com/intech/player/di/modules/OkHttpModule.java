package com.intech.player.di.modules;

import android.content.Context;

import com.intech.player.BuildConfig;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.intech.player.android.utils.AndroidUtils.isNetworkAvailable;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 09.04.18
 */
@Module
public class OkHttpModule {

    private static final int CACHE_SIZE = 5 * 10 * 1024 * 1024; // 50MB... no reason...

    @Provides
    @Reusable
    OkHttpClient provideOkHttpClient(Cache cache,
                                     HttpLoggingInterceptor logging,
                                     Interceptor forceCache) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder().cache(cache);
        final List<Interceptor> interceptors = builder.networkInterceptors();

        if (BuildConfig.DEBUG) {
            interceptors.add(logging); // sensitive data...
        }

        interceptors.add(forceCache);

        return builder.build();
    }

    @Provides
    @Reusable
    Cache provideCache(Context context) {
        return new Cache(context.getCacheDir(), CACHE_SIZE);
    }

    @Provides
    @Reusable
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor();
    }

    @Provides
    @Reusable
    Interceptor provideForceCacheInterceptor(Context context) {
        return chain -> {
            Request.Builder builder = chain.request().newBuilder();
            if (!isNetworkAvailable(context)) {
                builder.cacheControl(CacheControl.FORCE_CACHE);
            }

            return chain.proceed(builder.build());
        };
    }
}
