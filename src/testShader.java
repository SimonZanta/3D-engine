
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

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

        glEnable(GL_DEPTH_TEST);

//        float[] triangle =  {
//                -0.5f, -0.5f, 0.0f,
//                0.5f, -0.5f, 0.0f,
//                0.0f,  0.5f, 0.0f
//        };
//
//        // every vertex is on independent line -
//        float[] rectangle = new float[]{
//                -0.5f,  0.5f, -5.05f,
//                -0.5f, -0.5f, -5.05f,
//                0.5f, -0.5f, -5.05f,
//                0.5f,  0.5f, -5.05f,
//        };
//        int[] rectangleIndx = new int[]{
//                0, 1, 3, 3, 1, 2,
//        };
//        float[] colors = new float[]{
//                0.5f, 0.0f, 0.0f,
//                0.0f, 0.5f, 0.0f,
//                0.0f, 0.0f, 0.5f,
//                0.0f, 0.5f, 0.5f,
//        };

        float[] positions = new float[] {
                // VO
                -0.5f,  0.5f,  0.5f,
                // V1
                -0.5f, -0.5f,  0.5f,
                // V2
                0.5f, -0.5f,  0.5f,
                // V3
                0.5f,  0.5f,  0.5f,
                // V4
                -0.5f,  0.5f, -0.5f,
                // V5
                0.5f,  0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
        };

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

        int[] indices = new int[] {
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                6, 1, 0, 6, 0, 4,
                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5,
        };


        List<Mesh> meshList = new ArrayList<>();
        mesh = new Mesh(positions, indices, colours);
        meshList.add(mesh);

        gameItems = new ArrayList<>();
        gameItems.add(new GameItem(mesh));
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