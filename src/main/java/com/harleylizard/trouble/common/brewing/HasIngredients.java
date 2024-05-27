package com.harleylizard.trouble.common.brewing;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public sealed interface HasIngredients extends Iterable<ItemStack> permits BrewingRitual {

    List<ItemStack> getIngredients();
}
