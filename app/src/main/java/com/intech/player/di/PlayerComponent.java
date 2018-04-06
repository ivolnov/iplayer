package com.intech.player.di;

import android.content.Context;

import com.intech.player.controller.ITunesPlayerController;
import com.intech.player.di.modules.ExoPlayerModule;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 06.04.18
 */
@UriScope
@Component(modules = {ExoPlayerModule.class/*, ContextModule.class*/})
public interface PlayerComponent {

    void inject(ITunesPlayerController playerController);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder uri(@Named("uri") String uri);
        @BindsInstance
        Builder context(@Named("context") Context context);

        /*Builder contextModule(ContextModule module);*/

        PlayerComponent build();
    }
}
