package com.harleylizard.trouble.client;

import com.harleylizard.trouble.client.renderer.blockentity.BellowsRenderer;
import com.harleylizard.trouble.client.renderer.blockentity.BrewingCauldronRenderer;
import com.harleylizard.trouble.client.renderer.entity.MandrakeRenderer;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlockEntityTypes;
import com.harleylizard.trouble.common.registry.ToilAndTroubleBlocks;
import com.harleylizard.trouble.common.registry.ToilAndTroubleEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;

public final class ToilAndTroubleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                ToilAndTroubleBlocks.BREWING_CAULDRON,
                ToilAndTroubleBlocks.COTTON,
                ToilAndTroubleBlocks.MANDRAKE);

        BlockEntityRendererRegistry.register(ToilAndTroubleBlockEntityTypes.BELLOWS, context -> new BellowsRenderer());
        BlockEntityRendererRegistry.register(ToilAndTroubleBlockEntityTypes.BREWING_CAULDRON, context -> new BrewingCauldronRenderer());

        EntityRendererRegistry.register(ToilAndTroubleEntityTypes.MANDRAKE, MandrakeRenderer::new);
    }
}
