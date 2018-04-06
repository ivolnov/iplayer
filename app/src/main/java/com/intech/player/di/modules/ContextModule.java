package com.intech.player.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 05.04.18
 */
@Module
public class ContextModule {
    private Context mContext;

    public ContextModule(Context context) {
        mContext = context;
    }

    @Provides
    public Context provideContext() {
        return mContext;
    }
}
