package com.harleylizard.trouble.common.brewing;

import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.ritual.ConfiguredRitual;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public final class BrewingRitual implements HasIngredients {
    public static final Codec<BrewingRitual> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                HasIngredients.codecIngredients().forGetter(brewingRitual -> brewingRitual.list),
                ResourceLocation.CODEC.fieldOf("configured-ritual").forGetter(brewingRitual -> brewingRitual.configuredRitual))
                .apply(builder, BrewingRitual::new);
    });

    private final List<ItemStack> list;
    private final ResourceLocation configuredRitual;

    private BrewingRitual(List<ItemStack> list, ResourceLocation configuredRitual) {
        this.list = list;
        this.configuredRitual = configuredRitual;
    }

    @Override
    public void whenBrewed(BrewingCauldronBlockEntity blockEntity, BrewingCauldronBlockEntity.Ingredients ingredients) {
        if (configuredRitual != null) {
            ingredients.consume(this);

            ConfiguredRitual.getRitual(configuredRitual).apply(blockEntity.getLevel(), blockEntity.getBlockPos());

            var fluidStorage = blockEntity.getFluidStorage();
            if (!fluidStorage.isResourceBlank()) {
                try (var transaction = Transaction.openOuter()) {
                    fluidStorage.extract(fluidStorage.variant, FluidConstants.BUCKET, transaction);
                    transaction.commit();
                }
            }
        }
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @NotNull
    @Override
    public Iterator<ItemStack> iterator() {
        return list.iterator();
    }
}
