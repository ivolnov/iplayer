package com.intech.player.clean.entities;

/**
 * An m4v video.
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
public class Preview {

    public enum Type {M4V, M4A}

    private final Type type;
    private final byte[] bytes;


    public Preview(Type type, byte[] bytes) {
        this.type = type;
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Type getType() {
        return type;
    }
}
