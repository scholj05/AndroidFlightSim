package jesse.myapplication;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import Units.Vector3;
import util.ShaderHelper;
import util.TextureHelper;

import static android.opengl.GLES10.glColor4f;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glEnable;
import static android.opengl.GLES10.glEnableClientState;
import static android.opengl.GLES10.glMultMatrixf;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES32.GL_QUADS;
import static javax.microedition.khronos.opengles.GL10.*;

/**
 * Created by Jesse on 25/10/2017.
 */

public class Skybox {
    private static OpenGLRenderer mRenderer;

    Context context;

    private FloatBuffer vertexBuffer;
    private FloatBuffer colourBuffer;
    private FloatBuffer textureBuffer;

    static final int COORDS_PER_VERTEX = 3;
    static final int COORDS_PER_COLOUR = 4;
    static final int COORDS_PER_TEXTURE = 3;
    static final int BYTES_PER_FLOAT = 4;

    static float boxCoords[] = {   // in counterclockwise order:
            // right
            1.0f, 1.0f, -1.0f,      // 3
            1.0f, -1.0f, -1.0f,     // 2
            1.0f, -1.0f, 1.0f,      // 7
            1.0f, -1.0f, 1.0f,      // 7
            1.0f, 1.0f, 1.0f,       // 4
            1.0f, 1.0f, -1.0f,      // 3

            // left
            -1.0f, 1.0f, 1.0f,      // 5
            -1.0f, -1.0f, 1.0f,     // 6
            -1.0f, -1.0f, -1.0f,    // 1
            -1.0f, -1.0f, -1.0f,    // 1
            -1.0f, 1.0f, -1.0f,     // 0
            -1.0f, 1.0f, 1.0f,      // 5

            // top
            1.0f, 1.0f, 1.0f,       // 4
            -1.0f, 1.0f, 1.0f,      // 5
            -1.0f, 1.0f, -1.0f,     // 0
            -1.0f, 1.0f, -1.0f,     // 0
            1.0f, 1.0f, -1.0f,      // 3
            1.0f, 1.0f, 1.0f,       // 4

            // bottom
            1.0f, -1.0f, 1.0f,      // 7
            1.0f, -1.0f, -1.0f,     // 2
            -1.0f, -1.0f, -1.0f,    // 1
            -1.0f, -1.0f, -1.0f,    // 1
            -1.0f, -1.0f, 1.0f,     // 6
            1.0f, -1.0f, 1.0f,      // 7

            // front
            1.0f, 1.0f, 1.0f,       // 4
            1.0f, -1.0f, 1.0f,      // 7
            -1.0f, -1.0f, 1.0f,     // 6
            -1.0f, -1.0f, 1.0f,     // 6
            -1.0f, 1.0f, 1.0f,      // 5
            1.0f, 1.0f, 1.0f,       // 4

            // back
            -1.0f, 1.0f, -1.0f,     // 0
            -1.0f,  -1.0f, -1.0f,   // 1
            1.0f, -1.0f, -1.0f,     // 2
            1.0f, -1.0f, -1.0f,     // 2
            1.0f, 1.0f, -1.0f,      // 3
            -1.0f, 1.0f, -1.0f,     // 0

    };
    float colours[] = {
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,

            0.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f,

            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
    };

    final float[] textureCoords = {

            // right
            1.0f, -1.0f, 1.0f,      // 7
            1.0f, 1.0f, 1.0f,       // 4
            1.0f, 1.0f, -1.0f,      // 3
            1.0f, 1.0f, -1.0f,      // 3
            1.0f, -1.0f, -1.0f,     // 2
            1.0f, -1.0f, 1.0f,      // 7

            // left
            -1.0f, -1.0f, -1.0f,    // 1
            -1.0f, 1.0f, -1.0f,     // 0
            -1.0f, 1.0f, 1.0f,      // 5
            -1.0f, 1.0f, 1.0f,      // 5
            -1.0f, -1.0f, 1.0f,     // 6
            -1.0f, -1.0f, -1.0f,    // 1

            // top
            1.0f, 1.0f, 1.0f,       // 4
            -1.0f, 1.0f, 1.0f,      // 5
            -1.0f, 1.0f, -1.0f,     // 0
            -1.0f, 1.0f, -1.0f,     // 0
            1.0f, 1.0f, -1.0f,      // 3
            1.0f, 1.0f, 1.0f,       // 4

            // bottom
            -1.0f, -1.0f, -1.0f,    // 1
            -1.0f, -1.0f, 1.0f,     // 6
            1.0f, -1.0f, 1.0f,      // 7
            1.0f, -1.0f, 1.0f,      // 7
            1.0f, -1.0f, -1.0f,     // 2
            -1.0f, -1.0f, -1.0f,    // 1

            // front
            -1.0f, -1.0f, 1.0f,     // 6
            -1.0f, 1.0f, 1.0f,      // 5
            1.0f, 1.0f, 1.0f,       // 4
            1.0f, 1.0f, 1.0f,       // 4
            1.0f, -1.0f, 1.0f,      // 7
            -1.0f, -1.0f, 1.0f,     // 6

            // back
            1.0f, -1.0f, -1.0f,     // 2
            1.0f, 1.0f, -1.0f,      // 3
            -1.0f, 1.0f, -1.0f,     // 0
            -1.0f, 1.0f, -1.0f,     // 0
            -1.0f,  -1.0f, -1.0f,   // 1
            1.0f, -1.0f, -1.0f,     // 2
    };

    private final int program;

    private int positionHandle;
    private int colourHandle;
    private int mMVPMatrixHandle;
    private int textureUniformHandle;
    private int textureCoordHandle;
    private int textureDataHandle;

    private final int vertexCount = boxCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    private final int colourStride = COORDS_PER_COLOUR * 4;
    private final int textureStride = COORDS_PER_TEXTURE * 4;


    public Skybox(OpenGLRenderer renderer, Context context, float zFar) {

        mRenderer = renderer;
        newCubeCoordinates(boxCoords, zFar);
        vertexBuffer = allocateBuffer(boxCoords);
        //colourBuffer = allocateBuffer(colours);
        textureBuffer = allocateBuffer(textureCoords);
        textureDataHandle = TextureHelper.CubeMapTexture(context, R.raw.back, R.raw.left, R.raw.front, R.raw.right, R.raw.bottom, R.raw.top);

        int vertexShader = OpenGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, Shaders.TEXTURE_CUBE_MAP_VERTEX_SHADER);
        int fragmentShader = OpenGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, Shaders.TEXTURE_CUBE_MAP_FRAGMENT_SHADER);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        textureUniformHandle = GLES20.glGetUniformLocation(program, "uTexture");
        mRenderer.checkGLError("glGetUniformLocation");
        textureCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoordinate");
        mRenderer.checkGLError("glGetAttribLocation");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        mRenderer.checkGLError("glGetUniformLocation");

    }

    public void draw(float[] viewMatrix, float[] projectionMatrix)
    {
        GLES20.glUseProgram(program);

        Matrix.MultiplyMM(mMVMatrix, 0, viewMatrix, 0, modelMatrix);

        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glVertexAttribPointer(textureCoordHandle, COORDS_PER_TEXTURE, GLES20.GL_FLOAT, false, textureStride, textureBuffer);
        GLES20.glEnableVertexAttribArray(textureCoordHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureDataHandle);
        GLES20.glUniform1i(textureUniformHandle, 0);


        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        mRenderer.checkGLError("glUniformMatrix4fv");


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(textureCoordHandle);
    }

    private static FloatBuffer allocateBuffer(float[] content)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(content.length * BYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(content);
        fb.position(0);
        return fb;
    }

    private void newCubeCoordinates(float[] boxCoords, float zFar)
    {
        float distance = (float)Math.sqrt(2 * (zFar * zFar)) - zFar;
        Log.d("CUBE", Float.toString(distance));
        for (int i = 0; i < boxCoords.length; i++)
        {
            boxCoords[i] = (boxCoords[i] > 0 ? distance : -distance);
        }
    }

}
