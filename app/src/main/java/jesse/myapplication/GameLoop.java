package jesse.myapplication;

import android.util.Log;

/**
 * Created by Jesse on 7/11/2017.
 */

public class GameLoop extends Thread
{
    OpenGLView openGLView;

    public GameLoop(OpenGLView openGLView)
    {
        this.openGLView = openGLView;
    }

    @Override
    public void run()
    {
        while(true)
        {
            //Log.d("TAG", "GAMELOOP");
            openGLView.requestRender();


            try
            {
                Thread.sleep(16);
            }
            catch(InterruptedException exc)
            {
                exc.printStackTrace();
            }
        }
    }
}
