package com.harleylizard.trouble.common;

import com.harleylizard.trouble.common.registry.*;
import com.harleylizard.trouble.common.ritual.ConfiguredRitual;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public final class ToilAndTrouble implements ModInitializer {
    private static final String MOD_ID = "toil-and-trouble";

    @Override
    public void onInitialize() {
        ToilAndTroubleBlocks.register();
        ToilAndTroubleItems.register();
        ToilAndTroubleBlockEntityTypes.register();
        ToilAndTroubleEntityTypes.register();
        ToilAndTroubleSounds.register();

        ToilAndTroubleRitualTypes.register();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, resourceLocation("creative_tab"), FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup.toilAndTrouble"))
                        .displayItems((itemDisplayParameters, output) -> {
                            output.accept(ToilAndTroubleItems.BREWING_CAULDRON);
                        }).build());

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(ConfiguredRitual.RELOAD_LISTENER);
    }

    public static ResourceLocation resourceLocation(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
