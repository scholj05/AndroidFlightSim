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
    CameraController camera;// = mRenderer.GetCamera();
    GameLoop mGameLoop;
    float touchX = 0, touchY = 0;
    boolean rotateOrTranslate;

    public OpenGLView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mRenderer = new OpenGLRenderer(context);
        camera = new CameraController(0);
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
                camera.updateRoll((event.getX() - touchX) / 10f);
                camera.updatePitch((event.getY() - touchY) / 10f);
            }
            else
            {
               camera.updateYaw((event.getX() - touchX) / 10f);
               camera.updateFlightSpeed((event.getY() - touchY) / 100f);
            }


            touchX = event.getX();
            touchY = event.getY();
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            camera.stop();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent evt) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:   // Decrease Y-rotational speed
                mRenderer.camera.yaw(-1);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:  // Increase Y-rotational speed
                mRenderer.camera.yaw(1);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:     // Decrease X-rotational speed
                mRenderer.camera.pitch(1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:   // Increase X-rotational speed
                mRenderer.camera.pitch(-1);
                break;
            case KeyEvent.KEYCODE_W:           // Zoom out (decrease z)
                mRenderer.camera.translate(0, 0, 0.1f);
                break;
            case KeyEvent.KEYCODE_S:           // Zoom in (increase z)
                mRenderer.camera.translate(0, 0, -0.1f);
                break;
        }
        return true;  // Event handled
    }

    public CameraController getCamera()
    {
        return camera;
    }

}
