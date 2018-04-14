package com.intech.player.di.modules;

import com.intech.player.clean.boundaries.PlayerController;
import com.intech.player.clean.boundaries.SearchQueryRepository;
import com.intech.player.clean.boundaries.TrackService;
import com.intech.player.clean.interactors.GetLastSearchQueryUseCase;
import com.intech.player.clean.interactors.GetPlayerEventsUseCase;
import com.intech.player.clean.interactors.GetTrackListUseCase;
import com.intech.player.clean.interactors.PausePlayerUseCase;
import com.intech.player.clean.interactors.SetLastSearchQueryUseCase;
import com.intech.player.clean.interactors.StartPlayerUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
@Module(includes = {ITunesModule.class, RepositoriesModule.class})
public class UseCaseModule {
    @Provides
    GetTrackListUseCase provideTrackListUseCase(TrackService service) {
        return new GetTrackListUseCase(service);
    }

    @Provides
    StartPlayerUseCase provideStartPlayerUseCase(PlayerController controller) {
        return new StartPlayerUseCase(controller);
    }

    @Provides
    PausePlayerUseCase providePausePlayerUseCase(PlayerController controller) {
        return new PausePlayerUseCase(controller);
    }

    @Provides
    GetPlayerEventsUseCase provideGetPlayerEventsUseCase(PlayerController controller) {
        return new GetPlayerEventsUseCase(controller);
    }

    @Provides
    GetLastSearchQueryUseCase provideGetLastSearchQueryUseCase(SearchQueryRepository repository) {
        return new GetLastSearchQueryUseCase(repository);
    }

    @Provides
    SetLastSearchQueryUseCase provideSetLastSearchQueryUseCase(SearchQueryRepository repository) {
        return  new SetLastSearchQueryUseCase(repository);
    }
}
