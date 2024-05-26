package com.harleylizard.trouble.common.ritual;

import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.registry.ToilAndTroubleRitualTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public final class EmptyCauldronRitual implements Ritual {
    public static final Codec<EmptyCauldronRitual> CODEC = Codec.unit(EmptyCauldronRitual::new);

    @Override
    public void apply(Level level, BlockPos blockPos) {
        if (!level.isClientSide && level.getBlockEntity(blockPos) instanceof BrewingCauldronBlockEntity blockEntity) {
            blockEntity.clear();
            blockEntity.sync();

            level.playSound(null, blockPos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public RitualType<?> getType() {
        return ToilAndTroubleRitualTypes.EMPTY_CAULDRON;
    }
}
