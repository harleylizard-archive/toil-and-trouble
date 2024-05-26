package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public final class ToilAndTroubleSounds {

    private ToilAndTroubleSounds() {}

    public static void register() {
    }

    private static void register(String name, SoundEvent event) {
        Registry.register(BuiltInRegistries.SOUND_EVENT, ToilAndTrouble.resourceLocation(name), event);
    }

    private static SoundEvent soundEvent(String name) {
        return SoundEvent.createVariableRangeEvent(ToilAndTrouble.resourceLocation(name));
    }
}
