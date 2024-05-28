package com.harleylizard.trouble.common;

import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class Maths {

    private Maths() {}

    public static VoxelShape rotateShape(Quaternionf quaternionf, VoxelShape shape) {
        var matrix4f = new Matrix4f();
        matrix4f.translate(0.5F, 0.5F, 0.5F);
        matrix4f.rotate(quaternionf);
        matrix4f.translate(-0.5F, -0.5F, -0.5F);

        var min = new Vector3f();
        var max = new Vector3f();

        var result = Shapes.empty();
        for (var aabb : shape.toAabbs()) {
            var minX = (float) aabb.minX;
            var minY = (float) aabb.minY;
            var minZ = (float) aabb.minZ;
            var maxX = (float) aabb.maxX;
            var maxY = (float) aabb.maxY;
            var maxZ = (float) aabb.maxZ;
            matrix4f.transformAab(minX, minY, minZ, maxX, maxY, maxZ, min, max);

            result = Shapes.join(result, Shapes.create(min.x, min.y, min.z, max.x, max.y, max.z), BooleanOp.OR);
        }
        return result;
    }
}
