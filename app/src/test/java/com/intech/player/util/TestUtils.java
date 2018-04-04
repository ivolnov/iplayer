package com.intech.player.util;

import com.intech.player.clean.entities.Artwork;
import com.intech.player.clean.entities.Preview;
import com.intech.player.clean.entities.Track;

import io.reactivex.functions.Predicate;

import static com.intech.player.util.TestData.EXPECTED_ARTIST;
import static com.intech.player.util.TestData.EXPECTED_ARTWORK_URL;
import static com.intech.player.util.TestData.EXPECTED_M4A_PREVIEW_URL;
import static com.intech.player.util.TestData.EXPECTED_TRACK_NAME;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
public class TestUtils {

    public static Predicate<Throwable> http400Error() {
        return throwable -> throwable
                .getMessage()
                .contains("HTTP 400 Bad Request");
    }

    public static Predicate<Artwork> equalTo(Artwork reference) {
        return artwork -> equalTo(reference.getBytes()).test(artwork.getBytes());
    }

    public static Predicate<Preview> equalTo(Preview reference) {
        return preview -> equalTo(reference.getBytes()).test(preview.getBytes());
    }

    public static Predicate<byte[]> equalTo(byte[] reference) {
        return bytes -> {
            if (bytes.length != reference.length) {
                return false;
            }
            for (int i=0; i < bytes.length; i++) {
                if (bytes[i] != reference[i]) {
                    return false;
                }
            }
            return true;
        };
    }

    public static Track expectedTrack() {
        return new Track()
                .setArtist(EXPECTED_ARTIST)
                .setTrackName(EXPECTED_TRACK_NAME)
                .setArtworkUrl(EXPECTED_ARTWORK_URL)
                .setPreviewUrl(EXPECTED_M4A_PREVIEW_URL);
    }
}
