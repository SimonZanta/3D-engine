package render;

import engine.item.GameItem;
import engine.ligh.PointLight;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.List;

import static java.awt.SystemColor.window;
import static org.lwjgl.glfw.GLFW.*;

public class GameLoop {
    private Renderer renderer;
    private final long window;
    public GameLoop(Renderer renderer, long window) {
        this.renderer = renderer;
        this.window = window;
    }

    public void play(List<GameItem> gameItems, Vector3f ambientLight, PointLight pointLight) throws IOException {
        while(!glfwWindowShouldClose(window)){

            renderer.render(gameItems, ambientLight, pointLight);
            glfwSwapInterval(1);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
}
