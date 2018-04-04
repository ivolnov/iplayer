package com.intech.player.api;

import android.support.annotation.NonNull;

import com.intech.player.clean.entities.Artwork;
import com.intech.player.clean.entities.Preview;
import com.intech.player.clean.entities.Track;
import com.intech.player.clean.interactors.TrackListUseCase;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 03.04.18
 */
public class ITunesTrackService implements TrackListUseCase.TrackService {

    private static final String VIDEO_SUFFIX = "m4v";

    private ITunesApi api;

    public ITunesTrackService(@NonNull ITunesApi api) {
        this.api = api;
    }

    @Override
    public Observable<Track> getTracks(@NonNull String keyword) {
        return api
                .getTrackList(keyword)
                .flatMapObservable(ITunesTrackService::toTrackObservable);
    }

    @Override
    public Single<Artwork> getArtwork(@NonNull String url) {
        return api
                .getFile(url)
                .map(body -> new Artwork(body.bytes()));
    }

    @Override
    public Single<Preview> getPreview(@NonNull String url) {
        final Preview.Type type = url.endsWith(VIDEO_SUFFIX)
                ? Preview.Type.M4V
                : Preview.Type.M4A;
        return api
                .getFile(url)
                .map(body -> new Preview(type, body.bytes()));
    }


    private static Observable<Track> toTrackObservable(@NonNull final TrackListResponseModel model) {
        return Observable.create(emitter -> {
            if (!model.getResults().isEmpty()) {
                for (Result result: model.getResults()) {
                    emitter.onNext(toTrackEntity(result));
                }
                emitter.onComplete();
            }
        });
    }

    private static Track toTrackEntity(@NonNull Result result) {
        return new Track()
                .setArtist(result.getArtistName())
                .setTrackName(result.getTrackName())
                .setArtworkUrl(result.getArtworkUrl100())
                .setPreviewUrl(result.getPreviewUrl());
    }
}
