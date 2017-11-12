package jesse.myapplication;

import android.content.Context;
import android.opengl.GLES11;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import util.RawResourceReader;
import util.ShaderHelper;

import static java.lang.Math.tan;

/**
 * Created by Jesse on 25/10/2017.
 * based on code from tutorial @ https://github.com/learnopengles/Learn-OpenGLES-Tutorials/blob/master/android/AndroidOpenGLESLessons/app/src/main/java/com/learnopengles/android/lesson8/LessonEightRenderer.java
 * avoiding using GL10 which is passed as parameters in favour of newer GLES20 (GLES30+ has higher API level requirements)
 */

public class OpenGLRenderer implements GLSurfaceView.Renderer{

    private Triangle triangle;
    private Skybox skybox;

    private Camera camera;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mSkyboxRotationMatrix = new float[16];

    public float eyeX = 0.0f;
    public float eyeY = 0.0f;
    public float eyeZ = 1.5f;
    public float lookX = 0.0f;
    public float lookY = 0.0f;
    public float lookZ = -5.0f;
    public float upX = 0.0f;
    public float upY = 1.0f;
    public float upZ = 0.0f;

    public OpenGLRenderer()
    {
    }

    @Override
    public void onSurfaceCreated(GL10 GL10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //black wipe

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);

        triangle = new Triangle(this);
        skybox = new Skybox(this);
    }

    @Override
    public void onSurfaceChanged(GL10 GL10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        float fov = 90.0f;
        float near = 1.0f;
        float far = 1000.0f;
        float top = (float) Math.tan(fov * Math.PI / 360.0f) * near;
        float bottom = -top;
        float left = ratio * bottom;
        float right = ratio * top;
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 GL10) {
        float[] scratch = new float[16];
        float[] skyboxScratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setRotateM(mSkyboxRotationMatrix, 0, eyeY, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(skyboxScratch, 0, mMVPMatrix, 0, mSkyboxRotationMatrix, 0);

        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        triangle.draw(scratch);
        skybox.draw(skyboxScratch);
    }

    public static int loadShader(int type, String ShaderCode)
    {
        int shader = GLES20.glCreateShader(type);   // create new shader
        GLES20.glShaderSource(shader, ShaderCode);  // add the shader code
        GLES20.glCompileShader(shader);             // Compile shader code
        return shader;
    }

    public void checkGLError(String gl)
    {
        int error = GLES20.glGetError();
        if (error != GLES20.GL_NO_ERROR)
        {
            Log.e("GLERROR", gl + ": glError " + error);
            throw new RuntimeException(gl + ": glError " + error);
        }
    }


}
