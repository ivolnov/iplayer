package com.intech.player;

import android.support.test.runner.AndroidJUnit4;

import com.intech.player.api.ITunesApi;
import com.intech.player.api.ITunesTrackService;
import com.intech.player.clean.entities.Track;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
@RunWith(AndroidJUnit4.class)
public class ITunesTrackServiceInstrumentedTest {

    private static final int EXPECTED_COUNT = 50;
    private static final String KEYWORD = "justin bieber";
    private static final String EXPECTED_ARTIST = "Justin Bieber";
    private static final String EXPECTED_TRACK_NAME = "Love Yourself";
    private static final String EXPECTED_ARTWORK_URL = "http://is3.mzstatic.com/image/thumb/Music6/v4/0e/38/b4/0e38b405-ae2c-1da4-5959-866f7abb110e/source/100x100bb.jpg";
    private static final String EXPECTED_PREVIEW_URL = "https://audio-ssl.itunes.apple.com/apple-assets-us-std-000001/Music69/v4/8d/89/89/8d898987-2771-877f-b06c-27813f13c79f/mzaf_6954744275616470123.plus.aac.p.m4a";

    @Test
    public void testsGetTracks() {
        new ITunesTrackService(buildApi())
                .getTracks(KEYWORD)
                //.subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.from(backgroundLooper))
                .observeOn(AndroidSchedulers.mainThread())
                .test()
                .assertValueCount(EXPECTED_COUNT)
                .assertValueAt(0, track -> track.equals(expectedTrack()));
    }


    private ITunesApi buildApi() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.ITUNES_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(ITunesApi.class);
    }

    private Track expectedTrack() {
        return new Track()
                .setArtist(EXPECTED_ARTIST)
                .setTrackName(EXPECTED_TRACK_NAME)
                .setArtworkUrl(EXPECTED_ARTWORK_URL)
                .setPreviewUrl(EXPECTED_PREVIEW_URL);
    }

    //https://itunes.apple.com/search?term=Hendrix&entity=musicTrack&attribute=artistTerm

    //Context appContext = InstrumentationRegistry.getTargetContext();
}
