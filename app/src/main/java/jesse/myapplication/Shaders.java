package jesse.myapplication;

/**
 * Created by Jesse on 2/11/2017.
 */

public class Shaders {

    public static final String COLOUR_FRAGMENT_SHADER_STATIC =
                    "precision mediump float;" +
                    "uniform vec4 uColour;" +
                    "void main() {" +
                    "  gl_FragColor = uColour;" +
                    "}";

    public static final String COLOUR_FRAGMENT_SHADER_DYNAMIC =
                    "precision mediump float;" +
                    "varying vec4 vColour;" +
                    "void main() {" +
                    "  gl_FragColor = vColour;" +
                    "}";

    public static final String COLOUR_VERTEX_SHADER =
                    "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 aPosition;" +
                    "attribute vec4 aColour;" +
                    "varying vec4 vColour;" +
                    "void main() {" +
                    "vColour = aColour;" +
                    "  gl_Position = uMVPMatrix * aPosition;" +
                    "}";

    public static final String VERTEX_SHADER =
                    "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 aPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * aPosition;" +
                    "}";

    public static final String TEXTURE_VERTEX_SHADER_BROKEN =
                    "uniform mat4 uMVPMatrix;\n" +
                    "uniform mat4 uMVMatrix;\n" +
                    " \n" +
                    "attribute vec4 aPosition;\n" +
                    "attribute vec4 aColour;\n" +
                    "attribute vec3 aNormal;\n" +
                    "attribute vec2 aTexCoordinate;\n" +
                    " \n" +
                    "varying vec3 vPosition;\n" +
                    "varying vec4 vColour;\n" +
                    "varying vec3 vNormal;\n" +
                    "varying vec2 vTexCoordinate;\n" +
                    " \n" +
                    "void main()\n" +
                    "{\n" +
                    "    vPosition = vec3(uMVMatrix * aPosition);\n" +
                    "    vColour = aColour;\n" +
                    "    vTexCoordinate = aTexCoordinate;\n" +
                    "    vNormal = vec3(uMVMatrix * vec4(aNormal, 0.0));\n" +
                    "    gl_Position = uMVPMatrix * aPosition;\n" +
                    "}";

    public static final String TEXTURE_FRAGMENT_SHADER_BROKEN =
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


    public static final String TEXTURE_VERTEX_SHADER =
            "uniform mat4 uMVPMatrix;\n" +
                    " \n" +
                    "in vec4 vPosition;\n" +
                    "in vec2 texCoord;\n" +
                    " \n" +
                    "out vec2 texCoordV;\n" +
                    " \n" +
                    "void main() {\n" +
                    " \n" +
                    "    texCoordV = texCoord;\n" +
                    "    gl_Position = uMVPMatrix * position;\n" +
                    "}";

    public static final String TEXTURE_CUBE_MAP_VERTEX_SHADER =
                    "attribute vec4 aPosition;" +
                    "uniform mat4 uMVPMatrix;" +
                    "varying vec3 vTexCoords;" +
                    "attribute vec3 aTexCoordinate;" +
                    "void main()" +
                    "{" +
                        "vTexCoords = aTexCoordinate;" +
                        "gl_Position = uMVPMatrix * aPosition;" +
                    "}";

    public static final String TEXTURE_CUBE_MAP_FRAGMENT_SHADER =
                    "uniform samplerCube uTexture;" +
                    "varying vec3 vTexCoords;" +
                    "void main()" +
                    "{" +
                    "gl_FragColor = textureCube(uTexture, vTexCoords);" +
                    "}";


}
