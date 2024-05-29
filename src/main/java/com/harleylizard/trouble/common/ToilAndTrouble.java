package com.harleylizard.trouble.common;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.harleylizard.trouble.common.brewing.BrewingRecipe;
import com.harleylizard.trouble.common.brewing.BrewingRitual;
import com.harleylizard.trouble.common.brewing.HasIngredientList;
import com.harleylizard.trouble.common.brewing.ItemLookup;
import com.harleylizard.trouble.common.entity.MandrakeEntity;
import com.harleylizard.trouble.common.registry.*;
import com.harleylizard.trouble.common.ritual.Ritual;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.util.Optional;

public final class ToilAndTrouble implements ModInitializer {
    private static final String MOD_ID = "toil-and-trouble";

    @Override
    public void onInitialize() {
        ToilAndTroubleBlocks.register();
        ToilAndTroubleItems.register();
        ToilAndTroubleBlockEntityTypes.register();
        ToilAndTroubleEntityTypes.register();
        ToilAndTroubleSounds.register();
        ToilAndTroubleParticleTypes.register();
        ToilAndTroubleRitualTypes.register();

        FabricDefaultAttributeRegistry.register(ToilAndTroubleEntityTypes.MANDRAKE, MandrakeEntity.createAttributes());

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, resourceLocation("creative_tab"), FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup.toilAndTrouble"))
                        .displayItems((itemDisplayParameters, output) -> {
                            output.accept(ToilAndTroubleItems.BELLOWS);
                            output.accept(ToilAndTroubleItems.BREWING_CAULDRON);
                            output.accept(ToilAndTroubleItems.COTTON_SEEDS);
                            output.accept(ToilAndTroubleItems.MANDRAKE_SEEDS);
                            output.accept(ToilAndTroubleItems.MANDRAKE_SPAWN_EGG);
                        }).build());

        Ritual.DATA_LOOKUP.registerListener(dataLookup -> new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return ToilAndTrouble.resourceLocation("configured_ritual");
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                dataLookup.clear();
                for (var entry : resourceManager.listResources("configured_ritual", resourceLocation -> resourceLocation.getPath().endsWith(".json")).entrySet()) {
                    ToilAndTrouble.parseJson(Ritual.CODEC, entry.getValue()).ifPresent(ritual -> dataLookup.register(entry, ritual));
                }
            }
        });

        HasIngredientList.DATA_LOOKUP.registerListener(dataLookup -> new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return ToilAndTrouble.resourceLocation("has_ingredient_list");
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                dataLookup.clear();
                ItemLookup.clear();
                for (var entry : resourceManager.listResources("brewing/ritual", resourceLocation -> resourceLocation.getPath().endsWith(".json")).entrySet()) {
                    var optional = ToilAndTrouble.parseJson(BrewingRitual.CODEC, entry.getValue());
                    if (optional.isPresent()) {
                        var brewingRitual = optional.get();
                        ItemLookup.put(brewingRitual);
                        dataLookup.register(entry, brewingRitual);
                    }
                }
                for (var entry : resourceManager.listResources("brewing/recipe", resourceLocation -> resourceLocation.getPath().endsWith(".json")).entrySet()) {
                    var optional = ToilAndTrouble.parseJson(BrewingRecipe.CODEC, entry.getValue());
                    if (optional.isPresent()) {
                        var brewingRecipe = optional.get();
                        ItemLookup.put(brewingRecipe);
                        dataLookup.register(entry, brewingRecipe);
                    }
                }
            }
        });
    }

    public static ResourceLocation resourceLocation(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static <T> Optional<T> parseJson(Codec<T> codec, Resource resource) {
        try (var reader = resource.openAsReader()) {
            var gson = new GsonBuilder().create();
            return codec.parse(JsonOps.INSTANCE, gson.fromJson(reader, JsonElement.class)).result();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
