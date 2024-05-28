package com.harleylizard.trouble.client.model;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.blockentity.BellowsBlockEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public final class BellowsModel extends GeoModel<BellowsBlockEntity> {
    private static final ResourceLocation MODEL = ToilAndTrouble.resourceLocation("geo/bellows.geo.json");
    private static final ResourceLocation TEXTURE = ToilAndTrouble.resourceLocation("textures/entity/bellows.png");
    private static final ResourceLocation ANIMATION = ToilAndTrouble.resourceLocation("animations/bellows.animation.json");

    @Override
    public ResourceLocation getModelResource(BellowsBlockEntity bellowsBlockEntity) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(BellowsBlockEntity bellowsBlockEntity) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(BellowsBlockEntity bellowsBlockEntity) {
        return ANIMATION;
    }
}
