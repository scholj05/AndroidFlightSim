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

    public static final String TEXTURE_VERTEX_SHADER =
                    "uniform mat4 uMVPMatrix;\n" +
                    "uniform mat4 uMVMatrix;\n" +
                    " \n" +
                    "attribute vec4 aPosition;\n" +
                    "attribute vec4 aColour;\n" +
    //              "attribute vec3 aNormal;\n" +
                    "attribute vec2 aTexCoordinate;\n" +
                    " \n" +
                    "varying vec3 vPosition;\n" +
                    "varying vec4 vColour;\n" +
    //              "varying vec3 vNormal;\n" +
                    "varying vec2 vTexCoordinate;\n" +
                    " \n" +
                    "void main()\n" +
                    "{\n" +
                    "    vPosition = vec3(uMVMatrix * aPosition);\n" +
                    "    vColour = aColour;\n" +
                    "    vTexCoordinate = aTexCoordinate;\n" +
    //              "    vNormal = vec3(uMVMatrix * vec4(aNormal, 0.0));\n" +
                    "    glPosition = uMVPMatrix * aPosition;\n" +
                    "}";

    public static final String TEXTURE_FRAGMENT_SHADER =
                    "precision mediump float;\n" +
//                    "uniform vec3 u_LightPos;\n" +
                    "uniform sampler2D uTexture;\n" +
                    " \n" +
                    "varying vec3 vPosition;\n" +
                    "varying vec4 vColour;\n" +
//                    "varying vec3 vNormal;\n" +
                    "varying vec2 vTexCoordinate;\n" +
                    " \n" +
                    "void main()\n" +
                    "{\n" +
//                    "    float distance = length(u_LightPos - v_Position);\n" +
//                    "    vec3 lightVector = normalize(u_LightPos - v_Position);\n" +
//                    "    float diffuse = max(dot(v_Normal, lightVector), 0.0);\n" +
//                    "    diffuse = diffuse * (1.0 / (1.0 + (0.10 * distance)));\n" +
//                    "    diffuse = diffuse + 0.3;\n" +
//                    "    gl_FragColor = (vColour * diffuse * texture2D(uTexture, vTexCoordinate));\n" +
                      "    gl_FragColor = (vColour * texture2D(uTexture, vTexCoordinate));\n" +
                    "  }";




}
