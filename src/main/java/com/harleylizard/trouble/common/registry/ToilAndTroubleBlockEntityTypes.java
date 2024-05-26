package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ToilAndTroubleBlockEntityTypes {

    private ToilAndTroubleBlockEntityTypes() {}

    public static void register() {
    }

    private static <T extends BlockEntity> void register(String name, BlockEntityType<T> type) {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ToilAndTrouble.resourceLocation(name), type);
    }
}
