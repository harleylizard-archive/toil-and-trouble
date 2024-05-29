package com.harleylizard.trouble.common.registry;

import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.particle.BoilingBubblesParticleOptions;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ToilAndTroubleParticleTypes {
    public static final ParticleType<BoilingBubblesParticleOptions> BOILING_BUBBLES = FabricParticleTypes.complex(new BoilingBubblesParticleOptions.BoilingBubblesDeserializer());

    private ToilAndTroubleParticleTypes() {}

    public static void register() {
        register("boiling_bubbles", BOILING_BUBBLES);
    }

    private static <T extends ParticleOptions> void register(String name, ParticleType<T> type) {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, ToilAndTrouble.resourceLocation(name), type);
    }

    public static <T extends ParticleOptions> String toString(ParticleType<T> type) {
        return BuiltInRegistries.PARTICLE_TYPE.getKey(type).toString();
    }
}
