package com.intech.player.clean.boundaries.model;

/**
 * A DTO passed to lower layers to represent artwork entity's data.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
@Deprecated
public class ArtworkRequestModel {
    private byte[] bytes;

    public ArtworkRequestModel(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
