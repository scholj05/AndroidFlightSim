package jesse.myapplication;

import android.util.Log;

import Units.Vector2;
import Units.Vector3;

/**
 * Created by Jesse on 7/11/2017.
 */

public class GameLoop extends Thread
{
    OpenGLView openGLView;
    private HeightMap heightMap;

    public GameLoop(OpenGLView openGLView)
    {

        this.openGLView = openGLView;
        heightMap = new HeightMap();

        Vector3 startPos = new Vector3(0, 0, 0);
        Vector2 tileSize = new Vector2(100, 100);
        int colCount = 128, rowCount = 128, minHeight = 0, maxHeight = 1000;
        heightMap.Generate(startPos, tileSize, colCount, rowCount, minHeight, maxHeight);
    }

    @Override
    public void run()
    {
        while(true)
        {
            //Log.d("TAG", "GAMELOOP");
            openGLView.requestRender();
            if (openGLView.getCamera() != null)
                openGLView.getCamera().update();
                //heightMap.draw();

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
