package jesse.myapplication;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import Units.Vector2;
import Units.Vector3;

/**
 * Created by Jesse on 12/11/2017.
 */

public class HeightMap {

    static final int SIZE_PER_SIDE = 32;
    static final float MIN_POSITION = -5f;
    static final float POSITION_RANGE = 10f;

    final int[] vbo = new int[1];
    final int[] ibo = new int[1];

    int indexCount;

    static final int BYTES_PER_FLOAT = 4;
    static final int BYTES_PER_SHORT = 2;

    public HeightMap()
    {
        /*try {
            final int floatsPerVertex = POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS
                    + COLOR_DATA_SIZE_IN_ELEMENTS;
            final int xLength = SIZE_PER_SIDE;
            final int yLength = SIZE_PER_SIDE;

            //final float[] heightMapVertexData = new float[xLength * yLength * floatsPerVertex];

            int offset = 0;

            // First, build the data for the vertex buffer
            for (int y = 0; y < yLength ;y++){
                for (int x = 0; x < xLength ;
                x++){
                    final float xRatio = x / (float) (xLength - 1);

                    // Build our heightmap from the top down, so that our triangles are
                    // counter-clockwise.
                    final float yRatio = 1f - (y / (float) (yLength - 1));

                    final float xPosition = MIN_POSITION + (xRatio * POSITION_RANGE);
                    final float yPosition = MIN_POSITION + (yRatio * POSITION_RANGE);
                    // Cheap normal using a derivative of the function.
                    // The slope for X will be 2X, for Y will be 2Y.
                    // Divide by 10 since the position's Z is also divided by 10.
                    final float xSlope = (2 * xPosition) / 10f;
                    final float ySlope = (2 * yPosition) / 10f;

                    // Calculate the normal using the cross product of the slopes.
                    final float[] planeVectorX = {1f, 0f, xSlope};
                    final float[] planeVectorY = {0f, 1f, ySlope};
                    final float[] normalVector = {
                            (planeVectorX[1] * planeVectorY[2]) - (planeVectorX[2] * planeVectorY[1]),
                            (planeVectorX[2] * planeVectorY[0]) - (planeVectorX[0] * planeVectorY[2]),
                            (planeVectorX[0] * planeVectorY[1]) - (planeVectorX[1] * planeVectorY[0])};

                    // Normalize the normal
                    final float length = Matrix.length(normalVector[0], normalVector[1], normalVector[2]);

                    heightMapVertexData[offset++] = normalVector[0] / length;
                    heightMapVertexData[offset++] = normalVector[1] / length;
                    heightMapVertexData[offset++] = normalVector[2] / length;

                    // Add some fancy colors.
                    heightMapVertexData[offset++] = xRatio;
                    heightMapVertexData[offset++] = yRatio;
                    heightMapVertexData[offset++] = 0.5f;
                    heightMapVertexData[offset++] = 1f;


                }
            }

            // Now build the index data
            final int numStripsRequired = yLength - 1;
            final int numDegensRequired = 2 * (numStripsRequired - 1);
            final int verticesPerStrip = 2 * xLength;

            final short[] heightMapIndexData = new short[(verticesPerStrip * numStripsRequired)
                    + numDegensRequired];

            offset = 0;

            for (int y = 0; y < yLength - 1; y++) {      if (y == 0) {
                // Degenerate begin: repeat first vertex
                heightMapIndexData[offset++] = (short) (y * yLength);
            }

                for (int x = 0; x < xLength; x++) {
                    // One part of the strip
                    heightMapIndexData[offset++] = (short) ((y * yLength) + x);
                    heightMapIndexData[offset++] = (short) (((y + 1) * yLength) + x);
                }

                if (y == yLength - 2) {
                    // Degenerate end: repeat last vertex
                    heightMapIndexData[offset++] = (short) (((y + 1) * yLength) + (xLength - 1));
                }
            }

            final FloatBuffer heightMapVertexDataBuffer = ByteBuffer
                    .allocateDirect(heightMapVertexData.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            heightMapVertexDataBuffer.put(heightMapVertexData).position(0);

            final ShortBuffer heightMapIndexDataBuffer = ByteBuffer
                    .allocateDirect(heightMapIndexData.length * BYTES_PER_SHORT).order(ByteOrder.nativeOrder())
                    .asShortBuffer();
            heightMapIndexDataBuffer.put(heightMapIndexData).position(0);

            indexCount = heightMapIndexData.length;

            GLES20.glGenBuffers(1, vbo, 0);
            GLES20.glGenBuffers(1, ibo, 0);

            if (vbo[0] == 0 || ibo[0] == 0) {
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0]);
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, heightMapVertexDataBuffer.capacity()
                        * BYTES_PER_FLOAT, heightMapVertexDataBuffer, GLES20.GL_STATIC_DRAW);

                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
                GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, heightMapIndexDataBuffer.capacity()
                        * BYTES_PER_SHORT, heightMapIndexDataBuffer, GLES20.GL_STATIC_DRAW);

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
            } else {
                //errorHandler.handleError(ErrorType.BUFFER_CREATION_ERROR, &quot;glGenBuffers&quot;);
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }*/

    }

    public void Generate(Vector3 startPos, Vector2 tileSize, int colCount, int rowCount, float minHeight, float maxHeight) {

    }

    public void draw()
    {

        /*GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0]);

        // Bind Attributes
        GLES20.glVertexAttribPointer(positionAttribute, POSITION_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT,
                false, STRIDE, 0);
        GLES20.glEnableVertexAttribArray(positionAttribute);

        GLES20.glVertexAttribPointer(normalAttribute, NORMAL_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT,
                false, STRIDE, POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT);
        GLES20.glEnableVertexAttribArray(normalAttribute);

        GLES20.glVertexAttribPointer(colorAttribute, COLOR_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT,
                false, STRIDE, (POSITION_DATA_SIZE_IN_ELEMENTS + NORMAL_DATA_SIZE_IN_ELEMENTS)
                        * BYTES_PER_FLOAT);
        GLES20.glEnableVertexAttribArray(colorAttribute);

        // Draw
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, indexCount, GLES20.GL_UNSIGNED_SHORT, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        float diffuse;

        if (gl_FrontFacing) {
            diffuse = max(dot(v_Normal, lightVector), 0.0);
        } else {
            diffuse = max(dot(-v_Normal, lightVector), 0.0);
        }*/
    }
}
