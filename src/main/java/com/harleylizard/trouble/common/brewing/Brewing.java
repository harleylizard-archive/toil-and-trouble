package com.harleylizard.trouble.common.brewing;

import com.harleylizard.trouble.common.DeprecatedRegistry;
import com.harleylizard.trouble.common.ToilAndTrouble;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.Map;
import java.util.Set;

public final class Brewing implements SimpleSynchronousResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return ToilAndTrouble.resourceLocation("brewing");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        HasIngredients.MULTI_MAP.clear();
        DeprecatedRegistry.clear(HasIngredients.DEPRECATED_REGISTRY);

        reload(resourceManager, "brewing/ritual", BrewingRitual.CODEC);
        reload(resourceManager, "brewing/recipe", BrewingRecipe.CODEC);
    }

    private <T extends HasIngredients> void reload(ResourceManager manager, String path, Codec<T> codec) {
        for (var resourceEntry : listResource(manager, path)) {
            var parsed = ToilAndTrouble.parseJson(codec, resourceEntry.getValue());
            if (parsed.isPresent()) {
                var hasIngredients = parsed.get();
                for (var ingredient : hasIngredients) {
                    BrewingRitual.MULTI_MAP.put(ingredient.getItem(), hasIngredients);
                }
                DeprecatedRegistry.register(BrewingRitual.DEPRECATED_REGISTRY, resourceEntry, hasIngredients);
            }
        }
    }

    private Set<Map.Entry<ResourceLocation, Resource>> listResource(ResourceManager manager, String path) {
        return manager.listResources(path, resourceLocation -> resourceLocation.getPath().endsWith(".json")).entrySet();
    }
}
