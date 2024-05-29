package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.entity.MandrakeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ToilAndTroubleEntityTypes {
    public static final EntityType<MandrakeEntity> MANDRAKE = FabricEntityTypeBuilder.create(MobCategory.MONSTER, MandrakeEntity::new).dimensions(EntityDimensions.fixed(12.0F / 16.0F, 18.0F / 16.0F)).build();

    private ToilAndTroubleEntityTypes() {}

    public static void register() {
        register("mandrake", MANDRAKE);
    }

    private static <T extends Entity> void register(String name, EntityType<T> type) {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, ToilAndTrouble.resourceLocation(name), type);
    }
}
