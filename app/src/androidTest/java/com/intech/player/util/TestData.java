package com.intech.player.util;

import com.intech.player.clean.boundaries.model.TrackRequestModel;

/**
 * Test utility class that stores data for tests.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
public class TestData {

    public static final String EXPECTED_ARTIST = "Justin Bieber";
    public static final String EXPECTED_TRACK_NAME = "Love Yourself";
    public static final String EXPECTED_ARTWORK_URL = "http://is3.mzstatic.com/image/thumb/Music6/v4/0e/38/b4/0e38b405-ae2c-1da4-5959-866f7abb110e/source/100x100bb.jpg";
    public static final String EXPECTED_M4V_PREVIEW_URL = "http://video.itunes.apple.com/apple-assets-us-std-000001/Video117/v4/26/61/36/26613688-6caf-f658-b461-5a01d61d7e79/mzvf_4101194284929287983.640x458.h264lc.U.p.m4v";
    public static final TrackRequestModel EXPECTED_TRACK = getTrack();


    static TrackRequestModel getTrack() {
        return new TrackRequestModel()
                .setTrackName(EXPECTED_TRACK_NAME)
                .setArtist(EXPECTED_ARTIST)
                .setArtworkUrl(EXPECTED_ARTWORK_URL)
                .setPreviewUrl(EXPECTED_M4V_PREVIEW_URL);
    }

}
