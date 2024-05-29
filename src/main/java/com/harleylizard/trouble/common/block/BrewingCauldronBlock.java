package com.harleylizard.trouble.common.block;

import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.brewing.BrewingRitual;
import com.harleylizard.trouble.common.brewing.HasIngredients;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import com.harleylizard.trouble.common.registry.ToilAndTroubleSounds;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public final class BrewingCauldronBlock extends Block implements BrewingCauldron {
    private static final VoxelShape SHAPE = Stream.of(
            Block.box(11, 0, 2, 13, 2, 3),
            Block.box(2, 2, 2, 14, 4, 14),
            Block.box(13, 0, 11, 14, 2, 14),
            Block.box(13, 0, 2, 14, 2, 5),
            Block.box(2, 0, 2, 3, 2, 5),
            Block.box(2, 0, 11, 3, 2, 14),
            Block.box(3, 0, 13, 5, 2, 14),
            Block.box(3, 0, 2, 5, 2, 3),
            Block.box(11, 0, 13, 13, 2, 14),
            Block.box(0, 4, 0, 16, 15, 2),
            Block.box(0, 4, 14, 16, 15, 16),
            Block.box(0, 4, 2, 2, 15, 14),
            Block.box(14, 4, 2, 16, 15, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static AABB AABB = Block.box(2, 4, 2, 14, 15, 14).bounds();

    public BrewingCauldronBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FLUID_TYPE, FluidType.EMPTY));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.getBlockEntity(blockPos) instanceof BrewingCauldronBlockEntity blockEntity) {
            var hasStorage = ContainerItemContext.forPlayerInteraction(player, interactionHand).find(FluidStorage.ITEM) != null;
            if (hasStorage) {
                if (!level.isClientSide) {
                    FluidStorageUtil.interactWithFluidStorage(blockEntity.getFluidStorage(), player, interactionHand);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (level.getBlockEntity(blockPos) instanceof BrewingCauldronBlockEntity blockEntity) {
            var ingredients = blockEntity.getIngredients();
            if (blockEntity.canBoil() && entity instanceof ItemEntity itemEntity && isIntersecting(blockPos, itemEntity) && ingredients.addItem(itemEntity)) {
                var x = itemEntity.getX();
                var y = itemEntity.getY();
                var z = itemEntity.getZ();

                level.playSound(null, x, y, z, ToilAndTroubleSounds.WATER_SPLASH, SoundSource.BLOCKS, 0.75F, level.random.nextFloat() + 1.0F);

                var hasIngredients = HasIngredients.getFrom(ingredients);
                if (hasIngredients != null) {
                    blockEntity.queue(hasIngredients);
                }
                blockEntity.sync();
            }
        }
        super.entityInside(blockState, level, blockPos, entity);
    }

    private boolean isIntersecting(BlockPos blockPos, ItemEntity itemEntity) {
        return itemEntity.getBoundingBox().intersects(getShape().move(blockPos));
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

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public AABB getShape() {
        return AABB;
    }
}
