package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.brewing.HasIngredientListType;
import com.harleylizard.trouble.common.ritual.RitualType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ToilAndTroubleRegistries {
    public static final ResourceKey<Registry<RitualType<?>>> RITUAL_TYPE_REGISTRY_KEY = resourceKey("ritual_type");
    public static final ResourceKey<Registry<HasIngredientListType<?>>> HAS_INGREDIENT_LIST_TYPE = resourceKey("has_ingredient_list");

    public static final Registry<RitualType<?>> RITUAL_TYPE = FabricRegistryBuilder.createSimple(RITUAL_TYPE_REGISTRY_KEY).buildAndRegister();
    public static final Registry<HasIngredientListType<?>> HAS_INGREDIENT_LIST = FabricRegistryBuilder.createSimple(HAS_INGREDIENT_LIST_TYPE).buildAndRegister();

    private ToilAndTroubleRegistries() {}

    private static <T> ResourceKey<Registry<T>> resourceKey(String name) {
        return ResourceKey.createRegistryKey(ToilAndTrouble.resourceLocation(name));
    }
}
