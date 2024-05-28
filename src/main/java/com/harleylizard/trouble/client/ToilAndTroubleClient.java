package com.harleylizard.trouble.client;

import com.harleylizard.trouble.client.renderer.BellowsRenderer;
import com.harleylizard.trouble.client.renderer.BrewingCauldronRenderer;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;

public final class ToilAndTroubleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ToilAndTroubleBlocks.BREWING_CAULDRON, RenderType.cutout());

        BlockEntityRendererRegistry.register(ToilAndTroubleBlockEntityTypes.BELLOWS, context -> new BellowsRenderer());
        BlockEntityRendererRegistry.register(ToilAndTroubleBlockEntityTypes.BREWING_CAULDRON, context -> new BrewingCauldronRenderer());
    }
}
