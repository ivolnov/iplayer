package com.intech.player.clean.boundaries;

import com.intech.player.clean.boundaries.model.ArtworkRequestModel;
import com.intech.player.clean.boundaries.model.PreviewRequestModel;
import com.intech.player.clean.boundaries.model.TrackRequestModel;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 05.04.18
 */
public interface TrackService {
    Observable<TrackRequestModel> getTracks(String keyword);
    Single<ArtworkRequestModel> getArtwork(String url);
    Single<PreviewRequestModel> getPreview(String url);
}