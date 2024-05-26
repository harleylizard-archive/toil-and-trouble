package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ToilAndTroubleItems {
    public static final Item BREWING_CAULDRON = blockItem(ToilAndTroubleBlocks.BREWING_CAULDRON);

    private ToilAndTroubleItems() {}

    public static void register() {
        register("brewing_cauldron", BREWING_CAULDRON);
    }

    private static void register(String name, Item item) {
        Registry.register(BuiltInRegistries.ITEM, ToilAndTrouble.resourceLocation(name), item);
    }

    private static Item blockItem(Block block) {
        return new BlockItem(block, new Item.Properties());
    }
}
