package com.harleylizard.trouble.common.ritual;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.registry.ToilAndTroubleRegistries;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.HashMap;
import java.util.Map;

public final class ConfiguredRitual {
    private static final Map<ResourceLocation, Ritual> MAP = new HashMap<>();

    private static final Codec<Ritual> CODEC = ToilAndTroubleRegistries.RITUAL_TYPE.byNameCodec().dispatch(Ritual::getType, RitualType::getCodec);

    public static final SimpleSynchronousResourceReloadListener RELOAD_LISTENER = new SimpleSynchronousResourceReloadListener() {
        @Override
        public ResourceLocation getFabricId() {
            return ToilAndTrouble.resourceLocation("configured_ritual");
        }

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            MAP.clear();
            for (var resourceEntry : resourceManager.listResources("configured_ritual", resourceLocation -> resourceLocation.getPath().endsWith(".json")).entrySet()) {
                var parsed = ToilAndTrouble.parseJson(CODEC, resourceEntry.getValue());
                if (parsed.isPresent()) {
                    var key = resourceEntry.getKey();
                    var path = key.getPath();
                    path = path.substring(0, path.indexOf(".json")).substring(path.lastIndexOf("/") + 1);

                    MAP.put(new ResourceLocation(key.getNamespace(), path), parsed.get());
                }
            }
        }
    };

    private ConfiguredRitual() {}

    public static Ritual getRitual(ResourceLocation resourceLocation) {
        return MAP.get(resourceLocation);
    }
}
