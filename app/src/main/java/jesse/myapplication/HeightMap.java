package jesse.myapplication;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import org.xml.sax.ErrorHandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import Units.Vector2;
import Units.Vector3;
import util.ShaderHelper;

/**
 * Created by Steven on 12/11/2017.
 */

public class HeightMap {

    private static OpenGLRenderer mRenderer;
    Context context;

    /** Retain the most recent delta for touch events. */
    // These still work without volatile, but refreshes are not guaranteed to
    // happen.
    public volatile float deltaX;
    public volatile float deltaY;

    private Mountain[] mountains;

    public HeightMap(OpenGLRenderer renderer, Context context)
    {
        this.context = context;
        mRenderer = renderer;

        mountains = new Mountain[50];

        for(int i=0;i<mountains.length - 1;i++)
        {
            mountains[i] = new Mountain(renderer, context, new Vector3(i * 100, 0f, i * 100), 1 , new Vector3(0f, 0.1f, 0f), 10);
        }

        mountains[mountains.length - 1] = new Mountain(renderer, context, new Vector3(-100f, -200, -100), 1000, new Vector3(0f, 0f, 1f), 1000);
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

    public void Generate(Vector3 startPos, Vector2 tileSize, int colCount, int rowCount, float minHeight, float maxHeight) {

    }

    public void draw(float[] viewMatrix, float[] projectionMatrix)
    {
        for(int i=0;i<mountains.length;i++)
        {
            if(mountains[i] != null) mountains[i].draw(viewMatrix, projectionMatrix);
        }
    }
}
