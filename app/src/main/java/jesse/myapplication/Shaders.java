package jesse.myapplication;

/**
 * Created by Jesse on 2/11/2017.
 */

public class Shaders {

    public static final String COLOUR_FRAGMENT_SHADER_STATIC =
                    "precision mediump float;" +
                    "uniform vec4 vColour;" +
                    "void main() {" +
                    "  gl_FragColor = vColour;" +
                    "}";

    public static final String COLOUR_FRAGMENT_SHADER_DYNAMIC =
            "precision mediump float;" +
                    "varying vec4 aColour;" +
                    "void main() {" +
                    "  gl_FragColor = aColour;" +
                    "}";

    public static final String COLOUR_VERTEX_SHADER =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec4 vColour;" +
                    "varying vec4 aColour;" +
                    "void main() {" +
                    "aColour = vColour;" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    public static final String VERTEX_SHADER =
                    "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";




}
