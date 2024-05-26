package com.harleylizard.trouble.common.block;

import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public final class BrewingCauldronBlock extends Block implements BrewingCauldron {

    public BrewingCauldronBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FLUID_TYPE, FluidType.EMPTY));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.getBlockEntity(blockPos) instanceof BrewingCauldronBlockEntity blockEntity) {
            var itemStack = player.getItemInHand(interactionHand);

            var fluidStorage = blockEntity.getFluidStorage();
            if ((!fluidStorage.isResourceBlank() && itemStack.is(Items.BUCKET)) || isHoldingBucket(fluidStorage.variant, itemStack)) {
                if (!level.isClientSide) {
                    FluidStorageUtil.interactWithFluidStorage(fluidStorage, player, interactionHand);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private boolean isHoldingBucket(FluidVariant variant, ItemStack itemStack) {
        return variant.isBlank() ? itemStack.is(Items.WATER_BUCKET) || itemStack.is(Items.LAVA_BUCKET) : itemStack.is(variant.getFluid().getBucket());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FLUID_TYPE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ToilAndTroubleBlockEntityTypes.BREWING_CAULDRON.create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return BaseEntityBlock.createTickerHelper(blockEntityType, ToilAndTroubleBlockEntityTypes.BREWING_CAULDRON, BrewingCauldronBlockEntity::tick);
    }
}
