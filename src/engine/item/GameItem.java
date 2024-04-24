package engine.item;

import org.joml.Vector3f;

public class GameItem {
    private final Mesh mesh;
    private final Vector3f position;
    private float scale;
    private final Vector3f rotation;
    private boolean hasAnimation;
    private int prevRotation = 0;
    private boolean changeAnimDirection = false;

    public GameItem(Mesh mesh, boolean hasAnimation) {
        this.mesh = mesh;
        this.hasAnimation = hasAnimation;
        this.position = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);
        this.scale = 1;
    }

    public void playAnimation(){
        if (changeAnimDirection){
            int currentRotation = prevRotation - 1;
            if(currentRotation < -360){
                currentRotation = 0;
            }
            setRotation(0, currentRotation, 0);
            prevRotation = currentRotation;
        }else{
            int currentRotation = prevRotation + 1;
            if(currentRotation > 360){
                currentRotation = 0;
            }
            setRotation(0, currentRotation, 0);
            prevRotation = currentRotation;
        }
    }

    public boolean isChangeAnimDirection() {
        return changeAnimDirection;
    }

    public void setChangeAnimDirection(boolean changeAnimDirection) {
        this.changeAnimDirection = changeAnimDirection;
    }

    public boolean isHasAnimation() {
        return hasAnimation;
    }

    public void setHasAnimation(boolean hasAnimation) {
        this.hasAnimation = hasAnimation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z){
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setPosition(Vector3f position){
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z){
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
