package engine;

import org.joml.Math;
import org.joml.Vector3f;

public class Camera {
    private final Vector3f position;

    private final Vector3f rotation;

    public Camera() {
        position = new Vector3f(0, 0, 5);
        rotation = new Vector3f(0, 0, 1);
    }
    public Vector3f getPosition() {
        return position;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void moveRotation(float x, float y) {
        float scaledDeltaX = x * 0.05f;
        float scaledDeltaY = y * 0.05f;
        rotation.x += scaledDeltaY;
        rotation.y += scaledDeltaX;
    }
}
