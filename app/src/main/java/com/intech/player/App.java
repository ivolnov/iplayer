package com.intech.player;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.intech.player.di.AppComponent;
import com.intech.player.di.DaggerAppComponent;

/**
 * PlayerController application itself.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class App extends Application {
    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sAppComponent = DaggerAppComponent
                .builder()
                .build();
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    @VisibleForTesting
    public static void setAppComponent(@NonNull AppComponent appComponent) {
        sAppComponent = appComponent;
    }
}
