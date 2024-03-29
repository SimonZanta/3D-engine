package game.objects;

import engine.Texture;
import engine.item.Mesh;

import java.io.IOException;

public class Cube extends Object{
    private final int[] indices;
    private final float[] texCoords;
    private final float[] positions;
    private final Texture texture;
    private final Texture normalMap;
    private final float[] normals;
    public Cube(String texturePath, String normalMapPath) throws IOException {

        texture = new Texture(texturePath);
        normalMap = new Texture(normalMapPath);

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
        normals = calculateNormals(positions, indices);
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


    public static float[] calculateNormals(float[] positions, int[] indices) {
        int numVertices = positions.length / 3;
        float[] normals = new float[numVertices * 3];

        for (int i = 0; i < numVertices; i++) {
            normals[i * 3] = 0;
            normals[i * 3 + 1] = 0;
            normals[i * 3 + 2] = 0;
        }

        for (int i = 0; i < indices.length; i += 3) {
            int i1 = indices[i] * 3;
            int i2 = indices[i + 1] * 3;
            int i3 = indices[i + 2] * 3;

            float[] v1 = {positions[i1], positions[i1 + 1], positions[i1 + 2]};
            float[] v2 = {positions[i2], positions[i2 + 1], positions[i2 + 2]};
            float[] v3 = {positions[i3], positions[i3 + 1], positions[i3 + 2]};

            float[] normal = calculateFaceNormal(v1, v2, v3);

            for (int j = 0; j < 3; j++) {
                normals[indices[i] * 3 + j] += normal[j];
                normals[indices[i + 1] * 3 + j] += normal[j];
                normals[indices[i + 2] * 3 + j] += normal[j];
            }
        }

        for (int i = 0; i < numVertices; i++) {
            float length = (float) Math.sqrt(normals[i * 3] * normals[i * 3] +
                    normals[i * 3 + 1] * normals[i * 3 + 1] +
                    normals[i * 3 + 2] * normals[i * 3 + 2]);
            if (length != 0) {
                normals[i * 3] /= length;
                normals[i * 3 + 1] /= length;
                normals[i * 3 + 2] /= length;
            }
        }

        return normals;
    }

    private static float[] calculateFaceNormal(float[] v1, float[] v2, float[] v3) {
        float[] normal = new float[3];

        float[] edge1 = new float[3];
        float[] edge2 = new float[3];

        for (int i = 0; i < 3; i++) {
            edge1[i] = v2[i] - v1[i];
            edge2[i] = v3[i] - v1[i];
        }

        normal[0] = edge1[1] * edge2[2] - edge1[2] * edge2[1];
        normal[1] = edge1[2] * edge2[0] - edge1[0] * edge2[2];
        normal[2] = edge1[0] * edge2[1] - edge1[1] * edge2[0];

        return normal;
    }
}
