package com.harleylizard.trouble.common.blockentity;

import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class BellowsBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public BellowsBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ToilAndTroubleBlockEntityTypes.BELLOWS, blockPos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "idleController", 0, event -> {
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
