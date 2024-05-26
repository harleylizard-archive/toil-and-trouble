package com.harleylizard.trouble.common.block;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

public interface BrewingCauldron extends EntityBlock {
    EnumProperty<FluidType> FLUID_TYPE = EnumProperty.create("fluid_type", FluidType.class);

    AABB getShape();

    static int getLightLevel(BlockState blockState) {
        return getFluidType(blockState) == FluidType.LAVA ? 15 : 0;
    }

    static FluidType getFluidType(BlockState blockState) {
        return blockState.getValue(FLUID_TYPE);
    }

    static BlockState setFluidType(BlockState blockState, FluidVariant variant) {
        return blockState.setValue(FLUID_TYPE, variant.isBlank() ? FluidType.EMPTY : variant.isOf(Fluids.LAVA) ? FluidType.LAVA : FluidType.WATER);
    }

    enum FluidType implements StringRepresentable {
        EMPTY("empty"),
        WATER("water"),
        LAVA("lava");

        private final String name;

        FluidType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
