package com.harleylizard.trouble.common.ritual;

import com.harleylizard.trouble.common.registry.ToilAndTroubleRitualTypes;
import com.harleylizard.trouble.common.registry.ToilAndTroubleSounds;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public final class SummonRitual implements Ritual {
    public static final Codec<SummonRitual> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(ResourceLocation.CODEC.fieldOf("entity-type").forGetter(summonRitual -> summonRitual.resourceLocation)).apply(builder, SummonRitual::new);
    });

    public final ResourceLocation resourceLocation;

    private SummonRitual(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    @Override
    public void apply(Level level, BlockPos blockPos) {
        if (!level.isClientSide) {
            var entity = BuiltInRegistries.ENTITY_TYPE.get(resourceLocation).create(level);
            if (entity == null) {
                return;
            }

            var x = blockPos.getX() + 0.5D;
            var y = blockPos.getY();
            var z = blockPos.getZ() + 0.5D;
            entity.setPos(x, y, z);
            level.addFreshEntity(entity);

            level.playSound(null, blockPos, ToilAndTroubleSounds.SUMMON, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public RitualType<?> getType() {
        return ToilAndTroubleRitualTypes.SUMMON;
    }
}
