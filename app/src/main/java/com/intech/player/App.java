package com.intech.player;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.intech.player.di.AppComponent;
import com.intech.player.di.DaggerAppComponent;
import com.intech.player.di.modules.ContextModule;

/**
 * IPlayer application itself.
 *
 * @author Ivan Volnov
 * @since 01.04.18
 */
public class App extends Application {

    public static final int INVALID_ID = -1;
    public static final int TOLERABLE_ERROR_AMOUNT = 2;

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppComponent = DaggerAppComponent
                .builder()
                .contextModule(new ContextModule(this))
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
