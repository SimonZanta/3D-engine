package engine.item;

import org.joml.Vector3f;

public class GameItem {
    private final Mesh mesh;
    private final Vector3f position;
    private float scale;
    private final Vector3f rotation;
    private boolean hasAnimation;
    private Vector3f prevRotation = new Vector3f(0,0,0);
    private boolean changeAnimDirection = false;

    public GameItem(Mesh mesh, boolean hasAnimation) {
        this.mesh = mesh;
        this.hasAnimation = hasAnimation;
        this.position = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);
        this.scale = 1;
    }

    public GameItem(Mesh mesh, boolean hasAnimation, Vector3f rotation) {
        this.mesh = mesh;
        this.hasAnimation = hasAnimation;
        this.position = new Vector3f(0,0,0);
        this.rotation = rotation;
        this.prevRotation = rotation;
        this.scale = 1;
    }

    public void playAnimation(){
        if (changeAnimDirection){
            Vector3f currentRotation = new Vector3f(prevRotation.x, prevRotation.y - 1, prevRotation.z);
            if(currentRotation.y < -360){
                currentRotation.y = 0;
            }
            setRotation(currentRotation);
            prevRotation = currentRotation;
        }else{
            Vector3f currentRotation = new Vector3f(prevRotation.x, prevRotation.y + 1, prevRotation.z);
            if(currentRotation.y > 360){
                currentRotation.y = 0;
            }
            setRotation(currentRotation);
            prevRotation = currentRotation;
        }
    }

    public Vector3f getPrevRotation() {
        return prevRotation;
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

    public void setRotation(Vector3f rotation){
        this.rotation.x = rotation.x;
        this.rotation.y = rotation.y;
        this.rotation.z = rotation.z;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
