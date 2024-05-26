package com.harleylizard.trouble.common.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface Ritual {

    void apply(Level level, BlockPos blockPos);
}
