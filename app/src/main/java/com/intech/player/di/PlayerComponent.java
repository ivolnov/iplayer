package com.intech.player.di;

import android.content.Context;

import com.intech.player.clean.boundaries.model.TrackRequestModel;
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
@Component(modules = {ExoPlayerModule.class})
public interface PlayerComponent {

    void inject(ITunesPlayerController playerController);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder track(@Named("track") TrackRequestModel track);
        @BindsInstance
        Builder context(@Named("context") Context context);

        PlayerComponent build();
    }
}
