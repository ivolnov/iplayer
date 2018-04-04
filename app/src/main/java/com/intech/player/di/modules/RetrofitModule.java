package com.intech.player.di.modules;

import com.intech.player.BuildConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit variations.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
@Module
public class RetrofitModule {
    @Provides
    @Singleton
    Retrofit provideITunesRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.ITUNES_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }
}
