package com.intech.player.di;

import android.content.Context;

import com.intech.player.android.services.PlayerBoundForegroundService;
import com.intech.player.di.modules.ContextModule;
import com.intech.player.di.modules.UseCaseModule;
import com.intech.player.mvp.presenters.TrackListPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Application's dependencies.
 *
 * @since 01.04.18
 * @author Ivan Volnov
 */

@Singleton
@Component(modules = {UseCaseModule.class, ContextModule.class})
public interface AppComponent {
    void inject(TrackListPresenter presenter);
    void inject(PlayerBoundForegroundService service);
    //void inject(PlayerPresenter presenter);

    Context getContext();
}
