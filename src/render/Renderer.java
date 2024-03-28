package render;

import engine.Camera;
import engine.item.GameItem;
import engine.ShaderProgram;
import engine.Transformation;
import engine.ligh.PointLight;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private final ShaderProgram shaderProgram;
    private final Transformation transformation;
    private float fov = (float) Math.toRadians(60.0f);
    private float zNear = 0.01f;
    private float zFar = 1000.f;
    private Camera camera;
    private float specularPower;

    public Renderer(ShaderProgram shaderProgram) throws Exception {
        this.shaderProgram = shaderProgram;
        this.transformation = new Transformation();
        this.camera = new Camera();
        this.specularPower = 10f;
    }

    public void render(List<GameItem> gameItems, PointLight pointLight, Vector3f ambientLight){
        clearWindow();

        shaderProgram.bind();
        Matrix4f projectionMatrix = transformation.getProjectionMat(fov, 800, 600, zNear, zFar);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("tex_sampler", 0);
        shaderProgram.setUniform("ambientLight", ambientLight);
        shaderProgram.setUniform("specularPower", specularPower);

        PointLight currPointLight = new PointLight(pointLight);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;

        shaderProgram.setUniform("pointLight", currPointLight);


        for(GameItem gameItem : gameItems){
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);

            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
//            shaderProgram.setUniform("useColor", gameItem.getMesh().isTextured() ? 0 : 1);

            float rotation = gameItem.getRotation().x + 1.5f;
            if ( rotation > 360 ) {
                rotation = 0;
            }
            gameItem.setRotation(rotation, rotation, rotation);

            gameItem.setPosition(0,0,-5);
            shaderProgram.setUniform("material", gameItem.getMesh().getMaterial());
            
            gameItem.getMesh().render();
        }


        shaderProgram.unbind();
    }

    private static void clearWindow() {
        glClearColor(0.0784f, 0.0784f, 0.0784f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}