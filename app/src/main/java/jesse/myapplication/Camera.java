package jesse.myapplication;

import android.util.Log;

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
        position = new Vector3(0, 0, 0);
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
        Log.d("CAMERA", "pitch: " + Float.toString(pitch));
    }

    public void Yaw(float yawRadians)
    {
        Rotate(yawRadians, 0.0f, 1.0f, 0.0f);
        yaw += RadToDeg(yawRadians);
        Log.d("CAMERA", "yaw: " + Float.toString(yaw));
    }

    public void Roll(float rollRadians)
    {
        Rotate(rollRadians, 0.0f, 0.0f, 1.0f);
        roll += RadToDeg(rollRadians);
        Log.d("CAMERA", "roll: " + Float.toString(roll));
    }

    public void Turn(float turnRadians)
    {
        orientation.setRotation(0.0f, 1.0f, 0.0f, turnRadians);
        Rotate(orientation);
    }

    public void Rotate(float angleRadians, float x, float y, float z)
    {
        Quaternion q = new Quaternion(x, y, z, angleRadians);
        Rotate(q);
    }

    public  void Rotate(Quaternion q)
    {
        orientation = q.mul(orientation);
        PrintLog();
    }

    public Vector3 GetForward()
    {
        Quaternion q = orientation.conjugate();
        //Log.d("CAMERA", "q: " + Float.toString(q.x) + ", " + Float.toString(q.y) + ", " + Float.toString(q.z));
        q.mulInplace(new Quaternion(0.0f, 0.0f, -1.0f, 0.0f));
        //Log.d("CAMERA", "getForward: " + Float.toString(q.x) + ", " + Float.toString(q.y) + ", " + Float.toString(q.z));
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

        Vector3 temp = GetForward().mul(movement);
        //Log.d("CAMERA", "temp: " + Float.toString(temp.getX()) + ", " + Float.toString(temp.getY()) + ", " + Float.toString(temp.getZ()));
        position.add(temp);
        //Log.d("CAMERA", "position: " + Float.toString(position.getX()) + ", " + Float.toString(position.getY()) + ", " + Float.toString(position.getZ()));
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

    public float[] GetInverseViewMatrixAsArray()
    {
        Matrix4 viewMatrix = orientation.toMatrix();
        viewMatrix.setTranslation(position.mul(-1.0f));
        viewMatrix.inverse();
        return viewMatrix.getAsArray();
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

    public float RadToDeg(float radian)
    {
        return radian * (180 / (float)Math.PI);
    }

    public float DegToRad(float degree)
    {
        return degree * ((float)Math.PI / 180);
    }


    public void PrintLog()
    {
        float[] matrix = GetViewMatrixAsArray();
        //Log.d("CameraVec", Float.toString(pitch) + ", " + Float.toString(yaw) + ", " + Float.toString(roll));
        Log.d("CameraMatrix",
                Float.toString(matrix[0]) + ", " +
                Float.toString(matrix[1]) + ", " +
                Float.toString(matrix[2]) + ", " +
                Float.toString(matrix[3]) + ", \n" +
                Float.toString(matrix[4]) + ", " +
                Float.toString(matrix[5]) + ", " +
                Float.toString(matrix[6]) + ", " +
                Float.toString(matrix[7]) + ", \n" +
                Float.toString(matrix[8]) + ", " +
                Float.toString(matrix[9]) + ", " +
                Float.toString(matrix[10]) + ", " +
                Float.toString(matrix[11]) + ", \n" +
                Float.toString(matrix[12]) + ", " +
                Float.toString(matrix[13]) + ", " +
                Float.toString(matrix[14]) + ", " +
                Float.toString(matrix[15]) + ", "
        );
    }
}
