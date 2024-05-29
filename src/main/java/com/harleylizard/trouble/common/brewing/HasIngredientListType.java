package com.harleylizard.trouble.common.brewing;

import com.mojang.serialization.Codec;

public final class HasIngredientListType<T extends HasIngredientList> {
    private final Codec<T> codec;

    public HasIngredientListType(Codec<T> codec) {
        this.codec = codec;
    }

    public Codec<T> getCodec() {
        return codec;
    }
}
