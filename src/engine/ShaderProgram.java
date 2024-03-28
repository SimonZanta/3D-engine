package engine;

import engine.ligh.PointLight;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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
    public void setUniform(String uniformName, int value){
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, float value){
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value){
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, Vector4f value){
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    public void createPointLightUniform(String uniformName){
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }

    public void createMaterialUniform(String uniformName){
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }

    public void setUniform(String uniformName, PointLight pointLight){
        setUniform(uniformName + ".color", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        setUniform(uniformName + ".att.constant", pointLight.getAttenuation().getConstant());
        setUniform(uniformName + ".att.linear", pointLight.getAttenuation().getLinear());
        setUniform(uniformName + ".att.exponent", pointLight.getAttenuation().getExponent());
    }

    public void setUniform(String uniformName, Material material){
        setUniform(uniformName + ".ambient", material.getAmbientColour());
        setUniform(uniformName + ".diffuse", material.getDiffuseColour());
        setUniform(uniformName + ".specular", material.getSpecularColour());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1:0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }
}
