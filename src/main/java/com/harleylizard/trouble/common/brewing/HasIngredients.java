package com.harleylizard.trouble.common.brewing;

import com.harleylizard.trouble.common.DeprecatedRegistry;
import com.harleylizard.trouble.common.FloatComparable;
import com.harleylizard.trouble.common.MultiMap;
import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public sealed interface HasIngredients extends Iterable<ItemStack>, FloatComparable<BrewingCauldronBlockEntity.Ingredients> permits BrewingRecipe, BrewingRitual {
    DeprecatedRegistry<HasIngredients> DEPRECATED_REGISTRY = new DeprecatedRegistry<>();

    MultiMap<Item, HasIngredients> MULTI_MAP = MultiMap.mutableOf();

    void whenBrewed(BrewingCauldronBlockEntity blockEntity, BrewingCauldronBlockEntity.Ingredients ingredients);

    int getSize();

    @Override
    default float compareTo(BrewingCauldronBlockEntity.Ingredients ingredients) {
        var i = 0;
        for (var left : ingredients) {
            for (var right : this) {
                if (left.is(right.getItem()) && left.getCount() >= right.getCount()) {
                    i++;
                }
            }
        }
        return (float) i / (float) getSize();
    }

    static List<HasIngredients> getFrom(Item item) {
        return MULTI_MAP.get(item);
    }

    static List<HasIngredients> getFrom(ItemStack itemStack) {
        return getFrom(itemStack.getItem());
    }

    static HasIngredients getFrom(BrewingCauldronBlockEntity.Ingredients ingredients) {
        for (var ingredient : ingredients) {
            for (var hasIngredients : MULTI_MAP.get(ingredient.getItem())) {
                if (ingredients.canBrew(hasIngredients)) {
                    return hasIngredients;
                }
            }
        }
        return null;
    }

    static MapCodec<List<ItemStack>> codecIngredients() {
        return ItemStack.CODEC.listOf().fieldOf("ingredients");
    }
}
