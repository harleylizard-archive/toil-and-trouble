package com.harleylizard.trouble.common.brewing;

import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.registry.ToilAndTroubleHasIngredientListTypes;
import com.mojang.serialization.Codec;

import java.util.List;

public final class BrewingEffect implements HasIngredientList {
    public static final Codec<BrewingEffect> CODEC = Codec.unit(() -> null);

    @Override
    public void brew(BrewingCauldronBlockEntity blockEntity) {

    }

    @Override
    public List<ItemStackInfo> getIngredientList() {
        return null;
    }

    @Override
    public HasIngredientListType<?> getType() {
        return ToilAndTroubleHasIngredientListTypes.BREWING_EFFECT;
    }
}
