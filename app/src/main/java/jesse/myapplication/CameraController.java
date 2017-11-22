package jesse.myapplication;

import android.util.Log;

/**
 * Created by Jesse on 17/11/2017.
 */

public class CameraController {
    static NewCamera camera;
    float flightSpeed;
    float baseSpeed;
    float pitch;
    float yaw;
    float roll;

    public CameraController(float startSpeed)
    {
        flightSpeed = baseSpeed = startSpeed;

    }

    protected static void setCamera(NewCamera Camera)
    {
        CameraController.camera = Camera;
    }

    public void update()
    {
        if (camera == null) return;
        camera.pitch(pitch);
        camera.yaw(yaw);
        camera.roll(roll);
        //camera.translate(0, 0, flightSpeed);
    }

    public void updateFlightSpeed(float delta)
    {
        delta = delta;
        //flightSpeed = ((flightSpeed + delta) <= baseSpeed ? flightSpeed + delta : flightSpeed);
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

    public void stop()
    {
        pitch = yaw = roll = 0;
    }

}
