package com.intech.player.clean.boundaries.model;

/**
 * A DTO passed to lower layers to represent preview entity's data..
 *
 * @author Ivan Volnov
 * @since 04.04.18
 */
public class PreviewRequestModel {

    public enum Type {M4V, M4A}

    private final Type type;
    private final byte[] bytes;


    public PreviewRequestModel(Type type, byte[] bytes) {
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
