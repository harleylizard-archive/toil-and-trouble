package com.harleylizard.trouble.common.block;

import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class BrewingCauldronBlock extends Block implements BrewingCauldron {

    public BrewingCauldronBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ToilAndTroubleBlockEntityTypes.BREWING_CAULDRON.create(blockPos, blockState);
    }
}
