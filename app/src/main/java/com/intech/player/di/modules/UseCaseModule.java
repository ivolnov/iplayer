package com.intech.player.di.modules;

import com.intech.player.api.ITunesTrackService;
import com.intech.player.clean.interactors.GetTrackListUseCase;
import com.intech.player.clean.interactors.StartPlayerUseCase;
import com.intech.player.controller.ITunesPlayerController;

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
    GetTrackListUseCase provideTrackListUseCase(ITunesTrackService service) {
        return new GetTrackListUseCase(service);
    }

    @Provides
    StartPlayerUseCase provideStartPlayerUseCase(ITunesPlayerController player) {
        return new StartPlayerUseCase(player);
    }
}
