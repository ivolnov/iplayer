package com.intech.player.clean.boundaries.model;

import java.util.Objects;

/**
 * A DTO passed to lower layers to represent track entity's data.
 *
 * @author Ivan Volnov
 * @since 03.04.18
 */
public class TrackRequestModel {
    private String artist;
    private String trackName;
    private String artworkUrl;
    private String previewUrl;

    public String getArtist() {
        return artist;
    }

    public TrackRequestModel setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getTrackName() {
        return trackName;
    }

    public TrackRequestModel setTrackName(String trackName) {
        this.trackName = trackName;
        return this;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public TrackRequestModel setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
        return this;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public TrackRequestModel setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackRequestModel track = (TrackRequestModel) o;
        return Objects.equals(artist, track.artist) &&
                Objects.equals(trackName, track.trackName) &&
                Objects.equals(artworkUrl, track.artworkUrl) &&
                Objects.equals(previewUrl, track.previewUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artist, trackName, artworkUrl, previewUrl);
    }
}