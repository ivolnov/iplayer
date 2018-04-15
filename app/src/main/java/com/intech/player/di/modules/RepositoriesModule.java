package com.intech.player.di.modules;

import android.content.Context;

import com.intech.player.android.repositories.SharedPreferencesSearchQueryRepository;
import com.intech.player.clean.boundaries.SearchQueryRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 14.04.18
 */
@Module
public class RepositoriesModule {
    @Provides
    SearchQueryRepository getSharedPreferencesSearchQueryRepository(Context context) {
        return new SharedPreferencesSearchQueryRepository(context);
    }
}
