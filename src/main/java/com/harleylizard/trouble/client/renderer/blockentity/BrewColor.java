package com.harleylizard.trouble.client.renderer.blockentity;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.brewing.HasIngredients;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;

import java.util.HashMap;
import java.util.Map;

public final class BrewColor {
    private static final Codec<BrewColor> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(Codec.INT.fieldOf("color").forGetter(brewColor -> brewColor.color)).apply(builder, BrewColor::new);
    });

    private static final Map<HasIngredients, BrewColor> MAP = new HashMap<>();

    public static final SimpleSynchronousResourceReloadListener RELOAD_LISTENER = new SimpleSynchronousResourceReloadListener() {
        @Override
        public ResourceLocation getFabricId() {
            return ToilAndTrouble.resourceLocation("brew_color");
        }

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            MAP.clear();
        }
    };

    private final int color;

    private BrewColor(int color) {
        this.color = color;
    }

    public static int getAsInt(int color, BrewingCauldronBlockEntity.Ingredients ingredients) {
        HasIngredients mostComplete = null;
        var progress = 0.0F;
        for (var ingredient : ingredients) {
            for (var hasIngredient : HasIngredients.getFrom(ingredient)) {
                var compared = hasIngredient.compareTo(ingredients);
                if (compared > progress) {
                    progress = compared;
                    mostComplete = hasIngredient;
                }
            }
        }
        return mostComplete == null ? color : lerp(color, getOrCreate(mostComplete).color, progress);
    }

    private static BrewColor getOrCreate(HasIngredients hasIngredients) {
        return MAP.computeIfAbsent(hasIngredients, BrewColor::createFallback);
    }

    private static BrewColor createFallback(HasIngredients hasIngredients) {
        var hashCode = HasIngredients.DEPRECATED_REGISTRY.get(hasIngredients).toString().hashCode();
        var r = (hashCode >> 16) & 0xFF;
        var g = (hashCode >> 8) & 0xFF;
        var b = (hashCode >> 0) & 0xFF;

        return new BrewColor(r << 16 | g << 8 | b | 0xFF << 24);
    }

    private static int lerp(int left, int right, double a) {
        var r = (left >> 16) & 0xFF;
        var g = (left >>  8) & 0xFF;
        var b = (left >>  0) & 0xFF;

        var j = (right >> 16) & 0xFF;
        var k = (right >>  8) & 0xFF;
        var l = (right >>  0) & 0xFF;

        var u = (int) Mth.lerp(a, r, j) & 0xFF;
        var v = (int) Mth.lerp(a, g, k) & 0xFF;
        var w = (int) Mth.lerp(a, b, l) & 0xFF;
        return u << 16 | v << 8 | w << 0 | 0xFF << 24;
    }
}
