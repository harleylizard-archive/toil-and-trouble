package com.harleylizard.trouble.common.brewing;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public final class ItemStackInfo implements Predicate<ItemStack> {
    public static final Codec<ItemStackInfo> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.either(ResourceKey.codec(Registries.ITEM), TagKey.hashedCodec(Registries.ITEM)).fieldOf("item").forGetter(itemStackInfo -> itemStackInfo.either),
                Codec.INT.optionalFieldOf("count", 1).forGetter(ItemStackInfo::getCount)).apply(builder, ItemStackInfo::new);
    });

    private final Either<ResourceKey<Item>, TagKey<Item>> either;
    private final int count;

    private ItemStackInfo(Either<ResourceKey<Item>, TagKey<Item>> either, int count) {
        this.either = either;
        this.count = count;
    }

    @Override
    public boolean test(ItemStack itemStack) {
        if (itemStack.getCount() <= count) {
            return false;
        }
        var resourceKey = either.left();
        if (resourceKey.isPresent()) {
            return itemStack.is(BuiltInRegistries.ITEM.getHolderOrThrow(resourceKey.get()));
        }
        return either.right().filter(itemStack::is).isPresent();
    }

    public List<Item> getItems() {
        var list = new ArrayList<Item>();

        var item = either.left();
        if (item.isPresent()) {
            list.add(BuiltInRegistries.ITEM.getHolderOrThrow(item.get()).value());
        }
        var tagKey = either.right();
        if (tagKey.isPresent()) {
            for (var itemHolder : BuiltInRegistries.ITEM.getTag(tagKey.get()).orElseThrow()) {
                list.add(itemHolder.value());
            }
        }
        return list.isEmpty() ? List.of() : Collections.unmodifiableList(list);
    }

    public int getCount() {
        return count;
    }
}
