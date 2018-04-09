package com.intech.player.clean.interactors;

import com.intech.player.clean.boundaries.TrackService;
import com.intech.player.clean.boundaries.model.TrackRequestModel;
import com.intech.player.clean.entities.SearchQueryRule;

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

    public Observable<TrackRequestModel> execute(String keyword) {
        return SearchQueryRule.validate(keyword)
                ? service.getTracks(keyword)
                : Observable.empty();
    }
}
