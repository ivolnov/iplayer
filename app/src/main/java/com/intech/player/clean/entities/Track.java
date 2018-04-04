package com.intech.player.clean.entities;

import java.util.Objects;

/**
 * A track business entity.
 *
 * @author Ivan Volnov
 * @since 03.04.18
 */
public class Track {
    private String artist;
    private String trackName;
    private String artworkUrl;
    private String previewUrl;

    public String getArtist() {
        return artist;
    }

    public Track setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getTrackName() {
        return trackName;
    }

    public Track setTrackName(String trackName) {
        this.trackName = trackName;
        return this;
    }

    public String getArtworkUrl() {
        return artworkUrl;
    }

    public Track setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
        return this;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public Track setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
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