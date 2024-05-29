package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.brewing.*;
import net.minecraft.core.Registry;

public final class ToilAndTroubleHasIngredientListTypes {
    public static final HasIngredientListType<BrewingEffect> BREWING_EFFECT = new HasIngredientListType<>(BrewingEffect.CODEC);
    public static final HasIngredientListType<BrewingRecipe> BREWING_RECIPE = new HasIngredientListType<>(BrewingRecipe.CODEC);
    public static final HasIngredientListType<BrewingRitual> BREWING_RITUAL = new HasIngredientListType<>(BrewingRitual.CODEC);

    private ToilAndTroubleHasIngredientListTypes() {}

    public static void register() {
        register("brewing_effect", BREWING_EFFECT);
        register("brewing_recipe", BREWING_RECIPE);
        register("brewing_ritual", BREWING_RITUAL);
    }

    private static <T extends HasIngredientList> void register(String name, HasIngredientListType<T> type) {
        Registry.register(ToilAndTroubleRegistries.HAS_INGREDIENT_LIST, ToilAndTrouble.resourceLocation(name), type);
    }
}
