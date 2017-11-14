package jesse.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;

/**
 * Created by Jesse on 25/10/2017.
 */

public class OpenGLView extends GLSurfaceView{

    OpenGLRenderer mRenderer;// = new OpenGLRenderer();
    Camera camera;// = mRenderer.GetCamera();
    GameLoop mGameLoop;
    float touchX = 0, touchY = 0;
    boolean rotateOrTranslate;

    public OpenGLView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mRenderer = new OpenGLRenderer(context);
        camera = mRenderer.GetCamera();
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);

        mGameLoop = new GameLoop(this);
        mGameLoop.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            touchX = event.getX();
            touchY = event.getY();

            rotateOrTranslate = touchX > mRenderer.screenX / 2;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE)
        {


            if (rotateOrTranslate)
            {
                mRenderer.camera.Roll(mRenderer.camera.DegToRad((event.getX() - touchX)/2f));
                mRenderer.camera.Pitch(mRenderer.camera.DegToRad((event.getY() - touchY)/2f));
            }
            else
            {
                mRenderer.camera.Yaw(mRenderer.camera.DegToRad((event.getX() - touchX)/2f));
                mRenderer.camera.MoveForward((event.getY() - touchY)/2f);
            }


            touchX = event.getX();
            touchY = event.getY();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent evt) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:   // Decrease Y-rotational speed
                mRenderer.camera.Yaw(-1);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:  // Increase Y-rotational speed
                mRenderer.camera.Yaw(1);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:     // Decrease X-rotational speed
                mRenderer.camera.Pitch(1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:   // Increase X-rotational speed
                mRenderer.camera.Pitch(-1);
                break;
            case KeyEvent.KEYCODE_W:           // Zoom out (decrease z)
                mRenderer.camera.MoveForward(0.1f);
                break;
            case KeyEvent.KEYCODE_S:           // Zoom in (increase z)
                mRenderer.camera.MoveForward(-0.1f);
                break;
        }
        return true;  // Event handled
    }
}
