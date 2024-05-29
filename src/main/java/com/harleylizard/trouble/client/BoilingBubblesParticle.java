package com.harleylizard.trouble.client;

import com.harleylizard.trouble.common.particle.BoilingBubblesParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public final class BoilingBubblesParticle extends RisingParticle {
    private final SpriteSet spriteSet;

    public BoilingBubblesParticle(SpriteSet spriteSet, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(clientLevel, d, e, f, g, h, i);
        this.spriteSet = spriteSet;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(spriteSet);

        var random = level.random;
        xd += (random.nextFloat() - random.nextFloat()) * 0.005F;
        yd += (random.nextFloat() - random.nextFloat()) * 0.005F;
        zd += (random.nextFloat() - random.nextFloat()) * 0.005F;
    }

    @Override
    public float getQuadSize(float f) {
        return (quadSize * (lifetime - age) / (lifetime * 0.75F)) * 0.25F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static final class BoilingBubblesParticleProvider implements ParticleProvider<BoilingBubblesParticleOptions> {
        private final SpriteSet spriteSet;

        public BoilingBubblesParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(BoilingBubblesParticleOptions particleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            var particle = new BoilingBubblesParticle(spriteSet, clientLevel, d, e, f, g, h, i);

            var color = particleOptions.getColor();
            var j = ((color >> 16) & 0xFF) / 255.0F;
            var k = ((color >> 8)  & 0xFF) / 255.0F;
            var l = ((color >> 0)  & 0xFF) / 255.0F;
            particle.setColor(j, k, l);
            particle.setSpriteFromAge(spriteSet);
            return particle;
        }
    }
}
