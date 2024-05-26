package com.harleylizard.trouble.common.blockentity;

import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class BrewingCauldronBlockEntity extends SyncedBlockEntity {

    public BrewingCauldronBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ToilAndTroubleBlockEntityTypes.BREWING_CAULDRON, blockPos, blockState);
    }
}
