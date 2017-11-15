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

import Units.Matrix4;
import util.RawResourceReader;
import util.ShaderHelper;

import static java.lang.Math.tan;

/**
 * Created by Jesse on 25/10/2017.
 * based on code from tutorial @ https://github.com/learnopengles/Learn-OpenGLES-Tutorials/blob/master/android/AndroidOpenGLESLessons/app/src/main/java/com/learnopengles/android/lesson8/LessonEightRenderer.java
 * avoiding using GL10 which is passed as parameters in favour of newer GLES20 (GLES30+ has higher API level requirements)
 */

public class OpenGLRenderer implements GLSurfaceView.Renderer{

    private Context context;

    private Triangle triangle;

    private Skybox skybox;
    private int skyboxShaderProgram;
    private int skyboxMVPMatrixHandle;
    private int skyboxMVMatrixHandle;
    private int skyboxPositionHandle;
    private int skyboxTextureUniformHandle;
    private int skyboxTextureCoordinateHandle;

    private HeightMap heightMap;
    private int heightmapShaderProgram;
    private int heightmapMVPMatrixHandle;
    private int heightmapMVMatrixHandle;
    private int heightmapPositionHandle;
    private int heightmapNormalHandle;
    private int heightmapTextureUniformHandle;
    private int heightmapTextureCoordinateHandle;

    public Camera camera;

    public int screenX, screenY;
    private final float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mAccumulatedRotation = new float[16];
    private final float[] mSkyboxRotationMatrix = new float[16];
    private final float[] mTempMatrix = new float[16];

    private static float fov = 90.0f;
    private static float near = 0.1f;
    private static float far = 1000.0f;


    public OpenGLRenderer(Context context)
    {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 GL10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //black wipe
        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDisable(GLES20.GL_BLEND);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 0, 0, 0, -5, 0, 1, 0);

        int skyboxVertexShader = loadShader(GLES20.GL_VERTEX_SHADER, Shaders.TEXTURE_CUBE_MAP_VERTEX_SHADER);
        int skyboxFragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, Shaders.TEXTURE_CUBE_MAP_FRAGMENT_SHADER);
        skyboxShaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(skyboxShaderProgram, skyboxVertexShader);
        GLES20.glAttachShader(skyboxShaderProgram, skyboxFragmentShader);
        GLES20.glLinkProgram(skyboxShaderProgram);

        int heightmapVertexShader = loadShader(GLES20.GL_VERTEX_SHADER, Shaders.TEXTURE_CUBE_MAP_VERTEX_SHADER);
        int heightmapFragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, Shaders.TEXTURE_CUBE_MAP_FRAGMENT_SHADER);
        heightmapShaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(heightmapShaderProgram, heightmapVertexShader);
        GLES20.glAttachShader(heightmapShaderProgram, heightmapFragmentShader);
        GLES20.glLinkProgram(heightmapShaderProgram);

        skyboxMVPMatrixHandle = GLES20.glGetUniformLocation(skyboxShaderProgram, "uMVPMatrix");
        skyboxMVMatrixHandle = GLES20.glGetUniformLocation(skyboxShaderProgram, "uMVMatrix");
        skyboxTextureUniformHandle = GLES20.glGetUniformLocation(skyboxShaderProgram, "uTexture");
        skyboxPositionHandle = GLES20.glGetAttribLocation(skyboxShaderProgram, "aPosition");
        skyboxTextureCoordinateHandle = GLES20.glGetAttribLocation(skyboxShaderProgram, "aTextureCoordinate");

        heightmapMVPMatrixHandle = GLES20.glGetUniformLocation(heightmapShaderProgram, "uMVPMatrix");
        heightmapMVMatrixHandle = GLES20.glGetUniformLocation(heightmapShaderProgram, "uMVMatrix");
        heightmapPositionHandle = GLES20.glGetAttribLocation(heightmapShaderProgram, "aPosition");
        heightmapNormalHandle = GLES20.glGetAttribLocation(heightmapShaderProgram, "aNormal");

        //triangle = new Triangle(this);
        skybox = new Skybox(this, context, far);
        //camera = new Camera(0, 0, 0, 0, 0, 0);


        Matrix.setIdentityM(mAccumulatedRotation, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 GL10, int width, int height) {
        screenX = width;
        screenY = height;

        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        float top = (float) Math.tan(fov * Math.PI / 360.0f) * near;
        float bottom = -top;
        float left = ratio * bottom;
        float right = ratio * top;
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        //Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -1, 0, 0, 0, 0, 1, 0);

    }

    @Override
    public void onDrawFrame(GL10 GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(skyboxShaderProgram);

        Matrix.setIdentityM(mModelMatrix, 0);


        /*float[] scratch = new float[16];
        float[] skyboxScratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        mViewMatrix = camera.GetViewMatrixAsArray();
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setRotateM(mSkyboxRotationMatrix, 0, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(skyboxScratch, 0, mMVPMatrix, 0,  mSkyboxRotationMatrix, 0);

        skybox.draw(skyboxScratch);*/
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

    public Camera GetCamera()
    {
        return camera;
    }


}
