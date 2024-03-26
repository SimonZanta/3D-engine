package game.objects;

import engine.Texture;
import engine.item.GameItem;
import engine.item.Mesh;

public abstract class Object {

    protected abstract float[] getPositions();
    protected abstract int[] getIndices();
    protected abstract float[] getTexCoords();
    protected abstract Texture getTexture();

    public Mesh createMesh(){
        float[] positions = getPositions();
        int[] indices = getIndices();
        float[] texCoords = getTexCoords();
        Texture texture = getTexture();

        return new Mesh(positions, indices, texCoords, texture);
    }

    public GameItem createGameItem(){
        return new GameItem(createMesh());
    }

}
