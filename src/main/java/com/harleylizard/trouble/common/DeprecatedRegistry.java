package com.harleylizard.trouble.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.util.Map;

// TODO:: Remove
public final class DeprecatedRegistry<T> {
    private final BiMap<ResourceLocation, T> biMap = HashBiMap.create();

    public ResourceLocation get(T t) {
        return biMap.inverse().get(t);
    }

    public T get(ResourceLocation resourceLocation) {
        return biMap.get(resourceLocation);
    }

    public static <T> void register(DeprecatedRegistry<T> registry, Map.Entry<ResourceLocation, Resource> resourceEntry, T t) {
        var resourceLocation = resourceEntry.getKey();
        var path = resourceLocation.getPath();
        path = path.substring(0, path.indexOf(".json")).substring(path.lastIndexOf("/") + 1);

        registry.biMap.put(new ResourceLocation(resourceLocation.getNamespace(), path), t);
    }

    public static <T> void clear(DeprecatedRegistry<T> registry) {
        registry.biMap.clear();
    }
}
