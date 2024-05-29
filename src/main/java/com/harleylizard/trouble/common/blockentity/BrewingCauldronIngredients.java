package com.harleylizard.trouble.common.blockentity;

import com.harleylizard.trouble.common.brewing.HasIngredientList;
import com.harleylizard.trouble.common.brewing.ItemStackInfo;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BrewingCauldronIngredients implements Iterable<ItemStack> {
    private final List<ItemStack> list = new NonNullList<>(new ArrayList<>(), ItemStack.EMPTY);

    public CompoundTag save() {
        var compoundTag = new CompoundTag();
        save(compoundTag);
        return compoundTag;
    }

    private void save(CompoundTag compoundTag) {
        // Save the list of ItemStacks to NBT.
        var listTag = new ListTag();
        for (var itemStack : list) {
            if (itemStack.isEmpty()) {
                continue;
            }
            var itemStackTag = new CompoundTag();
            itemStack.save(itemStackTag);
            listTag.add(itemStackTag);
        }
        compoundTag.put("items", listTag);
    }

    public void load(CompoundTag compoundTag) {
        // Loads the list of ItemStacks from NBT.
        list.clear();
        var listTag = compoundTag.getList("items", ListTag.TAG_COMPOUND);
        for (var itemStackTag : listTag) {
            list.add(ItemStack.of((CompoundTag) itemStackTag));
        }
    }

    public boolean add(ItemEntity itemEntity) {
        return add(itemEntity.getItem());
    }

    public boolean add(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        var found = findItemStack(itemStack.getItem());
        if (found.isEmpty()) {
            return list.add(itemStack.copyAndClear());
        }
        found.setCount(found.getCount() + itemStack.getCount());
        itemStack.setCount(0);
        return true;
    }

    private ItemStack findItemStack(Item item) {
        for (var itemStack : list) {
            if (itemStack.is(item) && itemStack.getCount() < itemStack.getMaxStackSize()) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public void clear() {
        list.clear();
    }

    public void consume(HasIngredientList hasIngredientList) {
        for (var itemStackInfo : hasIngredientList.getIngredientList()) {
            for (var ingredient : list) {
                if (itemStackInfo.test(ingredient)) {
                    ingredient.shrink(itemStackInfo.getCount());
                }
            }
        }
    }

    public boolean canBrew(HasIngredientList hasIngredientList) {
        var ingredientList = hasIngredientList.getIngredientList();
        var map = new Object2BooleanArrayMap<ItemStackInfo>(ingredientList.size());

        for (var itemStackInfo : hasIngredientList.getIngredientList()) {
            for (var ingredient : list) {
                if (itemStackInfo.test(ingredient)) {
                    map.put(itemStackInfo, true);
                }
            }
        }
        var values = map.values();
        var i = 0;
        for (var b : values) {
            if (b) {
                i++;
            }
        }
        return i == ingredientList.size();
    }

    public boolean isEmpty() {
        return list.isEmpty() || list.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public Iterator<ItemStack> iterator() {
        return list.iterator();
    }
}
