package com.intech.player.api;

import com.intech.player.clean.boundaries.model.TrackListResponseModel;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Retrofit powered api to ITunes that provides a track response model.
 *
 * @author Ivan Volnov
 * @since 03.04.18
 */

public interface ITunesApi {

    @GET("/search")
    Single<TrackListResponseModel> getTrackList(@Query("term") String keyword);

    @GET
    Single<ResponseBody> getFile(@Url String url);
}
