package com.harleylizard.trouble.common.tags;

import com.harleylizard.trouble.common.ToilAndTrouble;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ToilAndTroubleItemTags {

    private ToilAndTroubleItemTags() {}

    private static TagKey<Item> tagKey(String name) {
        return TagKey.create(Registries.ITEM, ToilAndTrouble.resourceLocation(name));
    }
}
