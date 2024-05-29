package com.harleylizard.trouble.client.model;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.entity.MandrakeEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public final class MandrakeModel extends GeoModel<MandrakeEntity> {
    private static final ResourceLocation MODEL = ToilAndTrouble.resourceLocation("geo/mandrake.geo.json");
    private static final ResourceLocation TEXTURE = ToilAndTrouble.resourceLocation("textures/entity/mandrake.png");
    private static final ResourceLocation ANIMATION = ToilAndTrouble.resourceLocation("animations/mandrake.animation.json");

    @Override
    public ResourceLocation getModelResource(MandrakeEntity mandrakeEntity) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(MandrakeEntity mandrakeEntity) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(MandrakeEntity mandrakeEntity) {
        return ANIMATION;
    }
}
