package engine.item;

import engine.Material;
import engine.Texture;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Mesh {
    private static final Vector3f defaultColor = new Vector3f(1.f, 1.f, 1.f);
    private final int vaoId;
    private final int texVboId;
    private final int normalVboId;
    private final int vboPosId;
    private final int vboInxId;
    private final int vertexCount;
    private Material material;
//    private Texture texture;
//    private Vector3f color;

    public Mesh(float[] positions, int[] indices, float[] texCoords, float[] normals){

//        color = Mesh.defaultColor;
        material = new Material();
        vertexCount = indices.length;

        FloatBuffer verticesBuffer = null;
        FloatBuffer texCoordsBuffer = null;
        IntBuffer indexBuffer = null;
        FloatBuffer normalBuffer = null;

        try {
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();

            texCoordsBuffer = MemoryUtil.memAllocFloat(texCoords.length);
            texCoordsBuffer.put(texCoords).flip();

            indexBuffer = MemoryUtil.memAllocInt(indices.length);
            indexBuffer.put(indices).flip();

            normalBuffer = MemoryUtil.memAllocFloat(normals.length);
            normalBuffer.put(normals).flip();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            vboInxId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboInxId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
            memFree(indexBuffer);

            vboPosId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboPosId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            texVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, texVboId);
            glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW);

            // index is set to 1, because its next attrib in vert shader
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            memFree(texCoordsBuffer);

            normalVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, normalVboId);
            glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            glBindVertexArray(0);
        } finally {
            if (verticesBuffer != null) {
                memFree(verticesBuffer);
            }
        }
    }

    public void setTexture(Texture texture) {
        this.material.setTexture(texture);
    }

    public void setNormalTexture(Texture texture) {
        this.material.setNormalTexture(texture);
    }

    public boolean isTextured(){
        return material.isTextured();
    }

    public boolean isNormalTexture(){
        return material.isNormalTexture();
    }

    public Material getMaterial() {
        return material;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboPosId);
        glDeleteBuffers(vboInxId);
        glDeleteBuffers(texVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);

    }

    public void render(){
        //bind texture if it exists
        if(isTextured()){
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, material.getTexture().getTextId());
        }

        if(isNormalTexture()){
            // Activate first texture bank
            glActiveTexture(GL_TEXTURE1);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, material.getNormalTexture().getTextId());
        }

        glBindVertexArray(getVaoId());

        //enable location in vertex shader
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT,0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
