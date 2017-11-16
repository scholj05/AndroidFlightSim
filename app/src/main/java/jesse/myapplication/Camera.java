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

    public Camera(float positionX, float positionY, float positionZ, float rotationX, float rotationY, float rotationZ)
    {
        pitch = yaw = roll = 0.0f;
        Vector3 angles = new Vector3(DegToRad(rotationX), DegToRad(rotationY), DegToRad(rotationZ));
        orientation = new Quaternion(angles.x, angles.y, angles.z, 0);
        position = new Vector3(0.0f, 0.0f, 0.0f);
        MoveLeft(positionX);
        MoveUp(positionY);
        MoveForward(positionZ);


    }

    public void SetRenderer(OpenGLRenderer renderer)
    {
        mRenderer = renderer;
    }

    public void Pitch(float pitchRadians)
    {
        Rotate(pitchRadians,  new Vector3(1.0f, 0.0f, 0.0f));
        pitch += RadToDeg(pitchRadians);
        //Log.d("CAMERA", "pitch: " + Float.toString(pitch));
    }

    public void Yaw(float yawRadians)
    {
        Rotate(yawRadians,  new Vector3(0.0f, 1.0f, 0.0f));
        yaw += RadToDeg(yawRadians);
        //Log.d("CAMERA", "yaw: " + Float.toString(yaw));
    }

    public void Roll(float rollRadians)
    {
        Rotate(rollRadians, new Vector3(0.0f, 0.0f, 1.0f));
        roll += RadToDeg(rollRadians);
        //Log.d("CAMERA", "roll: " + Float.toString(roll));
    }

    public void Turn(float turnRadians)
    {
        orientation.setRotation(0.0f, 1.0f, 0.0f, turnRadians);
        Rotate(orientation);
    }

    public void Rotate(float angleRadians, Vector3 axis)
    {
        Quaternion q = new Quaternion().angleAxis(angleRadians, axis);
        Rotate(q);
    }

    public  void Rotate(Quaternion q)
    {

        orientation = q.mul(orientation);
        //PrintLog();
    }

    public Vector3 GetForward()
    {
        //Log.d("CAMERA", "orientation: " + Float.toString(orientation.x) + ", " + Float.toString(orientation.y) + ", " + Float.toString(orientation.z) + ", " + Float.toString(orientation.w));
        Quaternion q = orientation.conjugate();
        //Log.d("CAMERA", "q: " + Float.toString(q.x) + ", " + Float.toString(q.y) + ", " + Float.toString(q.z));
        Vector3 forward = q.mul(new Vector3(0.0f, 0.0f, -1.0f));
        //Log.d("CAMERA", "getForward: " + Float.toString(q.x) + ", " + Float.toString(q.y) + ", " + Float.toString(q.z));
        return forward;
    }

    public Vector3 GetLeft()
    {
        Quaternion q = orientation.conjugate();
        Vector3 left = q.mul(new Vector3(-1.0f, 0.0f, 0.0f));
        return left;
    }

    public Vector3 GetUp()
    {
        Quaternion q = orientation.conjugate();
        Vector3 up = q.mul(new Vector3(-1.0f, 0.0f, 0.0f));
        return up;
    }

    public void MoveForward(float movement)
    {
        Log.d("CAMERA", "______________________________");
        Vector3 forward = GetForward();
        Log.d("CAMERA", "getForward: " + Float.toString(forward.x) + ", " + Float.toString(forward.y) + ", " + Float.toString(forward.z));
        Log.d("CAMERA", "movement: " + Float.toString(movement));
        forward.mulInplace(movement);
        Log.d("CAMERA", "newForward: " + Float.toString(forward.x) + ", " + Float.toString(forward.y) + ", " + Float.toString(forward.z));
        Log.d("CAMERA", "position: " + Float.toString(position.x) + ", " + Float.toString(position.y) + ", " + Float.toString(position.z));
        position.addNoCopy(forward);
        Log.d("CAMERA", "newPosition: " + Float.toString(position.x) + ", " + Float.toString(position.y) + ", " + Float.toString(position.z));
    }

    public void MoveLeft(float movement)
    {
        Vector3 left = GetLeft();
        left.mulInplace(movement);
        position.addNoCopy(left);
    }

    public void MoveUp(float movement)
    {
        Vector3 up = GetUp();
        up.mulInplace(movement);
        position.addNoCopy(up);
    }

    public Matrix4 GetViewMatrix()
    {
        Matrix4 viewMatrix = orientation.toMatrix();
        viewMatrix.setTranslation(position.mul(-1.0f));
        return viewMatrix;
    }

    public float[] GetViewMatrixAsArray()
    {
        Matrix4 translatedOrientation = orientation.toMatrix();
        return translatedOrientation.translate(position);
        /*Matrix4 viewMatrix = orientation.toMatrix();
        //PrintLog("orientation: ", viewMatrix.getAsArray());
        viewMatrix.setTranslation(position.mul(-1.0f));
        //PrintLog("orientationAfter: ", viewMatrix.getAsArray());
        return viewMatrix.getAsArray();*/
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


    public void PrintLog(String title, float[] matrix)
    {
        //Log.d("CameraVec", Float.toString(pitch) + ", " + Float.toString(yaw) + ", " + Float.toString(roll));
        Log.d(title,
           Float.toString(matrix[0]) + ", " +
                Float.toString(matrix[1]) + ", " +
                Float.toString(matrix[2]) + ", " +
                Float.toString(matrix[3]) + ", "
        );
        Log.d(title + "2",
                Float.toString(matrix[4]) + ", " +
                Float.toString(matrix[5]) + ", " +
                Float.toString(matrix[6]) + ", " +
                Float.toString(matrix[7]) + ", "
        );
        Log.d(title + "3",
                Float.toString(matrix[8]) + ", " +
                        Float.toString(matrix[9]) + ", " +
                        Float.toString(matrix[10]) + ", " +
                        Float.toString(matrix[11]) + ", "
        );
        Log.d(title + "4",
                Float.toString(matrix[12]) + ", " +
                        Float.toString(matrix[13]) + ", " +
                        Float.toString(matrix[14]) + ", " +
                        Float.toString(matrix[15]) + ", "
        );
    }
}
