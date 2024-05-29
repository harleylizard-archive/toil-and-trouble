package com.harleylizard.trouble.common.block;

import com.harleylizard.trouble.common.registry.ToilAndTroubleEntityTypes;
import com.harleylizard.trouble.common.registry.ToilAndTroubleItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;

public final class MandrakeCropBlock extends CropBlock {

    public MandrakeCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ToilAndTroubleItems.MANDRAKE_SEEDS;
    }

    private boolean canSpawnMandrake(Level level, BlockState blockState) {
        return level.isDay() && isMaxAge(blockState) && level.getDifficulty() != Difficulty.PEACEFUL;
    }

    private void spawnMandrake(Level level, BlockPos blockPos) {
        var mandrake = ToilAndTroubleEntityTypes.MANDRAKE.create(level);
        if (mandrake != null) {
            var x = blockPos.getX() + 0.5D;
            var y = blockPos.getY();
            var z = blockPos.getZ() + 0.5D;
            mandrake.moveTo(x, y, z);
            level.addFreshEntity(mandrake);
            mandrake.spawnAnim();
        }
    }

    @Override
    public void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        if (canSpawnMandrake(serverLevel, blockState)) {
            spawnMandrake(serverLevel, blockPos);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        return canSpawnMandrake(builder.getLevel(), blockState) ? List.of() : super.getDrops(blockState, builder);
    }
}
