import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glCompileShader;

public class ShaderProgram {
    private final int shaderProgramId;
    private int vertShaderId;
    private int fragShaderId;
    private final Map<String, Integer> uniforms;

    public ShaderProgram() {
        this.uniforms = new HashMap<>();
        // no check for file creation fail
        this.shaderProgramId = glCreateProgram();
    }

    public void createVertShader(String shaderString){
        vertShaderId = getShader(GL_VERTEX_SHADER, shaderString);
    }

    public void createFragShader(String shaderString){
        fragShaderId = getShader(GL_FRAGMENT_SHADER, shaderString);
    }

    public void link(){
        glAttachShader(shaderProgramId, vertShaderId);
        glAttachShader(shaderProgramId, fragShaderId);
        glLinkProgram(shaderProgramId);

        //deleting shader that are already in use in runtime
        glDeleteShader(vertShaderId);
        glDeleteShader(fragShaderId);
    }

    public void bind(){
        glUseProgram(shaderProgramId);
    }

    public void unbind(){
        glUseProgram(0);
    }

    private int getShader(int glShader, CharSequence shaderSrc) {
        int shader = glCreateShader(glShader);
        glShaderSource(shader, shaderSrc);
        glCompileShader(shader);
        return shader;
    }

    public void createUniform(String uniformName){
        int uniformLocation = glGetUniformLocation(shaderProgramId, uniformName);
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer floatBuffer = stack.mallocFloat(16);
            value.get(floatBuffer);
            glUniformMatrix4fv(uniforms.get(uniformName), false, floatBuffer);
        }
    }
}
