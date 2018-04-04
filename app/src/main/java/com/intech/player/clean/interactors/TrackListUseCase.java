package com.intech.player.clean.interactors;

import com.intech.player.clean.entities.Artwork;
import com.intech.player.clean.entities.Preview;
import com.intech.player.clean.entities.Track;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
public class TrackListUseCase {

    public interface TrackService {
        Observable<Track> getTracks(String keyword);
        Single<Artwork> getArtwork(String url);
        Single<Preview> getPreview(String url);
    }

    private TrackService service;

    public TrackListUseCase(TrackService service) {
        this.service = service;
    }

    public Observable<Track> getTracks(String keyword) {
        //TODO: validate keyword
        return service.getTracks(keyword);
    }
}
