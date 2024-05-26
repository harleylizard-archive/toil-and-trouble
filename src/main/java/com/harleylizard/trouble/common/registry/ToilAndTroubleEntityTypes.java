package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public final class ToilAndTroubleEntityTypes {

    private ToilAndTroubleEntityTypes() {}

    public static void register() {
    }

    private static <T extends Entity> void register(String name, EntityType<T> type) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, ToilAndTrouble.resourceLocation(name), type);
    }
}
