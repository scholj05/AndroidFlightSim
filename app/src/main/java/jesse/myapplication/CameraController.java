package jesse.myapplication;

import android.util.Log;

/**
 * Created by Jesse on 17/11/2017.
 */

public class CameraController {
    static NewCamera camera;
    float flightSpeed;
    float pitch;
    float yaw;
    float roll;

    public CameraController(float startSpeed)
    {
        flightSpeed = startSpeed;

    }

    protected static void setCamera(NewCamera Camera)
    {
        CameraController.camera = Camera;
    }

    public void update()
    {
        Log.d("CAMERACONTROLLER", "pre");

        if (camera == null) return;
        Log.d("CAMERACONTROLLER", "update");
        camera.translate(0, 0, flightSpeed);
        camera.pitch(pitch);
        camera.yaw(yaw);
        camera.roll(roll);
    }

    public void updateFlightSpeed(float delta)
    {
        flightSpeed += delta;
    }

    public void updatePitch(float delta)
    {
        pitch += delta;
    }

    public void updateYaw(float delta)
    {
        yaw += delta;
    }

    public void updateRoll(float delta)
    {
        roll += delta;
    }

}
