package com.intech.player.di.modules;

import com.intech.player.api.ITunesApi;
import com.intech.player.api.ITunesTrackService;
import com.intech.player.controller.ITunesPlayerController;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * ITunes api service.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
@Module(includes = {RetrofitModule.class})
public class ITunesModule {
    @Provides
    public ITunesTrackService provideITunesTrackService(ITunesApi api) {
        return new ITunesTrackService(api);
    }

    @Provides
    public ITunesApi provideITunesApi(Retrofit retrofit) {
        return retrofit.create(ITunesApi.class);
    }

    @Provides
    @Singleton
    public ITunesPlayerController provideITunesPlayerController() {
        return new ITunesPlayerController();
    }
}
