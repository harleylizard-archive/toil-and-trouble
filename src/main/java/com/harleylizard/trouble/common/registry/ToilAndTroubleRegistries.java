package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.ritual.RitualType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ToilAndTroubleRegistries {
    public static final ResourceKey<Registry<RitualType<?>>> RITUAL_TYPE_REGISTRY_KEY = resourceKey("ritual_type");

    public static final Registry<RitualType<?>> RITUAL_TYPE = FabricRegistryBuilder.createSimple(RITUAL_TYPE_REGISTRY_KEY).buildAndRegister();

    private ToilAndTroubleRegistries() {}

    private static <T> ResourceKey<Registry<T>> resourceKey(String name) {
        return ResourceKey.createRegistryKey(ToilAndTrouble.resourceLocation(name));
    }
}
