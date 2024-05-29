package com.harleylizard.trouble.common.brewing;

import com.harleylizard.trouble.common.MultiMap;
import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.blockentity.BrewingCauldronIngredients;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public final class ItemLookup {
    private static final MultiMap<Item, HasIngredientList> MULTI_MAP = MultiMap.mutableOf();

    private ItemLookup() {}

    public static void put(HasIngredientList hasIngredientList) {
        for (var itemStackInfo : hasIngredientList.getIngredientList()) {
            for (var item : itemStackInfo.getItems()) {
                MULTI_MAP.put(item, hasIngredientList);
            }
        }
    }

    public static void clear() {
        MULTI_MAP.clear();
    }

    public static List<HasIngredientList> getAll(Item item) {
        if (item == Items.AIR) {
            return List.of();
        }
        return MULTI_MAP.get(item);
    }

    public static List<HasIngredientList> getAll(ItemStack itemStack) {
        return getAll(itemStack.getItem());
    }

    public static HasIngredientList getAll(BrewingCauldronIngredients ingredients) {
        for (var ingredient : ingredients) {
            for (var hasIngredientList : MULTI_MAP.get(ingredient.getItem())) {
                if (ingredients.canBrew(hasIngredientList)) {
                    return hasIngredientList;
                }
            }
        }
        return null;
    }
}
