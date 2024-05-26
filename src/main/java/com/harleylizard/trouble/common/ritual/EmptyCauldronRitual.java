package com.harleylizard.trouble.common.ritual;

import com.harleylizard.trouble.common.registry.ToilAndTroubleRitualTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class EmptyCauldronRitual implements Ritual {
    public static final Codec<EmptyCauldronRitual> CODEC = Codec.unit(EmptyCauldronRitual::new);

    @Override
    public void apply(Level level, BlockPos blockPos) {
    }

    @Override
    public RitualType<?> getType() {
        return ToilAndTroubleRitualTypes.EMPTY_CAULDRON;
    }
}
