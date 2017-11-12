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
    static final int COORDS_PER_TEXTURE = 2;
    static final int BYTES_PER_FLOAT = 4;

    static float boxCoords[] = {   // in counterclockwise order:
            // back
            -1.0f, 1.0f, -1.0f,     // 0
            -1.0f,  -1.0f, -1.0f,   // 1
            1.0f, -1.0f, -1.0f,     // 2
            1.0f, -1.0f, -1.0f,     // 2
            1.0f, 1.0f, -1.0f,      // 3
            -1.0f, 1.0f, -1.0f,     // 0

            // left
            -1.0f, 1.0f, 1.0f,      // 5
            -1.0f, -1.0f, 1.0f,     // 6
            -1.0f, -1.0f, -1.0f,    // 1
            -1.0f, -1.0f, -1.0f,    // 1
            -1.0f, 1.0f, -1.0f,     // 0
            -1.0f, 1.0f, 1.0f,      // 5

            // front
            1.0f, 1.0f, 1.0f,       // 4
            1.0f, -1.0f, 1.0f,      // 7
            -1.0f, -1.0f, 1.0f,     // 6
            -1.0f, -1.0f, 1.0f,     // 6
            -1.0f, 1.0f, 1.0f,      // 5
            1.0f, 1.0f, 1.0f,       // 4

            // right
            1.0f, 1.0f, -1.0f,      // 3
            1.0f, -1.0f, -1.0f,     // 2
            1.0f, -1.0f, 1.0f,      // 7
            1.0f, -1.0f, 1.0f,      // 7
            1.0f, 1.0f, 1.0f,       // 4
            1.0f, 1.0f, -1.0f,      // 3

            // bottom
            1.0f, -1.0f, 1.0f,      // 7
            1.0f, -1.0f, -1.0f,     // 2
            -1.0f, -1.0f, -1.0f,    // 1
            -1.0f, -1.0f, -1.0f,    // 1
            -1.0f, -1.0f, 1.0f,     // 6
            1.0f, -1.0f, 1.0f,      // 7

            // top
            1.0f, 1.0f, 1.0f,       // 4
            -1.0f, 1.0f, 1.0f,      // 5
            -1.0f, 1.0f, -1.0f,     // 0
            -1.0f, 1.0f, -1.0f,     // 0
            1.0f, 1.0f, -1.0f,      // 3
            1.0f, 1.0f, 1.0f,       // 4


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

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
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


    public Skybox(OpenGLRenderer renderer, Context context) {

        mRenderer = renderer;

        vertexBuffer = allocateBuffer(boxCoords);
        colourBuffer = allocateBuffer(colours);
        textureBuffer = allocateBuffer(textureCoords);

        int vertexShader = OpenGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, Shaders.COLOUR_VERTEX_SHADER);
        int fragmentShader = OpenGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, Shaders.COLOUR_FRAGMENT_SHADER_DYNAMIC);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        textureDataHandle = TextureHelper.loadTexture(context, R.raw.back);
    }

    public void draw(float[] mvpMatrix)
    {
        GLES20.glUseProgram(program);

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        colourHandle = GLES20.glGetAttribLocation(program, "vColour");
        GLES20.glEnableVertexAttribArray(colourHandle);

        //textureUniformHandle = GLES20.glGetUniformLocation(program, "uTexture");
        //mRenderer.checkGLError("glGetUniformLocation");
        //textureCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoordinate");

        //GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //GLES20.glBindTexture(GL_TEXTURE_2D, textureDataHandle);
        //GLES20.glUniform1i(textureUniformHandle, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        mRenderer.checkGLError("glGetUniformLocation");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        mRenderer.checkGLError("glUniformMatrix4fv");

        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        GLES20.glVertexAttribPointer(colourHandle, COORDS_PER_COLOUR, GLES20.GL_FLOAT, false, colourStride, colourBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colourHandle);
    }

    public static FloatBuffer allocateBuffer(float[] content)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(content.length * BYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(content);
        fb.position(0);
        return fb;
    }


}
