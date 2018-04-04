package com.intech.player.di.modules;

import com.intech.player.api.ITunesTrackService;
import com.intech.player.clean.interactors.TrackListUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
@Module(includes = {ITunesModule.class})
public class UseCaseModule {
    @Provides
    @Singleton
    TrackListUseCase provideTrackListuseCase(ITunesTrackService service) {
        return new TrackListUseCase(service);
    }
}
