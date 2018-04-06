package com.intech.player.clean.interactors;

import com.intech.player.clean.entities.Track;
import com.intech.player.clean.interactors.boundaries.TrackService;

import io.reactivex.Observable;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
public class GetTrackListUseCase {

    private TrackService service;

    public GetTrackListUseCase(TrackService service) {
        this.service = service;
    }

    public Observable<Track> getTracks(String keyword) {
        //TODO: validate keyword
        return service.getTracks(keyword);
    }
}
