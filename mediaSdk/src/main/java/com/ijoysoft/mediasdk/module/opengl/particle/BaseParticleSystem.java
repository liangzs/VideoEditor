package com.ijoysoft.mediasdk.module.opengl.particle;

import java.util.Random;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;

public class BaseParticleSystem {
    protected static final int POSITION_COMPONENT_COUNT = 3;
    protected static final int COLOR_COMPONENT_COUNT = 3;
    protected static final int VECTOR_COMPONENT_COUNT = 3;
    protected static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;
    protected static final int PARTICLE_ALPHA_COUNT = 1;


    protected static int TOTAL_COMPONENT_COUNT =
            POSITION_COMPONENT_COUNT
                    + COLOR_COMPONENT_COUNT
                    + VECTOR_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT;

    protected static int STRIDE = TOTAL_COMPONENT_COUNT * Geometry.BYTES_PER_FLOAT;

    protected float[] particles;
    protected ParticleUtil vertexArray;
    protected int maxParticleCount;

    protected int currentParticleCount;
    protected int nextParticle;
    protected Random random = new Random();

    public BaseParticleSystem(int maxParticleCount) {
        particles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
        vertexArray = new ParticleUtil(particles);
        this.maxParticleCount = maxParticleCount;
    }

    public BaseParticleSystem() {

    }

    public void addParticle(Geometry.Point position, int color, Geometry.Vector direction,
                            float particleStartTime) {
    }

    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getDirectionVectorAttributeLocation(),
                VECTOR_COMPONENT_COUNT, STRIDE);
        dataOffset += VECTOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleStartTimeAttributeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);

    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0, currentParticleCount);
    }

    public void addParticles() {

    }

    public void onDrawFrame(ParticleShaderProgram particleProgram, float currentTime) {

    }

    public void setBuilder(ParticleBuilder builder) {
    }
}
