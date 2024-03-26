package engine;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class Transformation {
    private final Matrix4f projectionMat;
    private final Matrix4f worldMat;

    public Transformation() {
        this.projectionMat = new Matrix4f();
        this.worldMat = new Matrix4f();
    }

    public Matrix4f getProjectionMat(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        projectionMat.identity();
        projectionMat.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMat;
    }

    public Matrix4f getWorldMat(Vector3f offset, Vector3f rotation, float scale) {
        worldMat.identity();
        worldMat.translate(offset).
                rotateX((float) Math.toRadians(rotation.x)).
                rotateY((float) Math.toRadians(rotation.y)).
                rotateZ((float) Math.toRadians(rotation.z)).
                scale(scale);
        return worldMat;
    }
}
