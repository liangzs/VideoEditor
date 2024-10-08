package com.ijoysoft.mediasdk.module.opengl.particle;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import static android.opengl.GLES20.glUseProgram;

abstract class ShaderProgram {
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_COLOR = "u_Color";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_TIME = "u_Time";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";


    protected static final String A_DIRECTION_VECTOR = "a_DirectionVector";
    protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";
    protected static final String A_POINTSIZE = "a_PointSize";
    protected static final String A_PARTICLE = "a_Paticle";
    protected static final String A_ALPHA = "a_Alpha";
    protected static final String A_ACCELERATE = "a_Accelerate";
    // Shader program
    protected final int program;

    protected ShaderProgram(int vertexShaderResourceId,
                            int fragmentShaderResourceId) {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
                OpenGlUtils.readShaderFromRawResource(vertexShaderResourceId),
                OpenGlUtils.readShaderFromRawResource(fragmentShaderResourceId));
    }

    protected ShaderProgram(String vertexShaderResourceId,
                            String fragmentShaderResourceId) {
        // Compile the shaders and link the program.
        LogUtils.v("ShaderProgram", "vertexShaderResourceId:" + vertexShaderResourceId + "," + "fragmentShaderResourceId:" + fragmentShaderResourceId);
        program = ShaderHelper.buildProgram(vertexShaderResourceId, fragmentShaderResourceId);
    }

    public void useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program);
    }

    public int getPrograram() {
        return program;
    }
}
