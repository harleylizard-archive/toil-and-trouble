package com.harleylizard.trouble.client.renderer.blockentity;

import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class BrewColor {
    private static final Object2IntMap<Item> ITEM_TO_COLOR = new Object2IntArrayMap<>();

    private BrewColor() {}

    public static int getColor(int color, BrewingCauldronBlockEntity.Ingredients ingredients) {
        for (var ingredient : ingredients) {
            var weight = (1.0F / 24.0F) * ingredient.getCount();
            color = lerp(color, getColor(ingredient), weight);
        }
        return color;
    }

    public static int lerp(int left, int right, double a) {
        var r = (left >> 16) & 0xFF;
        var g = (left >>  8) & 0xFF;
        var b = (left >>  0) & 0xFF;

        var j = (right >> 16) & 0xFF;
        var k = (right >>  8) & 0xFF;
        var l = (right >>  0) & 0xFF;

        var u = (int) Mth.lerp(a, r, j);
        var v = (int) Mth.lerp(a, g, k);
        var w = (int) Mth.lerp(a, b, l);
        return  0xFF << 24 |
                (u & 0xFF) << 16 |
                (v & 0xFF) << 8 |
                (w & 0xFF) << 0;
    }

    private static int getColor(ItemStack itemStack) {
        return ITEM_TO_COLOR.computeIfAbsent(itemStack.getItem(), item -> getColor(BuiltInRegistries.ITEM.getKey((Item) item))) | 0xFF << 24;
    }

    private static int getColor(ResourceLocation resourceLocation) {
        try {
            var bytes = MessageDigest.getInstance("SHA-256").digest(resourceLocation.toString().getBytes("UTF-8"));
            return ((bytes[0] & 0xFF) << 16) | ((bytes[1] & 0xFF) << 8) | (bytes[2] & 0xFF) & 0xFFFFFF;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return 0;
        }
    }
}
