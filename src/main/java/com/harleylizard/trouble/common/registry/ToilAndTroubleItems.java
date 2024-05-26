package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public final class ToilAndTroubleItems {

    private ToilAndTroubleItems() {}

    public static void register() {
    }

    private static void register(String name, Item item) {
        Registry.register(BuiltInRegistries.ITEM, ToilAndTrouble.resourceLocation(name), item);
    }
}
