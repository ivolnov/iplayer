package com.intech.player.clean.entities;

/**
 * A jpg image.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
public class Artwork {
    private byte[] bytes;

    public Artwork(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
