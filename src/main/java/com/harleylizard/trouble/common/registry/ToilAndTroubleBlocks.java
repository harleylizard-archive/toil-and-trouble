package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public final class ToilAndTroubleBlocks {

    private ToilAndTroubleBlocks() {}

    public static void register() {
    }

    private static void register(String name, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, ToilAndTrouble.resourceLocation(name), block);
    }
}
