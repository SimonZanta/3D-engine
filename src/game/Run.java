package game;

import engine.*;
import engine.item.GameItem;
import engine.ligh.PointLight;
import engine.item.objects.Cube;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.opengl.GL;
import render.Renderer;
import render.WelcomeText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Run {
    long window;
    private final Vector2d prevPos = new Vector2d(0,0);
    private boolean firstMove = true;
    private double lastFrameTime;

    private GameItem cube;
    private boolean isCubeTextured = true;
    List<GameItem> gameItems = new ArrayList<>();
    Camera camera;
    public static void main(String[] args) throws Exception {
        new Run().init();
    }


    
    public void init() throws Exception {

//      ----------- WINDOW INIT ------------------------------------------------------
        //init glfw for window creation
        glfwInit();

        window = glfwCreateWindow(800, 600, "Zapoctovy Projekt Normalove Mapy", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        camera = new Camera();

        lastFrameTime = glfwGetTime();

        glfwSwapInterval(1);

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        //add interactivity to glfw
        glfwMakeContextCurrent(window);

        //initialize OpenGL
        //don't write anything except window handling before this
        GL.createCapabilities();

//      ----------- GL SETUP --------------------------------------------------------
        glViewport(0,0,800,600);

        //enable z-buffer
        glEnable(GL_DEPTH_TEST);

//      ----------- SHADER SETUP ----------------------------------------------------
        new WelcomeText().drawWelcomeText();

        ShaderProgram shaderProgram = new ShaderProgram();
        Renderer renderer = new Renderer(shaderProgram, camera);


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
        cube = new Cube("/model/cube/rock.png", "/model/cube/rock_normals.png").createGameItem();


        Vector3f ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);

        Vector3f lightPos = new Vector3f(1.5f,0, 1);
        Vector3f lightColor = new Vector3f(1f,1f,1f);
        PointLight pointLight = new PointLight(lightColor, lightPos, 2f, new PointLight.Attenuation(0,0,1));

        gameItems.add(pointLight.getGameItem());
        gameItems.add(cube);

//      ----------- GAME LOOP -------------------------------------------------------

        while(!glfwWindowShouldClose(window)){
            double currentTime = glfwGetTime();
            double deltaTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            updateCamera(deltaTime);

            renderer.render(gameItems, ambientLight, pointLight);
            glfwSwapInterval(1);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        //destroy window
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void updateCamera(double deltaTime){
        float cameraSpeed = 1f;

        float cameraMove = (float) (cameraSpeed * 0.05 * (deltaTime * 1000.0f));

        glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                if (firstMove) {
                    prevPos.x = x;
                    prevPos.y = y;
                    firstMove = false;
                }
                float deltaX = (float)(x - prevPos.x);
                float deltaY = (float)(y - prevPos.y);


                prevPos.x = x;
                prevPos.y = y;
                camera.moveRotation(deltaX, deltaY);
            }
        });

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);

            if(action == GLFW_PRESS || action == GLFW_REPEAT) {
                GameItem item = gameItems.get(1);
                switch (key) {
                    case GLFW_KEY_W:
                        camera.movePosition(0,0,-cameraMove);
                        break;
                    case GLFW_KEY_A:
                        camera.movePosition(-cameraMove,0,0);
                        break;
                    case GLFW_KEY_S:
                        camera.movePosition(0,0, cameraMove);
                        break;
                    case GLFW_KEY_D:
                        camera.movePosition(cameraMove, 0,0);
                        break;
                    case GLFW_KEY_P:
                        if(isCubeTextured){
                            try {
                                cube = new Cube().createGameItem();
                                gameItems.remove(1);
                                gameItems.add(cube);
                                isCubeTextured = false;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            try {
                                cube = new Cube("/model/cube/rock.png", "/model/cube/rock_normals.png").createGameItem();
                                gameItems.remove(1);
                                gameItems.add(cube);
                                isCubeTextured = true;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                    case GLFW_KEY_SPACE:
                        if(item.isHasAnimation()){
                            item.setHasAnimation(false);
                        }else{
                            item.setHasAnimation(true);
                        }
                        break;
                    case GLFW_KEY_J:
                        if(!item.isChangeAnimDirection()){
                            item.setChangeAnimDirection(true);
                        }else{
                            item.setChangeAnimDirection(false);
                        }
                        break;

                }
            }
        });
    }
}