package com.harleylizard.trouble.common.ritual;

import com.harleylizard.trouble.common.DataLookup;
import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.registry.ToilAndTroubleRegistries;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.Level;

public interface Ritual {
    Codec<Ritual> CODEC = ToilAndTroubleRegistries.RITUAL_TYPE.byNameCodec().dispatch(Ritual::getType, RitualType::getCodec);

    DataLookup<Ritual> DATA_LOOKUP = DataLookup.of();

    void apply(Level level, BlockPos blockPos);

    RitualType<?> getType();
}
