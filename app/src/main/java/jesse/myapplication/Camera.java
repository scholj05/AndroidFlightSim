package jesse.myapplication;

import java.util.Vector;

import Units.Matrix4;
import Units.Quaternion;
import Units.Vector3;

/**
 * Created by Jesse on 11/11/2017.
 */

public class Camera {

    private static OpenGLRenderer mRenderer;

    private float pitch, yaw, roll;
    private Vector3 position;
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
        orientation.setRotation(0.0f, 1.0f, 0.0f, turnRadians);
        Rotate(orientation);
    }

    public void Rotate(float angleRadians, float x, float y, float z)
    {
        Quaternion q = new Quaternion(x, y, z, angleRadians);
    }

    public  void Rotate(Quaternion q)
    {
        orientation = q.mul(orientation);
    }

    public Vector3 GetForward()
    {
        Quaternion q = orientation.conjugate();
        q.mulInplace(new Quaternion(0.0f, 0.0f, -1.0f, 0.0f));
        return new Vector3(q.x, q.y, q.z);
    }

    public Vector3 GetLeft()
    {
        Quaternion q = orientation.conjugate();
        q.mulInplace(new Quaternion(-1.0f, 0.0f, 0.0f, 0.0f));
        return new Vector3(q.x, q.y, q.z);
    }

    public Vector3 GetUp()
    {
        Quaternion q = orientation.conjugate();
        q.mulInplace(new Quaternion(0.0f, 1.0f, 0.0f, 0.0f));
        return new Vector3(q.x, q.y, q.z);
    }

    public void MoveForward(float movement)
    {
        position.add(GetForward().mul(movement));
    }

    public void MoveLeft(float movement)
    {
        position.add(GetLeft().mul(movement));
    }

    public void MoveUp(float movement)
    {
        position.add(GetUp().mul(movement));
    }

    public Matrix4 GetViewMatrix()
    {
        Matrix4 viewMatrix = orientation.toMatrix();
        viewMatrix.setTranslation(position.mul(-1.0f));
        return viewMatrix;
    }

    public float[] GetViewMatrixAsArray()
    {
        Matrix4 viewMatrix = orientation.toMatrix();
        viewMatrix.setTranslation(position.mul(-1.0f));
        return viewMatrix.getAsArray();
    }

    public Matrix4 GetInverseViewMatrix()
    {
        Matrix4 viewMatrix = orientation.toMatrix();
        viewMatrix.setTranslation(position.mul(-1.0f));
        viewMatrix.inverse();
        return viewMatrix;
    }

    public Vector3 GetEulerAngles()
    {
        float[] matrix = GetViewMatrixAsArray();

        if (matrix[0] == 1.0f || matrix[0] == -1.0f)
        {
            yaw = (float)Math.atan2((double)matrix[2], (double)matrix[11]);
            pitch = 0;
            roll = 0;
        }
        else
        {
            yaw = (float)Math.atan2((double)-matrix[8], (double)matrix[0]);
            pitch = (float)Math.asin((double)matrix[4]);
            roll = (float)Math.atan2((double)-matrix[6], (double)matrix[5]);
        }
        return new Vector3(RadToDeg(pitch), RadToDeg(yaw), RadToDeg(roll));
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
