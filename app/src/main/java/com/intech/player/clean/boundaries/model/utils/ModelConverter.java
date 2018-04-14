package com.intech.player.clean.boundaries.model.utils;

import android.support.annotation.NonNull;

import com.intech.player.clean.boundaries.model.EventRequestModel;
import com.intech.player.clean.boundaries.model.TrackListResponseModel;
import com.intech.player.clean.boundaries.model.TrackRequestModel;
import com.intech.player.clean.boundaries.model.TrackResponseModel;

import io.reactivex.Observable;

import static com.intech.player.clean.boundaries.model.EventRequestModel.INVALID_PROGRESS;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 08.04.18
 */
public class ModelConverter {

    public static Observable<TrackRequestModel> asTrackObservable(
            @NonNull final TrackListResponseModel model) {
        return Observable.create(emitter -> {
            if (!model.getTrackResponseModels().isEmpty()) {
                for (TrackResponseModel track: model.getTrackResponseModels()) {
                    emitter.onNext(asTrackRequestModel(track));
                }
                emitter.onComplete();
            }
        });
    }

    public static EventRequestModel asPlayEvent(@NonNull TrackRequestModel track) {
        return buildEvent(EventRequestModel.Type.Play, track, INVALID_PROGRESS);
    }

    public static EventRequestModel asPauseEvent(@NonNull TrackRequestModel track) {
        return buildEvent(EventRequestModel.Type.Pause, track, INVALID_PROGRESS);
    }

    public static EventRequestModel asProgressEvent(double progress) {
        return buildEvent(EventRequestModel.Type.Progress, null, progress);
    }

    private static TrackRequestModel asTrackRequestModel(@NonNull TrackResponseModel track) {
        return new TrackRequestModel()
                .setArtist(track.getArtistName())
                .setTrackName(track.getTrackName())
                .setArtworkUrl(track.getArtworkUrl100())
                .setPreviewUrl(track.getPreviewUrl());
    }

    private static EventRequestModel buildEvent(EventRequestModel.Type type,
                                                TrackRequestModel track,
                                                double progress) {
        return new EventRequestModel(type, track, progress);
    }
}
