package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public final class ToilAndTroubleSounds {
    public static final SoundEvent SUMMON = soundEvent("summon");
    public static final SoundEvent WATER_SPLASH = soundEvent("water.splash");
    public static final SoundEvent WATER_BOILING = soundEvent("water.boiling");

    private ToilAndTroubleSounds() {}

    public static void register() {
        register("summon", SUMMON);
        register("water_splash", WATER_SPLASH);
        register("water_boiling", WATER_BOILING);
    }

    private static void register(String name, SoundEvent event) {
        Registry.register(BuiltInRegistries.SOUND_EVENT, ToilAndTrouble.resourceLocation(name), event);
    }

    private static SoundEvent soundEvent(String name) {
        return SoundEvent.createVariableRangeEvent(ToilAndTrouble.resourceLocation(name));
    }
}
