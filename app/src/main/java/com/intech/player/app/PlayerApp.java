package com.intech.player.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.intech.player.di.AppComponent;
import com.intech.player.di.DaggerAppComponent;

/**
 * Player application itself.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class PlayerApp extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent
                .builder()
                .build();
    }

    @VisibleForTesting
    public void setAppComponent(@NonNull AppComponent appComponent) {
        mAppComponent = appComponent;
    }
}
