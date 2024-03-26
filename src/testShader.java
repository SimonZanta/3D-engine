
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memFree;

public class testShader {
    private int width=800, height=600;
    private long window;
    private ShaderProgram shaderProgram;
    private Mesh mesh;
    private Renderer renderer;
    private List<GameItem> gameItems;

    public void init() throws Exception {

        //init glfw for window creation
        glfwInit();

        //add context - version to glfw
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        //create window of size 800/600 with title idkTest
        window = glfwCreateWindow(width, height, "idkTest", NULL, NULL);

        if(window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        glfwSetFramebufferSizeCallback(window, new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                if (width > 0 && height > 0 &&
                        (testShader.this.width != width || testShader.this.height != height)) {
                    testShader.this.width = width;
                    testShader.this.height = height;
                }
            }
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        //add interactivity to glfw
        glfwMakeContextCurrent(window);

        //initialize OpenGL, without this, nothing will happen
        GL.createCapabilities();

        glViewport(0,0,800,600);

        //enable z-buffer
        glEnable(GL_DEPTH_TEST);

        //load shaders from file
        String shaderVertSrc = Utils.readFile("resources/shaders/shader.vert");
        String shaderFragSrc = Utils.readFile("resources/shaders/shader.frag");

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertShader(shaderVertSrc);
        shaderProgram.createFragShader(shaderFragSrc);
        shaderProgram.link();

        renderer = new Renderer(shaderProgram);

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("textureSampler");

        float[] colours = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };

        // it seems uneffective, but this way of duplicating vertecies is really the most effective
        float[] positions = new float[] {
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        };
        float[] texCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };

        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,};


        List<Mesh> meshList = new ArrayList<>();
//        mesh = new Mesh(positions, indices, colours);
        mesh = new Mesh(positions, indices, texCoords, new Texture("model/cube/cube.png"));
        meshList.add(mesh);

        gameItems = new ArrayList<>();
        gameItems.add(new GameItem(mesh));


        gameLoop();

        //destroy window when is purge time
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void gameLoop() {
        while(!glfwWindowShouldClose(window)){
            renderer.render(gameItems);
            glfwSwapInterval(1);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) throws Exception {
        new testShader().init();
    }
}