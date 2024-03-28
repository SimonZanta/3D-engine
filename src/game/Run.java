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
        shaderProgram.createUniform("tex_sampler");

//        shaderProgram.createUniform("color");
//        shaderProgram.createUniform("useColor");

        shaderProgram.createMaterialUniform("material");
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");

//      ----------- OBJECT INIT -----------------------------------------------------
        //TODO create scene, rather than gameItems themselves

        List<GameItem> gameItems = new ArrayList<>();
        GameItem cube = new Cube("/model/cube/cube.png").createGameItem();

        gameItems.add(cube);

        Vector3f ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        PointLight pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);

//      ----------- GAME LOOP -------------------------------------------------------
        GameLoop gameLoop = new GameLoop(renderer, window);
        gameLoop.play(gameItems, pointLight, ambientLight);

        //destroy window
        glfwDestroyWindow(window.getWindow());
        glfwTerminate();
    }
}