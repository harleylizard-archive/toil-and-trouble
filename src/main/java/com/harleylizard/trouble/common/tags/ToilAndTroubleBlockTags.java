package com.harleylizard.trouble.common.tags;

import com.harleylizard.trouble.common.ToilAndTrouble;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ToilAndTroubleBlockTags {
    public static final TagKey<Block> HEAT_SOURCE = tagKey("heat_source");

    private ToilAndTroubleBlockTags() {}

    private static TagKey<Block> tagKey(String name) {
        return TagKey.create(Registries.BLOCK, ToilAndTrouble.resourceLocation(name));
    }
}
