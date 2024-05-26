package com.harleylizard.trouble.common.blockentity;

import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public final class BrewingCauldronBlockEntity extends SyncedBlockEntity {
    private final Ingredients ingredients = new Ingredients();

    public BrewingCauldronBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ToilAndTroubleBlockEntityTypes.BREWING_CAULDRON, blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!ingredients.isEmpty()) {
            var ingredientsTag = new CompoundTag();
            ingredients.save(ingredientsTag);
            compoundTag.put("ingredients", ingredientsTag);
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("ingredients", Tag.TAG_COMPOUND)) {
            ingredients.load(compoundTag.getCompound("ingredients"));
        }
    }

    public Ingredients getIngredients() {
        return ingredients;
    }

    // Comments for TheRebelT
    public static final class Ingredients {
        private final List<ItemStack> list = new NonNullList<>(new ArrayList<>(), ItemStack.EMPTY);

        private Ingredients() {
        }

        private void save(CompoundTag compoundTag) {
            // Save the list of ItemStacks to NBT.
            var listTag = new ListTag();
            for (var itemStack : list) {
                var itemStackTag = new CompoundTag();
                itemStack.save(itemStackTag);
                listTag.add(itemStackTag);
            }
            compoundTag.put("items", listTag);
        }

        private void load(CompoundTag compoundTag) {
            // Loads the list of ItemStacks from NBT.
            list.clear();
            var listTag = compoundTag.getList("items", ListTag.TAG_COMPOUND);
            for (var itemStackTag : listTag) {
                list.add(ItemStack.of((CompoundTag) itemStackTag));
            }
        }

        public boolean addItem(ItemStack itemStack) {
            return list.add(itemStack);
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }
    }
}
