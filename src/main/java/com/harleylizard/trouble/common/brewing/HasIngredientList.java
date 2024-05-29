package com.harleylizard.trouble.common.brewing;

import com.harleylizard.trouble.common.DataLookup;
import com.harleylizard.trouble.common.FloatComparable;
import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.blockentity.BrewingCauldronIngredients;
import com.harleylizard.trouble.common.registry.ToilAndTroubleRegistries;
import com.mojang.serialization.Codec;

import java.util.List;

public sealed interface HasIngredientList extends FloatComparable<BrewingCauldronIngredients> permits BrewingEffect, BrewingRecipe, BrewingRitual {
    Codec<HasIngredientList> CODEC = ToilAndTroubleRegistries.HAS_INGREDIENT_LIST.byNameCodec().dispatch(HasIngredientList::getType, HasIngredientListType::getCodec);

    DataLookup<HasIngredientList> DATA_LOOKUP = DataLookup.of();

    void brew(BrewingCauldronBlockEntity blockEntity);

    List<ItemStackInfo> getIngredientList();

    HasIngredientListType<?> getType();

    @Override
    default float compareTo(BrewingCauldronIngredients ingredients) {
        var list = getIngredientList();
        var i = 0;
        for (var ingredient : ingredients) {
            for (var itemStackInfo : list) {
                if (itemStackInfo.test(ingredient)) {
                    i++;
                }
            }
        }
        return (float) i / (float) list.size();
    }
}
