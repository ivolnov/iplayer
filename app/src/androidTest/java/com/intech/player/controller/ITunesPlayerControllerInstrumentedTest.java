package com.intech.player.controller;

import android.content.Context;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.intech.player.App;
import com.intech.player.clean.boundaries.PlayerController;
import com.intech.player.di.DaggerAppComponent;
import com.intech.player.di.modules.ContextModule;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.schedulers.Schedulers;

import static com.intech.player.util.TestData.EXPECTED_TRACK;
import static com.intech.player.util.TestUtils.completableObserver;
import static org.hamcrest.CoreMatchers.not;


/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 06.04.18
 */
@RunWith(AndroidJUnit4.class)
public class ITunesPlayerControllerInstrumentedTest {

    @Test
    public void newExoPlayerIsCreatedOnEveryInitTest() {
        final Context context = InstrumentationRegistry.getTargetContext();
        App.setAppComponent(
                DaggerAppComponent
                        .builder()
                        .contextModule(new ContextModule(context))
                        .build());

        Looper.prepare();

        final ITunesPlayerController controller = new ITunesPlayerController();

        initController(controller);
        final SimpleExoPlayer oldOne = controller.getPlayer();

        initController(controller);
        final SimpleExoPlayer newOne = controller.getPlayer();

        Assert.assertThat(newOne, not(oldOne));
    }

    private void initController(PlayerController controller) {
        controller.init(EXPECTED_TRACK)
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe(completableObserver());
    }
}
