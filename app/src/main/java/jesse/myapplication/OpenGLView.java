package jesse.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;

/**
 * Created by Jesse on 25/10/2017.
 */

public class OpenGLView extends GLSurfaceView{

    OpenGLRenderer mRenderer = new OpenGLRenderer();
    GameLoop mGameLoop;
    float lastX = 0, lastY = 0;

    public OpenGLView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);

        mGameLoop = new GameLoop(this);
        mGameLoop.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float newX = event.getX();
        float newY = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                float deltaX = newX - lastX;
                float deltaY = newY - lastY;
                mRenderer.eyeZ += deltaY / 50;
                mRenderer.upX += deltaX / 50;
                Log.d("TOUCH", "eyeZ " + Float.toString(mRenderer.eyeZ));
                Log.d("TOUCH", "upX " + Float.toString(mRenderer.upX));
                lastX = newX;
                lastY = newY;

                return true;
            case MotionEvent.ACTION_UP:

                return true;
        }

        return super.onTouchEvent(event);
    }
}
