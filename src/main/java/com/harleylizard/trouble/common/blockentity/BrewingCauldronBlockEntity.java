package com.harleylizard.trouble.common.blockentity;

import com.harleylizard.trouble.common.block.BellowsBlock;
import com.harleylizard.trouble.common.block.BrewingCauldron;
import com.harleylizard.trouble.common.brewing.HasIngredients;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlocks;
import com.harleylizard.trouble.common.registry.ToilAndTroubleSounds;
import com.harleylizard.trouble.common.tags.ToilAndTroubleBlockTags;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    private final Ingredients ingredients = new Ingredients();
    private final Queue<HasIngredients> queue = new LinkedList<>();

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
            for (var ritual : queue) {
                var tag = StringTag.valueOf(HasIngredients.DEPRECATED_REGISTRY.get(ritual).toString());
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
                queue.offer(HasIngredients.DEPRECATED_REGISTRY.get(new ResourceLocation(tag.getAsString())));
            }
        }
    }

    public void queue(HasIngredients hasIngredients) {
        queue.offer(hasIngredients);
    }

    public void poll() {
        if (!queue.isEmpty()) {
            var hasIngredients = queue.poll();
            if (!ingredients.canBrew(hasIngredients)) {
                return;
            }
            hasIngredients.whenBrewed(this, ingredients);
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

    public Ingredients getIngredients() {
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

    // Comments for TheRebelT
    public static final class Ingredients implements Iterable<ItemStack> {
        private final List<ItemStack> list = new NonNullList<>(new ArrayList<>(), ItemStack.EMPTY);

        private Ingredients() {}

        private CompoundTag save() {
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

        private void load(CompoundTag compoundTag) {
            // Loads the list of ItemStacks from NBT.
            list.clear();
            var listTag = compoundTag.getList("items", ListTag.TAG_COMPOUND);
            for (var itemStackTag : listTag) {
                list.add(ItemStack.of((CompoundTag) itemStackTag));
            }
        }

        public void consume(HasIngredients hasIngredients) {
            for (var ingredient : hasIngredients) {
                for (var itemStack : list) {
                    if (itemStack.is(ingredient.getItem())) {
                        itemStack.shrink(ingredient.getCount());
                    }
                }
            }
        }

        public boolean canBrew(HasIngredients hasIngredients) {
            var map = new Object2BooleanArrayMap<Item>();
            for (var ingredient : hasIngredients) {
                var item = ingredient.getItem();
                map.put(item, false);

                for (var thisIngredient : list) {
                    if (thisIngredient.is(item)) {
                        map.put(item, thisIngredient.getCount() >= ingredient.getCount());
                    }
                }
            }
            return allTrue(map);
        }

        public boolean addItem(ItemEntity itemEntity) {
            return addItem(itemEntity.getItem());
        }

        public boolean addItem(ItemStack itemStack) {
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

        private void clear() {
            list.clear();
        }

        public boolean isEmpty() {
            return list.isEmpty() || list.stream().allMatch(ItemStack::isEmpty);
        }

        @NotNull
        @Override
        public Iterator<ItemStack> iterator() {
            return list.iterator();
        }

        private static <T> boolean allTrue(Object2BooleanMap<T> map) {
            var values = map.values();
            var i = 0;
            for (var b : values) {
                if (b) {
                    i++;
                }
            }
            return i == values.size();
        }
    }
}
