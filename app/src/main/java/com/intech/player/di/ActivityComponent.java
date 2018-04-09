package com.intech.player.di;

import com.intech.player.android.activities.PlayerActivity;
import com.intech.player.android.activities.TrackListActivity;
import com.intech.player.di.modules.FragmentModule;

import dagger.Component;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 09.04.18
 */
@Component(modules = {FragmentModule.class})
public interface ActivityComponent {
    void inject(TrackListActivity activity);
    void inject(PlayerActivity activity);
}
