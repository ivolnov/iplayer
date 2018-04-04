package com.intech.player.di;

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
@Component(modules = {UseCaseModule.class})
public interface AppComponent {
    void inject(TrackListPresenter presenter);
}
