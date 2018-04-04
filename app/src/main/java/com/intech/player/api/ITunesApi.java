package com.intech.player.api;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Self explanatory.
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
