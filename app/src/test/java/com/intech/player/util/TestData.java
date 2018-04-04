package com.intech.player.util;

import com.intech.player.clean.entities.Artwork;
import com.intech.player.clean.entities.Preview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Test utility class that stores data for tests.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
public class TestData {
    public static final int EXPECTED_COUNT = 50;
    public static final String KEYWORD = "justin bieber";
    public static final String SENSELESS_STRING = "%$?()|/";
    public static final String EXPECTED_ARTIST = "Justin Bieber";
    public static final String EXPECTED_TRACK_NAME = "Love Yourself";
    public static final String EXPECTED_ARTWORK_URL = "http://is3.mzstatic.com/image/thumb/Music6/v4/0e/38/b4/0e38b405-ae2c-1da4-5959-866f7abb110e/source/100x100bb.jpg";
    public static final String EXPECTED_M4A_PREVIEW_URL = "https://audio-ssl.itunes.apple.com/apple-assets-us-std-000001/Music69/v4/8d/89/89/8d898987-2771-877f-b06c-27813f13c79f/mzaf_6954744275616470123.plus.aac.p.m4a";
    public static final String EXPECTED_M4V_PREVIEW_URL = "http://video.itunes.apple.com/apple-assets-us-std-000001/Video117/v4/26/61/36/26613688-6caf-f658-b461-5a01d61d7e79/mzvf_4101194284929287983.640x458.h264lc.U.p.m4v";
    public static final Artwork EXPECTED_ARTWORK = getArtwork();
    public static final Preview EXPECTED_M4A_PREVIEW = getAudioPreview();
    public static final Preview EXPECTED_M4V_PREVIEW = getVideoPreview();

    public static Artwork getArtwork() {
        return new Artwork(getFileBytes("artwork.jpg"));
    }

    private static Preview getAudioPreview() {
        return new Preview(Preview.Type.M4A, getFileBytes("preview.m4a"));
    }

    private static Preview getVideoPreview() {
        return new Preview(Preview.Type.M4A, getFileBytes("preview.m4v"));
    }

    private static byte[] getFileBytes(String filename) {
        try{
            final String path = TestUtils.class
                    .getClassLoader()
                    .getResource(filename)
                    .getFile();
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            System.err.printf("Failed to open a file '%s'. Returning an empty array", filename);
            return new byte[0];
        }
    }
}
