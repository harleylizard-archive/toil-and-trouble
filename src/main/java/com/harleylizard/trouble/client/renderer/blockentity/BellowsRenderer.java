package com.harleylizard.trouble.client.renderer.blockentity;

import com.harleylizard.trouble.client.model.BellowsModel;
import com.harleylizard.trouble.common.blockentity.BellowsBlockEntity;
import mod.azure.azurelib.renderer.GeoBlockRenderer;

public final class BellowsRenderer extends GeoBlockRenderer<BellowsBlockEntity> {
    public BellowsRenderer() {
        super(new BellowsModel());
    }
}
