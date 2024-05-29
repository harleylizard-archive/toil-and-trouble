package com.harleylizard.trouble.common.brewing;

import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public final class BrewingRecipe implements HasIngredients {
    public static final Codec<BrewingRecipe> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                        HasIngredients.codecIngredients().forGetter(brewingRecipe -> brewingRecipe.list),
                        ItemStack.CODEC.fieldOf("result").forGetter(brewingRecipe -> brewingRecipe.result))
                .apply(builder, BrewingRecipe::new);
    });

    private final List<ItemStack> list;
    private final ItemStack result;

    private BrewingRecipe(List<ItemStack> list, ItemStack result) {
        this.list = list;
        this.result = result;
    }

    @Override
    public void whenBrewed(BrewingCauldronBlockEntity blockEntity, BrewingCauldronBlockEntity.Ingredients ingredients) {
        ingredients.consume(this);

        var blockPos = blockEntity.getBlockPos();
        var x = blockPos.getX() + 0.5D;
        var y = blockPos.getY() + 1.0D;
        var z = blockPos.getZ() + 0.5D;
        Containers.dropItemStack(blockEntity.getLevel(), x, y, z, result.copy());

        var fluidStorage = blockEntity.getFluidStorage();
        if (!fluidStorage.isResourceBlank()) {
            try (var transaction = Transaction.openOuter()) {
                fluidStorage.extract(fluidStorage.variant, FluidConstants.BUCKET, transaction);
                transaction.commit();
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
