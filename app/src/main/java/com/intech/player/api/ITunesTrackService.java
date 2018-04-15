package com.intech.player.api;

import android.support.annotation.NonNull;

import com.intech.player.clean.boundaries.TrackService;
import com.intech.player.clean.boundaries.model.ArtworkRequestModel;
import com.intech.player.clean.boundaries.model.PreviewRequestModel;
import com.intech.player.clean.boundaries.model.TrackRequestModel;
import com.intech.player.clean.boundaries.model.utils.ModelConverter;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * A {@link TrackService} implementation that uses {@link ITunesApi} to provide
 * request model instances with tracks' data.
 *
 * @author Ivan Volnov
 * @since 03.04.18
 */
public class ITunesTrackService implements TrackService {

    private static final String VIDEO_SUFFIX = "m4v";

    private ITunesApi api;

    public ITunesTrackService(@NonNull ITunesApi api) {
        this.api = api;
    }

    @Override
    public Observable<TrackRequestModel> getTracks(@NonNull String keyword) {
        return api
                .getTrackList(keyword)
                .flatMapObservable(ModelConverter::asTrackObservable);
    }

    @Override
    public Single<ArtworkRequestModel> getArtwork(@NonNull String url) {
        return api
                .getFile(url)
                .map(body -> new ArtworkRequestModel(body.bytes()));
    }

    @Override
    public Single<PreviewRequestModel> getPreview(@NonNull String url) {
        final PreviewRequestModel.Type type = url.endsWith(VIDEO_SUFFIX)
                ? PreviewRequestModel.Type.M4V
                : PreviewRequestModel.Type.M4A;
        return api
                .getFile(url)
                .map(body -> new PreviewRequestModel(type, body.bytes()));
    }
}
