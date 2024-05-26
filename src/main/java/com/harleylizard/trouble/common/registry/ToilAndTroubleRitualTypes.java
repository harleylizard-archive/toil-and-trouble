package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.ritual.EmptyCauldronRitual;
import com.harleylizard.trouble.common.ritual.RitualType;
import com.harleylizard.trouble.common.ritual.SummonRitual;
import net.minecraft.core.Registry;

public final class ToilAndTroubleRitualTypes {
    public static final RitualType<SummonRitual> SUMMON = new RitualType<>(SummonRitual.CODEC);
    public static final RitualType<EmptyCauldronRitual> EMPTY_CAULDRON = new RitualType<>(EmptyCauldronRitual.CODEC);

    private ToilAndTroubleRitualTypes() {}

    public static void register() {
        register("summon", SUMMON);
        register("empty_cauldron", EMPTY_CAULDRON);
    }

    private static void register(String name, RitualType<?> type) {
        Registry.register(ToilAndTroubleRegistries.RITUAL_TYPE, ToilAndTrouble.resourceLocation(name), type);
    }
}
