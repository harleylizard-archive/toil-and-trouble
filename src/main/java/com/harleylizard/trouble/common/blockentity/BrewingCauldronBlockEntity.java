package com.harleylizard.trouble.common.blockentity;

import com.harleylizard.trouble.common.block.BellowsBlock;
import com.harleylizard.trouble.common.block.BrewingCauldron;
import com.harleylizard.trouble.common.brewing.HasIngredientList;
import com.harleylizard.trouble.common.brewing.ItemLookup;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlocks;
import com.harleylizard.trouble.common.registry.ToilAndTroubleSounds;
import com.harleylizard.trouble.common.tags.ToilAndTroubleBlockTags;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import java.util.LinkedList;
import java.util.Queue;

public final class BrewingCauldronBlockEntity extends SyncedBlockEntity {
    private final SingleFluidStorage fluidStorage = new SingleFluidStorage() {
        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidConstants.BUCKET * 3;
        }

        @Override
        protected void onFinalCommit() {
            if (isResourceBlank()) ingredients.clear();

            level.setBlock(getBlockPos(), BrewingCauldron.setFluidType(getBlockState(), variant), Block.UPDATE_ALL);
            sync();
        }

        @Override
        protected boolean canInsert(FluidVariant variant) {
            return ingredients.isEmpty();
        }
    };

    private final BrewingCauldronIngredients ingredients = new BrewingCauldronIngredients();
    private final Queue<HasIngredientList> queue = new LinkedList<>();

    private int ticks;
    private int heat;
    private int delay;

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
        compoundTag.putInt("heat", heat);
        compoundTag.putInt("delay", delay);

        if (!queue.isEmpty()) {
            var listTag = new ListTag();
            for (var hasIngredientList : queue) {
                var key = HasIngredientList.DATA_LOOKUP.getResourceLocation(hasIngredientList).toString();
                var tag = StringTag.valueOf(key);
                listTag.add(tag);
            }
            compoundTag.put("queue", listTag);
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        ingredients.clear();
        if (compoundTag.contains("ingredients", Tag.TAG_COMPOUND)) {
            ingredients.load(compoundTag.getCompound("ingredients"));
        }
        fluidStorage.readNbt(compoundTag);

        ticks = compoundTag.getInt("ticks");
        heat = compoundTag.getInt("heat");
        delay = compoundTag.getInt("delay");

        queue.clear();
        if (compoundTag.contains("queue", Tag.TAG_LIST)) {
            var listTag = compoundTag.getList("queue", Tag.TAG_STRING);
            for (var tag : listTag) {
                var hasIngredientList = HasIngredientList.DATA_LOOKUP.getObject(new ResourceLocation(tag.getAsString()));
                queue.offer(hasIngredientList);
            }
        }
    }

    public void queue(HasIngredientList hasIngredientList) {
        queue.offer(hasIngredientList);
    }

    public void poll() {
        if (!queue.isEmpty()) {
            var hasIngredients = queue.poll();
            if (ingredients.canBrew(hasIngredients)) {
                hasIngredients.brew(this);
                if (!ingredients.isEmpty()) {
                    var next = ItemLookup.getAll(ingredients);
                    if (next != null) {
                        queue(next);
                    }
                }
            }
        }
    }

    public void clear() {
        if (!fluidStorage.isResourceBlank()) {
            ingredients.clear();
            try (var transaction = Transaction.openOuter()) {
                fluidStorage.extract(fluidStorage.variant, fluidStorage.amount, transaction);
                transaction.commit();
            }
        }
    }

    public BrewingCauldronIngredients getIngredients() {
        return ingredients;
    }

    public SingleFluidStorage getFluidStorage() {
        return fluidStorage;
    }

    public boolean canBoil() {
        return heat >= calculateTimeReduction() && fluidStorage.variant.isOf(Fluids.WATER) && fluidStorage.amount >= FluidConstants.BUCKET;
    }

    private int calculateTimeReduction() {
        var i = 50;
        for (var direction : Direction.Plane.HORIZONTAL) {
            var blockState = level.getBlockState(getBlockPos().relative(direction));
            if (blockState.is(ToilAndTroubleBlocks.BELLOWS) && BellowsBlock.getDirection(blockState) == direction.getOpposite()) {
                i -= 11;
            }
        }
        return i;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BrewingCauldronBlockEntity blockEntity) {
        if (!level.isClientSide) {
            var hasFluid = blockEntity.fluidStorage.variant.isOf(Fluids.WATER);
            if (!hasFluid) {
                blockEntity.heat = 0;
            }
            if (blockEntity.delay >= 3) {
                blockEntity.delay = 0;
                blockEntity.poll();
                blockEntity.sync();
            }
            var ticks = blockEntity.ticks;
            if (ticks % 20 == 0) {
                if (!blockEntity.queue.isEmpty()) {
                    blockEntity.delay++;
                }
                if (hasFluid) {
                    var heat = blockEntity.heat;
                    if (level.getBlockState(blockPos.below()).is(ToilAndTroubleBlockTags.HEAT_SOURCE)) {
                        if (heat < 75) {
                            blockEntity.heat++;
                        }
                    } else if (heat > 0) {
                        blockEntity.heat = Math.max(heat - 4, 0);
                    }
                }
                blockEntity.sync();
            }
            if (ticks % 50 == 0 && blockEntity.canBoil()) {
                level.playSound(null, blockPos, ToilAndTroubleSounds.WATER_BOILING, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() + 0.75F);
            }
            blockEntity.ticks = (ticks + 1) % (20 * 3);
        }
    }
}
