package jesse.myapplication;

import android.opengl.Matrix;
import android.util.Log;

import Units.Matrix4;
import Units.Quaternion;
import Units.Vector3;
import Units.Vector4;

/**
 * Created by Jesse on 15/11/2017.
 */

public class NewCamera {

    Vector3 mPosition;
    Quaternion mOrientation;

    public NewCamera(float posX, float posY, float posZ, float rotX, float rotY, float rotZ)
    {
        mPosition = new Vector3(posX, posY, posZ);
        mOrientation = new Quaternion(0, 0, 0, 1);
        pitch(rotX);
        yaw(rotY);
        roll(rotZ);
    }

    public Vector3 position()
    {
        return mPosition;
    }

    public Quaternion orientation()
    {
        return mOrientation;
    }

    public float[] viewMatrix()
    {
        Log.d("NEWCAMERA", "orientation: " + Float.toString(mOrientation.x) + ", " + Float.toString(mOrientation.y) + ", " + Float.toString(mOrientation.z) + ", " + Float.toString(mOrientation.w));
        Log.d("NEWCAMERA", "position: " + Float.toString(mPosition.x) + ", " + Float.toString(mPosition.y) + ", " + Float.toString(mPosition.z));


        float[] view = new float[16];
        Matrix.setIdentityM(view, 0);
        Matrix.translateM(view, 0, mPosition.x, mPosition.y, mPosition.z);
        //PrintLog("view", view);

        float[] hold = new float[16];
        Matrix.setIdentityM(hold, 0);
        Matrix.multiplyMM(hold, 0, view, 0, mOrientation.toMatrix().getAsArray(), 0);
        Matrix.invertM(view, 0, hold, 0);
        PrintLog("view", view);
        return view;
    }

    public void translate(Vector3 v)
    {
        Quaternion vQuat = new Quaternion(v, 1);
        vQuat.mulInplace(mOrientation);
        mPosition = mPosition.add(new Vector3(vQuat.x, vQuat.y, vQuat.z));
    }

    public void translate(float x, float y, float z)
    {
        translate(new Vector3(x, y, z));
    }

    public void rotate(float angle, Vector3 axis)
    {
        mOrientation = mOrientation.mul(mOrientation.angleAxis(angle, mOrientation.mul(axis)));
    }

    public void rotate(float angle, float x, float y, float z)
    {
        rotate(angle, new Vector3(x, y, z));
    }

    public void pitch(float angle)
    {
        rotate(angle, 1.0f, 0.0f, 0.0f);
    }

    public void yaw(float angle)
    {
        rotate(angle, 0.0f, 1.0f, 0.0f);
    }

    public void roll(float angle)
    {
        rotate(angle, 0.0f, 0.0f, 1.0f);
    }

    public float radToDeg(float radian)
    {
        return radian * (180 / (float)Math.PI);
    }

    public float degToRad(float degree)
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
