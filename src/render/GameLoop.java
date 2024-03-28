package render;

import engine.item.GameItem;
import engine.ligh.PointLight;
import org.joml.Vector3f;

import java.util.List;

import static java.awt.SystemColor.window;
import static org.lwjgl.glfw.GLFW.*;

public class GameLoop {
    private Renderer renderer;
    private final Window window;
    public GameLoop(Renderer renderer, Window window) {
        this.renderer = renderer;
        this.window = window;
    }

    public void play(List<GameItem> gameItems, Vector3f ambientLight){
        while(!glfwWindowShouldClose(window.getWindow())){
            renderer.render(gameItems, ambientLight);
            glfwSwapInterval(1);
            glfwSwapBuffers(window.getWindow());
            glfwPollEvents();
        }
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
}
