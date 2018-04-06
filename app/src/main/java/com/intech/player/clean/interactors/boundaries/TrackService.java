package com.intech.player.clean.interactors.boundaries;

import com.intech.player.clean.entities.Artwork;
import com.intech.player.clean.entities.Preview;
import com.intech.player.clean.entities.Track;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 05.04.18
 */
public interface TrackService {
    Observable<Track> getTracks(String keyword);
    Single<Artwork> getArtwork(String url);
    Single<Preview> getPreview(String url);
}