package com.harleylizard.trouble.common.brewing;

import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.registry.ToilAndTroubleHasIngredientListTypes;
import com.harleylizard.trouble.common.registry.ToilAndTroubleSounds;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class BrewingRecipe implements HasIngredientList {
    public static final Codec<BrewingRecipe> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                ItemStackInfo.CODEC.listOf().fieldOf("ingredients").forGetter(BrewingRecipe::getIngredientList),
                ItemStack.CODEC.fieldOf("result").forGetter(brewingRecipe -> brewingRecipe.itemStack)).apply(builder, BrewingRecipe::new);
    });

    private final List<ItemStackInfo> ingredientList;
    private final ItemStack itemStack;

    private BrewingRecipe(List<ItemStackInfo> ingredientList, ItemStack itemStack) {
        this.ingredientList = ingredientList;
        this.itemStack = itemStack;
    }

    @Override
    public void brew(BrewingCauldronBlockEntity blockEntity) {
        var ingredients = blockEntity.getIngredients();
        ingredients.consume(this);

        var level = blockEntity.getLevel();
        var blockPos = blockEntity.getBlockPos();
        var x = blockPos.getX() + 0.5D;
        var y = blockPos.getY() + 1.0D;
        var z = blockPos.getZ() + 0.5D;
        Containers.dropItemStack(level, x, y, z, itemStack.copy());

        level.playSound(null, blockPos, ToilAndTroubleSounds.SUMMON, SoundSource.BLOCKS, 1.0F, 1.0F);

        var fluidStorage = blockEntity.getFluidStorage();
        if (!fluidStorage.isResourceBlank()) {
            try (var transaction = Transaction.openOuter()) {
                fluidStorage.extract(fluidStorage.variant, FluidConstants.BUCKET, transaction);
                transaction.commit();
            }
        }
    }

    @Override
    public List<ItemStackInfo> getIngredientList() {
        return ingredientList;
    }

    @Override
    public HasIngredientListType<?> getType() {
        return ToilAndTroubleHasIngredientListTypes.BREWING_RECIPE;
    }
}
