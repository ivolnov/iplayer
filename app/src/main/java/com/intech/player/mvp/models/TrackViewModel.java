package com.intech.player.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A DTO for the MVP layer representing a track.
 *
 * @author Ivan Volnov
 * @since 08.04.18
 */
public class TrackViewModel implements Parcelable {

    public static final Creator<TrackViewModel> CREATOR = new Creator<TrackViewModel>() {
        @Override
        public TrackViewModel createFromParcel(Parcel parcel) {
            return new TrackViewModel(parcel);
        }

        @Override
        public TrackViewModel[] newArray(int size) {
            return new TrackViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(artist);
        parcel.writeString(trackName);
        parcel.writeString(artworkUrl);
        parcel.writeString(previewUrl);
    }

    private boolean isAudio;
    private String artist;
    private String trackName;
    private String artworkUrl;
    private String previewUrl;

    public TrackViewModel() {}

    private TrackViewModel(Parcel parcel) {
        artist = parcel.readString();
        trackName = parcel.readString();
        artworkUrl = parcel.readString();
        previewUrl = parcel.readString();
    }

    public String getArtist() {
        return artist;
    }

    public TrackViewModel setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getTrackName() {
        return trackName;
    }

    public TrackViewModel setTrackName(String trackName) {
        this.trackName = trackName;
        return this;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public TrackViewModel setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
        return this;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public TrackViewModel setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
        return this;
    }
}
