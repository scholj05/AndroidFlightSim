package jesse.myapplication;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import util.TextureHelper;

    /**
     * Created by Steve on 17/10/2017.
     */

public class HeightMapNew {
        private static OpenGLRenderer mRenderer;

        Context context;

        private FloatBuffer vertexBuffer;
        private FloatBuffer colorBuffer;
        private FloatBuffer textureBuffer;

        static final int COORDS_PER_VERTEX = 3;
        static final int COORDS_PER_COLOUR = 4;
        static final int COORDS_PER_TEXTURE = 3;
        static final int BYTES_PER_FLOAT = 4;

        static float boxCoords[] = {   // in counterclockwise order:
                // right
                100.0f, 100.0f, -100.0f,      // 3
                0f, 0f, -0f,     // 2
                0f, 0f, -100.0f      // 7

        };

        static float colorCoords[] = {   // in counterclockwise order:
                // right
                1.0f, 1.0f, 1.0f, 1.0f,     // 3
                1.0f, 0f, 0f, 1f,  // 2
                0f, 1.0f, 1.0f, 1f      // 7

        };


        private float[] modelMatrix = new float[16];
        private float[] tempMatrix = new float[16];
        private float[] mvMatrix = new float[16];
        private float[] mvpMatrix = new float[16];

        private final int program;

        private int positionHandle;
        private int colorHandle;
        private int mMVMatrixHandle;
        private int mMVPMatrixHandle;
        private int textureUniformHandle;
        private int textureCoordHandle;
        private int textureDataHandle;

        private final int vertexCount = boxCoords.length / COORDS_PER_VERTEX;
        private final int vertexStride = COORDS_PER_VERTEX * 4;
        private final int colorStride = COORDS_PER_COLOUR * 4;


        public HeightMapNew(OpenGLRenderer renderer, Context context, float zFar) {

            mRenderer = renderer;
            vertexBuffer = allocateBuffer(boxCoords);
            colorBuffer = allocateBuffer(colorCoords);
            int vertexShader = OpenGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, Shaders.per_pixel_vertex_shader_no_text);
            int fragmentShader = OpenGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, Shaders.per_pixel_fragment_shader_no_tex);

            program = GLES20.glCreateProgram();
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);
            GLES20.glLinkProgram(program);

            positionHandle = GLES20.glGetAttribLocation(program, "a_Position");
            mRenderer.checkGLError("glGetAttribLocation");
            colorHandle = GLES20.glGetAttribLocation(program, "a_Color");
            mRenderer.checkGLError("glGetAttribLocation");
            mMVMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVMatrix");
            mRenderer.checkGLError("glGetUniformLocation");
            mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
            mRenderer.checkGLError("glGetUniformLocation");

            Matrix.setIdentityM(modelMatrix, 0);

        }

        public void draw(float[] viewMatrix, float[] projectionMatrix)
        {
            GLES20.glUseProgram(program);

            Matrix.setIdentityM(modelMatrix, 0);
            //Matrix.scaleM(modelMatrix, 0, 100, 100, 100);
            //Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -3.5f);

            Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);//modelViewMatrix
            GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mvMatrix, 0);
            mRenderer.checkGLError("glUniformMatrix4fv");
            Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0);
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
            mRenderer.checkGLError("glUniformMatrix4fv");

            GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
            GLES20.glEnableVertexAttribArray(positionHandle);

            GLES20.glVertexAttribPointer(colorHandle, COORDS_PER_TEXTURE, GLES20.GL_FLOAT, false, colorStride, colorBuffer);
            GLES20.glEnableVertexAttribArray(colorHandle);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

            GLES20.glDisableVertexAttribArray(positionHandle);
            GLES20.glDisableVertexAttribArray(colorHandle);
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