package com.intech.player.di.modules;

import com.intech.player.api.ITunesApi;
import com.intech.player.api.ITunesTrackService;
import com.intech.player.clean.boundaries.PlayerController;
import com.intech.player.clean.boundaries.TrackService;
import com.intech.player.controllers.ITunesPlayerController;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
@Module(includes = {RetrofitModule.class})
public class ITunesModule {
    @Provides
    public TrackService provideITunesTrackService(ITunesApi api) {
        return new ITunesTrackService(api);
    }

    @Provides
    public ITunesApi provideITunesApi(Retrofit retrofit) {
        return retrofit.create(ITunesApi.class);
    }

    @Provides
    @Singleton
    public PlayerController provideITunesPlayerController() {
        return new ITunesPlayerController();
    }
}
