package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.block.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public final class ToilAndTroubleBlocks {
    public static final Block BELLOWS = new BellowsBlock(Properties.copy(Blocks.OAK_PLANKS));
    public static final Block BREWING_CAULDRON = new BrewingCauldronBlock(Properties.copy(Blocks.CAULDRON).lightLevel(BrewingCauldron::getLightLevel));
    public static final Block COTTON = new CottonCropBlock(cropProperties());
    public static final Block MANDRAKE = new MandrakeCropBlock(cropProperties());

    private ToilAndTroubleBlocks() {}

    public static void register() {
        register("bellows", BELLOWS);
        register("brewing_cauldron", BREWING_CAULDRON);
        register("cotton", COTTON);
        register("mandrake", MANDRAKE);
    }

    private static void register(String name, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, ToilAndTrouble.resourceLocation(name), block);
    }

    private static Properties cropProperties() {
        return Properties.copy(Blocks.WHEAT);
    }
}
