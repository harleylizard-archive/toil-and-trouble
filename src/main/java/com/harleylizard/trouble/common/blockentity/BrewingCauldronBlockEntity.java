package com.harleylizard.trouble.common.blockentity;

import com.harleylizard.trouble.common.block.BrewingCauldron;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import com.harleylizard.trouble.common.registry.ToilAndTroubleSounds;
import com.harleylizard.trouble.common.tags.ToilAndTroubleBlockTags;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

public final class BrewingCauldronBlockEntity extends SyncedBlockEntity {
    private final SingleFluidStorage fluidStorage = new SingleFluidStorage() {
        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidConstants.BUCKET * 3;
        }

        @Override
        protected void onFinalCommit() {
            level.setBlock(getBlockPos(), BrewingCauldron.setFluidType(getBlockState(), variant), Block.UPDATE_ALL);
            sync();
        }
    };

    private final Ingredients ingredients = new Ingredients();

    private int ticks;
    private boolean heated;

    public BrewingCauldronBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ToilAndTroubleBlockEntityTypes.BREWING_CAULDRON, blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!ingredients.isEmpty()) {
            compoundTag.put("ingredients", ingredients.save());
        }
        fluidStorage.writeNbt(compoundTag);

        compoundTag.putInt("ticks", ticks);
        compoundTag.putBoolean("heated", heated);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("ingredients", Tag.TAG_COMPOUND)) {
            ingredients.load(compoundTag.getCompound("ingredients"));
        }
        fluidStorage.readNbt(compoundTag);

        ticks = compoundTag.getInt("ticks");
        heated = compoundTag.getBoolean("heated");
    }

    public Ingredients getIngredients() {
        return ingredients;
    }

    public SingleFluidStorage getFluidStorage() {
        return fluidStorage;
    }

    public boolean canBoil() {
        return heated && fluidStorage.variant.isOf(Fluids.WATER);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BrewingCauldronBlockEntity blockEntity) {
        if (!level.isClientSide) {
            var ticks = blockEntity.ticks;
            if (ticks % 20 == 0) {
                blockEntity.heated = level.getBlockState(blockPos.below()).is(ToilAndTroubleBlockTags.HEAT_SOURCE);
            }
            if (ticks % 50 == 0 && blockEntity.canBoil()) {
                level.playSound(null, blockPos, ToilAndTroubleSounds.WATER_BOILING, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() + 0.75F);
            }
            blockEntity.ticks = (ticks + 1) % (20 * 3);
        }
    }

    // Comments for TheRebelT
    public static final class Ingredients {
        private final List<ItemStack> list = new NonNullList<>(new ArrayList<>(), ItemStack.EMPTY);

        private Ingredients() {
        }

        private CompoundTag save() {
            var compoundTag = new CompoundTag();
            save(compoundTag);
            return compoundTag;
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

        public boolean addItem(ItemEntity itemEntity) {
            return addItem(itemEntity.getItem());
        }

        public boolean addItem(ItemStack itemStack) {
            return !itemStack.isEmpty() && list.add(itemStack.copyAndClear());
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }
    }
}
