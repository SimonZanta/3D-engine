package game;

import engine.*;
import engine.item.GameItem;
import engine.item.Mesh;
import engine.ligh.PointLight;
import game.objects.Cube;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import render.GameLoop;
import render.Renderer;
import render.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Run {

    Vector3f lightPos;
    public static void main(String[] args) throws Exception {
        new Run().init();
    }
    
    public void init() throws Exception {

//      ----------- WINDOW INIT ------------------------------------------------------
        //init glfw for window creation
        glfwInit();

        Window window = new Window(800, 600);

        //add interactivity to glfw
        glfwMakeContextCurrent(window.getWindow());

        //initialize OpenGL
        //don't write anything except window handling before this
        GL.createCapabilities();

//      ----------- GL SETUP --------------------------------------------------------
        glViewport(0,0,800,600);

        //enable z-buffer
        glEnable(GL_DEPTH_TEST);

//      ----------- SHADER SETUP ----------------------------------------------------

        ShaderProgram shaderProgram = new ShaderProgram();
        Renderer renderer = new Renderer(shaderProgram);


        //load fragment and vertex shaders from file
        String shaderVertSrc = Utils.readFile("resources/shaders/shader.vert");
        String shaderFragSrc = Utils.readFile("resources/shaders/shader.frag");

        shaderProgram.createVertShader(shaderVertSrc);
        shaderProgram.createFragShader(shaderFragSrc);
        shaderProgram.link();

        //create uniforms
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("normalMap");

        shaderProgram.createMaterialUniform("material");
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");

        shaderProgram.createPointLightUniform("pointLight");

//      ----------- OBJECT INIT -----------------------------------------------------
        //TODO create scene, rather than gameItems themselves

        List<GameItem> gameItems = new ArrayList<>();
        GameItem cube = new Cube("/model/cube/rock.png", "/model/cube/rock_normals.png").createGameItem();
//        Mesh cubeMesh = OBJLoader.loadMesh("/model/cube/cube.obj");
//        GameItem cube = new GameItem(cubeMesh);

        gameItems.add(cube);

        Vector3f ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);

        //TODO: create light class, with visualizing where light is - sphere
        Vector3f lightPos = new Vector3f(0,1,-1);
        Vector3f lightColor = new Vector3f(1f,1f,1f);
        PointLight pointLight = new PointLight(lightColor, lightPos, 10f, new PointLight.Attenuation(0,0,1));

//      ----------- GAME LOOP -------------------------------------------------------
        GameLoop gameLoop = new GameLoop(renderer, window);
        gameLoop.play(gameItems, ambientLight, pointLight);

        //destroy window
        glfwDestroyWindow(window.getWindow());
        glfwTerminate();
    }
    //TODO implement movement
    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_RIGHT)) {
            lightPos.y += 0.5f;
        }
    }
}