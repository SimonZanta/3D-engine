import org.lwjgl.system.MemoryUtil;
import org.w3c.dom.Text;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Mesh {
    private final int vaoId;
//    private final int colorVboId;
    private final int texVboId;
    private final int vboPosId;
    private final int vboInxId;
    private final int vertexCount;

    public Mesh(float[] positions, int[] indices, float[] texCoords, Texture texture){
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextId());


        vertexCount = indices.length;
        FloatBuffer verticesBuffer = null;
        FloatBuffer texCoordsBuffer = null;
        IntBuffer indexBuffer = null;
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();

            texCoordsBuffer = MemoryUtil.memAllocFloat(texCoords.length);
            texCoordsBuffer.put(texCoords).flip();

            indexBuffer = MemoryUtil.memAllocInt(indices.length);
            indexBuffer.put(indices).flip();

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

            glBindVertexArray(0);
        } finally {
            if (verticesBuffer != null) {
                memFree(verticesBuffer);
            }
        }
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
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT,0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }
}
