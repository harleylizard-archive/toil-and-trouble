package com.harleylizard.trouble.common.particle;

import com.harleylizard.trouble.common.registry.ToilAndTroubleParticleTypes;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public final class BoilingBubblesParticleOptions implements ParticleOptions {
    private final int color;

    public BoilingBubblesParticleOptions(int color) {
        this.color = color;
    }

    @Override
    public ParticleType<?> getType() {
        return ToilAndTroubleParticleTypes.BOILING_BUBBLES;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(color);
    }

    @Override
    public String writeToString() {
        return ToilAndTroubleParticleTypes.toString(ToilAndTroubleParticleTypes.BOILING_BUBBLES);
    }

    public int getColor() {
        return color;
    }

    public static final class BoilingBubblesDeserializer implements Deserializer<BoilingBubblesParticleOptions> {

        @Override
        public BoilingBubblesParticleOptions fromCommand(ParticleType<BoilingBubblesParticleOptions> particleType, StringReader stringReader) throws CommandSyntaxException {
            return new BoilingBubblesParticleOptions(0xFFFFFFFF);
        }

        @Override
        public BoilingBubblesParticleOptions fromNetwork(ParticleType<BoilingBubblesParticleOptions> particleType, FriendlyByteBuf friendlyByteBuf) {
            return new BoilingBubblesParticleOptions(friendlyByteBuf.readInt());
        }
    }
}
