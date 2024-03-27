package game.objects;

import engine.Texture;
import engine.item.Mesh;

import java.io.IOException;

public class Cube extends Object{
    private final int[] indices;
    private final float[] texCoords;
    private final float[] positions;
    private final Texture texture;
    private final float[] normals;
    public Cube(String texturePath) throws IOException {

        texture = new Texture(texturePath);

        float[] colours = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f
        };

        // it seems uneffective, but this way of duplicating vertecies is really the most effective
        positions = new float[] {
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f
        };

        //chatbot did it so chance of working are close to 0
        normals = new float[] {
                // Front face
                0.0f, 0.0f, 1.0f,  // V0
                0.0f, 0.0f, 1.0f,  // V1
                0.0f, 0.0f, 1.0f,  // V2
                0.0f, 0.0f, 1.0f,  // V3

                // Back face
                0.0f, 0.0f, -1.0f, // V4
                0.0f, 0.0f, -1.0f, // V5
                0.0f, 0.0f, -1.0f, // V6
                0.0f, 0.0f, -1.0f, // V7

                // Top face
                0.0f, 1.0f, 0.0f,  // V8
                0.0f, 1.0f, 0.0f,  // V9
                0.0f, 1.0f, 0.0f,  // V10
                0.0f, 1.0f, 0.0f,  // V11

                // Bottom face
                0.0f, -1.0f, 0.0f, // V12
                0.0f, -1.0f, 0.0f, // V13
                0.0f, -1.0f, 0.0f, // V14
                0.0f, -1.0f, 0.0f, // V15

                // Left face
                -1.0f, 0.0f, 0.0f, // V16
                -1.0f, 0.0f, 0.0f, // V17
                -1.0f, 0.0f, 0.0f, // V18
                -1.0f, 0.0f, 0.0f, // V19

                // Right face
                1.0f, 0.0f, 0.0f,  // V20
                1.0f, 0.0f, 0.0f,  // V21
                1.0f, 0.0f, 0.0f,  // V22
                1.0f, 0.0f, 0.0f   // V23
        };

        texCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f
        };

        indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7
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
    protected float[] getNormals() {
        return normals;
    }
}
