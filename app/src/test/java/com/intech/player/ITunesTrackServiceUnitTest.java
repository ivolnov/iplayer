package com.intech.player;

import com.intech.player.api.ITunesApi;
import com.intech.player.api.ITunesTrackService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.junit.Test;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.intech.player.util.TestData.EXPECTED_ARTWORK;
import static com.intech.player.util.TestData.EXPECTED_ARTWORK_URL;
import static com.intech.player.util.TestData.EXPECTED_COUNT;
import static com.intech.player.util.TestData.EXPECTED_M4A_PREVIEW;
import static com.intech.player.util.TestData.EXPECTED_M4A_PREVIEW_URL;
import static com.intech.player.util.TestData.EXPECTED_M4V_PREVIEW;
import static com.intech.player.util.TestData.EXPECTED_M4V_PREVIEW_URL;
import static com.intech.player.util.TestData.KEYWORD;
import static com.intech.player.util.TestData.SENSELESS_STRING;
import static com.intech.player.util.TestUtils.equalTo;
import static com.intech.player.util.TestUtils.expectedTrack;
import static com.intech.player.util.TestUtils.http400Error;

/**
 * {@link ITunesTrackService} unit tests.
 * Fetches data from ITunes thus network is required for tests to pass.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
public class ITunesTrackServiceUnitTest {

    @Test
    public void testsGetTracksOk() {
        new ITunesTrackService(buildApi())
                .getTracks(KEYWORD)
                .test()
                .assertValueCount(EXPECTED_COUNT)
                .assertValueAt(0, track -> track.equals(expectedTrack()));
    }

    @Test
    public void testGetTracksEmpty() {
        new ITunesTrackService(buildApi())
                .getTracks(SENSELESS_STRING)
                .test()
                .assertEmpty();
    }

    @Test
    public void testGetArtworkOk() {
        new ITunesTrackService(buildApi())
                .getArtwork(EXPECTED_ARTWORK_URL)
                .test()
                .assertValue(equalTo(EXPECTED_ARTWORK));
    }

    @Test
    public void testGetArtworkError() {
        new ITunesTrackService(buildApi())
                .getArtwork(SENSELESS_STRING)
                .test()
                .assertError(http400Error());
    }

    @Test
    public void testGetAudioPreviewOk() {
        new ITunesTrackService(buildApi())
                .getPreview(EXPECTED_M4A_PREVIEW_URL)
                .test()
                .assertValue(equalTo(EXPECTED_M4A_PREVIEW));
    }

    /**
     * Takes an infinity...
     */
    @Test
    public void testGetVideoPreviewOk() {
        new ITunesTrackService(buildApi())
                .getPreview(EXPECTED_M4V_PREVIEW_URL)
                .test()
                .assertValue(equalTo(EXPECTED_M4V_PREVIEW));
    }

    @Test
    public void testGetPreviewError() {
        new ITunesTrackService(buildApi())
                .getPreview(SENSELESS_STRING)
                .test()
                .assertError(http400Error());
    }

    private ITunesApi buildApi() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.ITUNES_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(ITunesApi.class);
    }
}
