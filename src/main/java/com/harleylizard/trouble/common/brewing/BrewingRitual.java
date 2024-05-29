package com.harleylizard.trouble.common.brewing;

import com.harleylizard.trouble.common.DeprecatedRegistry;
import com.harleylizard.trouble.common.MultiMap;
import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.ritual.ConfiguredRitual;
import com.harleylizard.trouble.common.ritual.Ritual;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public final class BrewingRitual implements HasIngredients {
    public static final DeprecatedRegistry<BrewingRitual> REGISTRY = new DeprecatedRegistry<>();

    private static final MultiMap<Item, BrewingRitual> MULTI_MAP = MultiMap.mutableOf();

    public static final Codec<BrewingRitual> CODEC = RecordCodecBuilder.create(builder -> builder.group(ItemStack.CODEC.listOf().fieldOf("ingredients").forGetter(BrewingRitual::getIngredients), ResourceLocation.CODEC.fieldOf("configured-ritual").forGetter(brewingRitual -> brewingRitual.configuredRitual)).apply(builder, BrewingRitual::new));

    public static final SimpleSynchronousResourceReloadListener RELOAD_LISTENER = new SimpleSynchronousResourceReloadListener() {
        @Override
        public ResourceLocation getFabricId() {
            return ToilAndTrouble.resourceLocation("brewing_ritual");
        }

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            MULTI_MAP.clear();
            DeprecatedRegistry.clear(REGISTRY);
            for (var resourceEntry : resourceManager.listResources("brewing/ritual", resourceLocation -> resourceLocation.getPath().endsWith(".json")).entrySet()) {
                var parsed = ToilAndTrouble.parseJson(CODEC, resourceEntry.getValue());
                if (parsed.isPresent()) {
                    var brewingRitual = parsed.get();
                    for (var ingredient : brewingRitual.ingredients) {
                        MULTI_MAP.put(ingredient.getItem(), brewingRitual);
                    }
                    DeprecatedRegistry.register(REGISTRY, resourceEntry, brewingRitual);
                }
            }
        }
    };

    private final List<ItemStack> ingredients;
    private final ResourceLocation configuredRitual;

    private BrewingRitual(List<ItemStack> ingredients, ResourceLocation configuredRitual) {
        this.ingredients = ingredients;
        this.configuredRitual = configuredRitual;
    }

    public Ritual getRitual() {
        return ConfiguredRitual.getRitual(configuredRitual);
    }

    public float compare(BrewingCauldronBlockEntity.Ingredients ingredients) {
        var i = 0;
        for (var left : ingredients) {
            for (var right : this.ingredients) {
                if (left.is(right.getItem()) && left.getCount() >= right.getCount()) {
                    i++;
                }
            }
        }
        return (float) i / (float) this.ingredients.size();
    }

    @Override
    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    @NotNull
    @Override
    public Iterator<ItemStack> iterator() {
        return ingredients.iterator();
    }

    public static BrewingRitual getRitual(BrewingCauldronBlockEntity.Ingredients ingredients) {
        for (var ingredient : ingredients) {
            for (var ritual : MULTI_MAP.get(ingredient.getItem())) {
                if (ingredients.canBrewRitual(ritual)) {
                    return ritual;
                }
            }
        }
        return null;
    }

    public static List<BrewingRitual> getRitual(Item item) {
        return MULTI_MAP.get(item);
    }
}
