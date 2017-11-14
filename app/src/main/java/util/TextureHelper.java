package util;

/**
 * Created by Jesse on 1/11/2017.  Source http://www.learnopengles.com/
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;

import jesse.myapplication.R;

public class TextureHelper {

    public static int loadTexture(final Context context, final int resourceId)

    {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error generating texture name.");
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;	// No pre-scaling

        // Read in the resource
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle();

        return textureHandle[0];
    }

    public static int CubeMapTexture(Context context, int back, int left, int front, int right, int bottom, int top)
    {
        ByteBuffer cubeBuffer = null;
        int[] cubeTexture = new int[1];

        GLES20.glGenTextures(1, cubeTexture, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, cubeTexture[0]);

        Bitmap image = null;
        image = BitmapFactory.decodeResource(context.getResources(), R.raw.right);
        cubeBuffer = ByteBuffer.allocateDirect(image.getHeight() * image.getHeight() * 4);
        image.copyPixelsToBuffer(cubeBuffer);
        cubeBuffer.position(0);
        GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
                0,
                GLES20.GL_RGBA,
                image.getWidth(),
                image.getHeight(),
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                cubeBuffer);
        cubeBuffer = null;
        image.recycle();

        image = BitmapFactory.decodeResource(context.getResources(), R.raw.left);
        cubeBuffer = ByteBuffer.allocateDirect(image.getHeight() * image.getHeight() * 4);
        image.copyPixelsToBuffer(cubeBuffer);
        cubeBuffer.position(0);
        GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
                0,
                GLES20.GL_RGBA,
                image.getWidth(),
                image.getHeight(),
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                cubeBuffer);
        cubeBuffer = null;
        image.recycle();

        image = BitmapFactory.decodeResource(context.getResources(), R.raw.top);
        cubeBuffer = ByteBuffer.allocateDirect(image.getHeight() * image.getHeight() * 4);
        image.copyPixelsToBuffer(cubeBuffer);
        cubeBuffer.position(0);
        GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
                0,
                GLES20.GL_RGBA,
                image.getWidth(),
                image.getHeight(),
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                cubeBuffer);
        cubeBuffer = null;
        image.recycle();

        image = BitmapFactory.decodeResource(context.getResources(), R.raw.bottom);
        cubeBuffer = ByteBuffer.allocateDirect(image.getHeight() * image.getHeight() * 4);
        image.copyPixelsToBuffer(cubeBuffer);
        cubeBuffer.position(0);
        GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
                0,
                GLES20.GL_RGBA,
                image.getWidth(),
                image.getHeight(),
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                cubeBuffer);
        cubeBuffer = null;
        image.recycle();

        image = BitmapFactory.decodeResource(context.getResources(), R.raw.back);
        cubeBuffer = ByteBuffer.allocateDirect(image.getHeight() * image.getHeight() * 4);
        image.copyPixelsToBuffer(cubeBuffer);
        cubeBuffer.position(0);
        GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
                0,
                GLES20.GL_RGBA,
                image.getWidth(),
                image.getHeight(),
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                cubeBuffer);
        cubeBuffer = null;
        image.recycle();

        image = BitmapFactory.decodeResource(context.getResources(), R.raw.front);
        cubeBuffer = ByteBuffer.allocateDirect(image.getHeight() * image.getHeight() * 4);
        image.copyPixelsToBuffer(cubeBuffer);
        cubeBuffer.position(0);
        GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z,
                0,
                GLES20.GL_RGBA,
                image.getWidth(),
                image.getHeight(),
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                cubeBuffer);
        cubeBuffer = null;
        image.recycle();

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        return cubeTexture[0];
    }
}