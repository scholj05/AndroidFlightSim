package jesse.myapplication;

import java.util.Vector;

import Units.Quaternion;
import Units.Vector3;

/**
 * Created by Jesse on 11/11/2017.
 */

public class Camera {

    private static OpenGLRenderer mRenderer;

    private float pitch, yaw, roll;
    private float[] position = new float[3];
    private Quaternion orientation;

    public Camera(float x, float y, float z)
    {
        pitch = yaw = roll = 0.0f;
        orientation = new Quaternion(x, y, z, 0);
        MoveLeft(x);
        MoveUp(y);
        MoveForward(z);
    }

    public void SetRenderer(OpenGLRenderer renderer)
    {
        mRenderer = renderer;
    }

    public void Pitch(float pitchRadians)
    {
        Rotate(pitchRadians, 1.0f, 0.0f, 0.0f);
        pitch += RadToDeg(pitchRadians);
    }

    public void Yaw(float yawRadians)
    {
        Rotate(yawRadians, 0.0f, 1.0f, 0.0f);
        yaw += RadToDeg(yawRadians);
    }

    public void Roll(float rollRadians)
    {
        Rotate(rollRadians, 0.0f, 0.0f, 1.0f);
        roll += RadToDeg(rollRadians);
    }

    public void Turn(float turnRadians)
    {
        //Quaternion q = new Quaternion(turnRadians, orientation.dot() * new Vector3(0.0f, 1.0f, 0.0f));
        //q.
    }

    public void Rotate(float angleRadians, float x, float y, float z)
    {

    }

    public  void Rotate(float[] rotationQuat)
    {

    }

    public float[] GetForward()
    {
        return new float[0];
    }

    public float[] GetLeft()
    {
        return new float[0];
    }

    public float[] GetUp()
    {
        return new float[0];
    }

    public void MoveForward(float movement)
    {

    }

    public void MoveLeft(float movement)
    {

    }

    public void MoveUp(float movement)
    {

    }

    public float[] GetViewMatrix()
    {
        return new float[0];
    }

    public float[] GetInverseViewMatrix()
    {
        return new float[0];
    }

    public float[] GetEulerAngles()
    {
        return new float[0];
    }

    private float RadToDeg(float radian)
    {
        return radian * (180 / (float)Math.PI);
    }

    private float DegToRad(float degree)
    {
        return degree * ((float)Math.PI / 180);
    }
}
