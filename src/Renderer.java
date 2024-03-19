import org.joml.Matrix4f;

import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private final ShaderProgram shaderProgram;
    private final Transformation transformation;
    private float fov = (float) Math.toRadians(60.0f);
    private float zNear = 0.01f;
    private float zFar = 1000.f;

    public Renderer(ShaderProgram shaderProgram) throws Exception {
        this.shaderProgram = shaderProgram;
        this.transformation = new Transformation();
    }

    public void render(List<GameItem> gameItems){
        clearWindow();

        shaderProgram.bind();
        Matrix4f projectionMatrix = transformation.getProjectionMat(fov, 800, 600, zNear, zFar);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        for(GameItem gameItem : gameItems){

            Matrix4f worldMatrix = transformation.getWorldMat(
                                        gameItem.getPosition(),
                                        gameItem.getRotation(),
                                        gameItem.getScale());

            shaderProgram.setUniform("worldMatrix", worldMatrix);

            float rotation = gameItem.getRotation().x + 1.5f;
            if ( rotation > 360 ) {
                rotation = 0;
            }
            gameItem.setRotation(rotation, rotation, rotation);

            gameItem.setPosition(0,0,-5);

            gameItem.getMesh().render();
        }


        shaderProgram.unbind();
    }

    private static void clearWindow() {
        glClearColor(0.0784f, 0.0784f, 0.0784f, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}