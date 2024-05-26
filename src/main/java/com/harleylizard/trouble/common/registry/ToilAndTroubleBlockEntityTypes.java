package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ToilAndTroubleBlockEntityTypes {
    public static final BlockEntityType<BrewingCauldronBlockEntity> BREWING_CAULDRON = FabricBlockEntityTypeBuilder.create(BrewingCauldronBlockEntity::new, ToilAndTroubleBlocks.BREWING_CAULDRON).build();

    private ToilAndTroubleBlockEntityTypes() {}

    public static void register() {
        register("brewing_cauldron", BREWING_CAULDRON);
    }

    private static <T extends BlockEntity> void register(String name, BlockEntityType<T> type) {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ToilAndTrouble.resourceLocation(name), type);
    }
}
