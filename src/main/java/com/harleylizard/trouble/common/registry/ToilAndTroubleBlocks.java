package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.block.BrewingCauldron;
import com.harleylizard.trouble.common.block.BrewingCauldronBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public final class ToilAndTroubleBlocks {
    public static final Block BREWING_CAULDRON = new BrewingCauldronBlock(Properties.of().lightLevel(BrewingCauldron::getLightLevel));

    private ToilAndTroubleBlocks() {}

    public static void register() {
        register("brewing_cauldron", BREWING_CAULDRON);
    }

    private static void register(String name, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, ToilAndTrouble.resourceLocation(name), block);
    }
}
