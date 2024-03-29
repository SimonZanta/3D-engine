package game.objects;

import engine.Texture;
import engine.item.GameItem;
import engine.item.Mesh;

import java.util.Arrays;

public abstract class Object {

    protected abstract float[] getPositions();
    protected abstract int[] getIndices();
    protected abstract float[] getTexCoords();
    protected abstract Texture getTexture();
    protected  abstract  float[] getNormals();

    public Mesh createMesh(){
        float[] positions = getPositions();
        int[] indices = getIndices();
        float[] texCoords = getTexCoords();
        float[] normals = getNormals();

        Mesh returnMesh =  new Mesh(positions, indices, texCoords, normals);

        if(getTexture() != null){
            returnMesh.setTexture(getTexture());
        }
        return returnMesh;
    }

    public GameItem createGameItem(){
        return new GameItem(createMesh());
    }

}
