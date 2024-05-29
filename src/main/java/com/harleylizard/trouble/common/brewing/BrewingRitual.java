package com.harleylizard.trouble.common.brewing;

import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.registry.ToilAndTroubleHasIngredientListTypes;
import com.harleylizard.trouble.common.ritual.Ritual;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class BrewingRitual implements HasIngredientList {
    public static final Codec<BrewingRitual> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                ItemStackInfo.CODEC.listOf().fieldOf("ingredients").forGetter(BrewingRitual::getIngredientList),
                ResourceLocation.CODEC.fieldOf("configured-ritual").forGetter(brewingRitual -> brewingRitual.resourceLocation)).apply(builder, BrewingRitual::new);
    });

    private final List<ItemStackInfo> ingredientList;
    private final ResourceLocation resourceLocation;

    private BrewingRitual(List<ItemStackInfo> ingredientList, ResourceLocation resourceLocation) {
        this.ingredientList = ingredientList;
        this.resourceLocation = resourceLocation;
    }

    @Override
    public void brew(BrewingCauldronBlockEntity blockEntity) {
        var ritual  = Ritual.DATA_LOOKUP.getObject(resourceLocation);
        if (ritual != null) {
            blockEntity.getIngredients().consume(this);

            var level = blockEntity.getLevel();
            var blockPos = blockEntity.getBlockPos();
            ritual.apply(level, blockPos);

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
    public List<ItemStackInfo> getIngredientList() {
        return ingredientList;
    }

    @Override
    public HasIngredientListType<?> getType() {
        return ToilAndTroubleHasIngredientListTypes.BREWING_RITUAL;
    }
}
