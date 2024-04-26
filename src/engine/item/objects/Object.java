package engine.item.objects;

import engine.Texture;
import engine.item.GameItem;
import engine.item.Mesh;
import org.joml.Vector3f;

public abstract class Object {

    protected abstract float[] getPositions();
    protected abstract int[] getIndices();
    protected abstract float[] getTexCoords();
    protected abstract Texture getTexture();
    protected abstract Texture getNormalMap();
    protected  abstract  float[] getNormals();
    protected abstract boolean isHasAnimation();

    public Mesh createMesh(){
        float[] positions = getPositions();
        int[] indices = getIndices();
        float[] texCoords = getTexCoords();
        float[] normals = getNormals();

        Mesh returnMesh =  new Mesh(positions, indices, texCoords, normals);

        if(getTexture() != null){
            returnMesh.setTexture(getTexture());
        }

        if(getNormalMap() != null){
            returnMesh.setNormalTexture(getNormalMap());
        }
        return returnMesh;
    }

    public GameItem createGameItem(){
        return new GameItem(createMesh(), isHasAnimation());
    }

    public GameItem createGameItem(Vector3f rotation){
        return new GameItem(createMesh(), isHasAnimation(),rotation);
    }

}
