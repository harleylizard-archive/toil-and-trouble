package com.harleylizard.trouble.client.renderer.entity;

import com.harleylizard.trouble.client.model.MandrakeModel;
import com.harleylizard.trouble.common.entity.MandrakeEntity;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public final class MandrakeRenderer extends GeoEntityRenderer<MandrakeEntity> {

    public MandrakeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MandrakeModel());
    }
}
