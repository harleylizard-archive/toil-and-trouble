package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;

public final class ToilAndTroubleItems {
    public static final Item BELLOWS = blockItem(ToilAndTroubleBlocks.BELLOWS);
    public static final Item BREWING_CAULDRON = blockItem(ToilAndTroubleBlocks.BREWING_CAULDRON);
    public static final Item COTTON_SEEDS = new ItemNameBlockItem(ToilAndTroubleBlocks.COTTON, new Item.Properties());
    public static final Item MANDRAKE_SEEDS = new ItemNameBlockItem(ToilAndTroubleBlocks.MANDRAKE, new Item.Properties());
    public static final Item MANDRAKE_SPAWN_EGG = new SpawnEggItem(ToilAndTroubleEntityTypes.MANDRAKE, 0xFFFFFF, 0xFFFFFF, new Item.Properties());

    private ToilAndTroubleItems() {}

    public static void register() {
        register("bellows", BELLOWS);
        register("brewing_cauldron", BREWING_CAULDRON);
        register("cotton_seeds", COTTON_SEEDS);
        register("mandrake_seeds", MANDRAKE_SEEDS);
        register("mandrake_spawn_egg", MANDRAKE_SPAWN_EGG);
    }

    private static void register(String name, Item item) {
        Registry.register(BuiltInRegistries.ITEM, ToilAndTrouble.resourceLocation(name), item);
    }

    private static Item blockItem(Block block) {
        return new BlockItem(block, new Item.Properties());
    }
}
