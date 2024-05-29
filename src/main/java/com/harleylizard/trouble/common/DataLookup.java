package com.harleylizard.trouble.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;

import java.util.Map;
import java.util.function.Function;

public final class DataLookup<T> {
    private final BiMap<ResourceLocation, T> biMap = HashBiMap.create();

    private DataLookup() {}

    public void register(Map.Entry<ResourceLocation, Resource> entry, T t) {
        var resourceLocation = entry.getKey();
        var path = resourceLocation.getPath();
        path = path.substring(0, path.indexOf(".json")).substring(path.lastIndexOf("/") + 1);

        biMap.put(new ResourceLocation(resourceLocation.getNamespace(), path), t);
    }

    public T getObject(ResourceLocation resourceLocation) {
        return biMap.get(resourceLocation);
    }

    public ResourceLocation getResourceLocation(T t) {
        return biMap.inverse().get(t);
    }

    public void clear() {
        biMap.clear();
    }

    public void registerListener(Function<DataLookup<T>, SimpleSynchronousResourceReloadListener> function) {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(function.apply(this));
    }

    public static <T> DataLookup<T> of() {
        return new DataLookup<>();
    }
}
