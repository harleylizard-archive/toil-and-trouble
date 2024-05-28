package com.harleylizard.trouble.common.block;

import com.harleylizard.trouble.common.Maths;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import com.mojang.math.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Stream;

public final class BellowsBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Maths.rotateShape(Axis.XP.rotation(90.0F), Stream.of(
            Block.box(1, 1, 13, 15, 15, 16),
            Block.box(2, 2, 5, 14, 14, 13),
            Block.box(1, 1, 2, 15, 15, 5),
            Block.box(6, 6, 0, 10, 10, 2)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get());

    private static final Map<Direction, VoxelShape> MAP = Map.of(
            Direction.NORTH, Maths.rotateShape(Direction.NORTH.getRotation(), SHAPE),
            Direction.EAST, Maths.rotateShape(Direction.EAST.getRotation(), SHAPE),
            Direction.WEST, Maths.rotateShape(Direction.WEST.getRotation(), SHAPE),
            Direction.SOUTH, Maths.rotateShape(Direction.SOUTH.getRotation(), SHAPE)
    );

    public BellowsBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ToilAndTroubleBlockEntityTypes.BELLOWS.create(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return MAP.get(getDirection(blockState));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public static Direction getDirection(BlockState blockState) {
        return blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
    }
}
