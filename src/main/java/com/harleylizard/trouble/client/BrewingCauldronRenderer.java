package com.harleylizard.trouble.client;

import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.material.Fluids;

public final class BrewingCauldronRenderer implements BlockEntityRenderer<BrewingCauldronBlockEntity> {
    @Override
    public void render(BrewingCauldronBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        var fluidStorage = blockEntity.getFluidStorage();
        if (fluidStorage.isResourceBlank()) {
            return;
        }
        poseStack.pushPose();
        var last = poseStack.last();

        var toX = 0.0F;
        var toZ = 0.0F;
        var fromX = 1.0F;
        var fromZ = 1.0F;

        var percentage = (float) fluidStorage.amount / (float) fluidStorage.getCapacity();
        var height = percentage * (10.0F / 16.0F) + (4.0F / 16.0F);

        var variant = fluidStorage.variant;
        var texture = FluidVariantRendering.getSprite(variant);
        var u0 = texture.getU0();
        var u1 = texture.getU1();
        var v0 = texture.getV0();
        var v1 = texture.getV1();

        var ingredients = blockEntity.getIngredients();
        var color = FluidVariantRendering.getColor(variant, blockEntity.getLevel(), blockEntity.getBlockPos());
        if (!ingredients.isEmpty()) {
            color = BrewColor.getColor(color, ingredients);
        }
        if (blockEntity.canBoil()) {
            var level = blockEntity.getLevel();
            var random = level.random;
            var blockPos = blockEntity.getBlockPos();
            var x = (double) blockPos.getX() + ((random.nextDouble() - random.nextDouble()) * 0.375D) + 0.5D;
            var y = (double) blockPos.getY() + height;
            var z = (double) blockPos.getZ() + ((random.nextDouble() - random.nextDouble()) * 0.375D) + 0.5D;
            level.addParticle(ParticleTypes.BUBBLE, x, y, z, 0.0, 0.0, 0.0);
        }

        var light = variant.isOf(Fluids.LAVA) ? LightTexture.FULL_BRIGHT : i;

        var consumer = multiBufferSource.getBuffer(RenderType.entityTranslucentCull(texture.atlasLocation()));
        vertex(consumer, last, toX, height, fromZ, u0, v1, color, light);
        vertex(consumer, last, fromX, height, fromZ, u1, v1, color, light);
        vertex(consumer, last, fromX, height, toZ, u1, v0, color, light);
        vertex(consumer, last, toX, height, toZ, u0, v0, color, light);
        poseStack.popPose();
    }

    private void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int color, int i) {
        consumer.vertex(pose.pose(), x, y, z).color(color).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(i).normal(pose.normal(), 0.0F, 1.0F, 0.0F).endVertex();
    }
}
