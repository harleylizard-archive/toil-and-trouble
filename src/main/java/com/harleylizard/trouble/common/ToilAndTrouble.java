package com.harleylizard.trouble.common;

import com.harleylizard.trouble.common.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public final class ToilAndTrouble implements ModInitializer {
    private static final String MOD_ID = "toil-and-trouble";

    @Override
    public void onInitialize() {
        ToilAndTroubleBlocks.register();
        ToilAndTroubleItems.register();
        ToilAndTroubleBlockEntityTypes.register();
        ToilAndTroubleEntityTypes.register();
        ToilAndTroubleSounds.register();
    }

    public static ResourceLocation resourceLocation(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
