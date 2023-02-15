package com.ijoysoft.mediasdk.module.opengl.particle;

import android.opengl.GLES20;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class ParticleShaderProgram extends ShaderProgram {
    // Uniform locations
    private int uMatrixLocation;
    private int uTimeLocation;

    // Attribute locations
    private int aPositionLocation;
    private int aColorLocation;
    private int aDirectionVectorLocation;
    private int aParticleStartTimeLocation;
    private int aPointSizeLocation;
    private int uTextureUnitLocation;

    private int particleTetureLocation;
    private int alphaLocation;
    private int accelerateLocation;

    public ParticleShaderProgram(int vexId, int fragId) {
        super(vexId, fragId);
        initLocation();
    }


    public ParticleShaderProgram(String vexId, String fragId) {
        super(vexId, fragId);
        initLocation();
    }

    private void initLocation() {
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR);
        aParticleStartTimeLocation =
                glGetAttribLocation(program, A_PARTICLE_START_TIME);
        aPointSizeLocation =
                glGetAttribLocation(program, A_POINTSIZE);

        particleTetureLocation = glGetAttribLocation(program, A_PARTICLE);
        alphaLocation = glGetAttribLocation(program, A_ALPHA);
        uTimeLocation = glGetUniformLocation(program, U_TIME);
        accelerateLocation = glGetAttribLocation(program, A_ACCELERATE);
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
    }

    public void onDrawPreview() {
        uTimeLocation = glGetUniformLocation(program, U_TIME);
    }

    /*
    public void setUniforms(float[] matrix, float elapsedTime) {
     */
    public void setUniforms(float[] matrix, float elapsedTime, int textureId) {
        //先回收
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform1f(uTimeLocation, elapsedTime);
        glActiveTexture(GLES20.GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);
    }

    public void setUniforms(float[] matrix, float elapsedTime) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform1f(uTimeLocation, elapsedTime);
    }

    public void setMatrix(float[] matrix) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    protected void setFloat(final int location, final float floatValue) {
        GLES20.glUniform1f(location, floatValue);
    }

    public void onDestroy() {
    }


    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }

    public int getDirectionVectorAttributeLocation() {
        return aDirectionVectorLocation;
    }

    public int getParticleStartTimeAttributeLocation() {
        return aParticleStartTimeLocation;
    }

    public int getaPointSizeLocation() {
        return aPointSizeLocation;
    }


    public int getParticleTetureLocation() {
        return particleTetureLocation;
    }

    public int getAlphaLocation() {
        return alphaLocation;
    }

    public int getAccelerateLocation() {
        return accelerateLocation;
    }

    public int getuTimeLocation() {
        return uTimeLocation;
    }


}
