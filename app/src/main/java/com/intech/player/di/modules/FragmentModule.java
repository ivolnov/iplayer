package com.intech.player.di.modules;

import com.intech.player.android.fragments.PlayerFragment;
import com.intech.player.android.fragments.TrackListFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 09.04.18
 */
@Module
public class FragmentModule {
    @Provides
    TrackListFragment provideTrackListFragment() {
        return new TrackListFragment();
    }

    @Provides
    PlayerFragment providePlayerFragment() {
        return new PlayerFragment();
    }
}
