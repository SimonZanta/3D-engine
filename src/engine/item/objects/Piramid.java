package engine.item.objects;

import engine.Texture;

public class Piramid extends Object{
    private final int[] indices;
    private final float[] texCoords;
    private final float[] positions;
    private final Texture texture;
    private final Texture normalMap;
    private final float[] normals;

    public Piramid() {
        this.normalMap = null;
        this.texture = null;
        this.texCoords = new float[]{
                1,1,1,
                1,1,1,
                1,1,1,
                1,1,1,
        };
        this.positions = new float[]{
                -0.5f, -0.5f, 0.5f,
                0, 1f, 0,
                0.5f, -0.5f, 0.5f,
                0, -0.5f, -0.5f,
        };
        this.indices = new int[]{
                0,1,2,
                2,0,3,
                3,0,1,
                1,3,2
        };
        this.normals = new float[]{
                0,1f,0,
                0,0,0,
                0,1f,0,
                0,1f,0
        };
    }

    @Override
    protected float[] getPositions() {
        return positions;
    }

    @Override
    protected int[] getIndices() {
        return indices;
    }

    @Override
    protected float[] getTexCoords() {
        return texCoords;
    }

    @Override
    protected Texture getTexture() {
        return texture;
    }

    @Override
    protected Texture getNormalMap() {
        return normalMap;
    }

    @Override
    protected float[] getNormals() {
        return normals;
    }

    @Override
    protected boolean isHasAnimation() {
        return false;
    }
}
