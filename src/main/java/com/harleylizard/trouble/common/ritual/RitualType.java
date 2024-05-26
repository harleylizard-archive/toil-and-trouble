package com.harleylizard.trouble.common.ritual;

import com.mojang.serialization.Codec;

public final class RitualType<T extends Ritual> {
    private final Codec<T> codec;

    public RitualType(Codec<T> codec) {
        this.codec = codec;
    }

    public Codec<T> getCodec() {
        return codec;
    }
}
