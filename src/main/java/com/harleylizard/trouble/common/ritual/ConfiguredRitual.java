package com.harleylizard.trouble.common.ritual;

import com.harleylizard.trouble.common.ToilAndTrouble;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public final class ConfiguredRitual {
    public static final SimpleSynchronousResourceReloadListener RELOAD_LISTENER = new SimpleSynchronousResourceReloadListener() {
        @Override
        public ResourceLocation getFabricId() {
            return ToilAndTrouble.resourceLocation("configured_ritual");
        }

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            for (var resourceEntry : resourceManager.listResources("configured_ritual", resourceLocation -> resourceLocation.getPath().endsWith(".json")).entrySet()) {

            }
        }
    };

    private ConfiguredRitual() {}
}
