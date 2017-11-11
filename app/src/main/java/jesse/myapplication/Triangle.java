package jesse.myapplication;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import util.RawResourceReader;

/**
 * Created by Jesse on 2/11/2017.
 */

public class Triangle {

    OpenGLRenderer mRenderer;

    private FloatBuffer vertexBuffer;
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // in counterclockwise order:
            0.0f,  0.622008459f, -1.0f, // top
            -0.5f, -0.311004243f, -1.0f, // bottom left
            0.5f, -0.311004243f, -1.0f  // bottom right
    };
    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    private final int program;

    private int positionHandle;
    private int colourHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private int mMVPMatrixHandle;

    public Triangle(OpenGLRenderer renderer) {
        mRenderer = renderer;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        int vertexShader = OpenGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, Shaders.VERTEX_SHADER);
        int fragmentShader = OpenGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, Shaders.COLOUR_FRAGMENT_SHADER_STATIC);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix)
    {
        // add the program
        GLES20.glUseProgram(program);

        // get handle to vertex shader's position member
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        // enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // prepare the triangle coord data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // get handle to fragment shader's colour member
        colourHandle = GLES20.glGetUniformLocation(program, "vColour");
        // set colour for drawing triangle
        GLES20.glUniform4fv(colourHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        mRenderer.checkGLError("glGetUniformLocation");

        // pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        mRenderer.checkGLError("glUniformMatrix4fv");

        // draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
