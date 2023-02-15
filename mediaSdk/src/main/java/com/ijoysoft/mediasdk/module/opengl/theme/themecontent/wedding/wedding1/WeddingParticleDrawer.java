package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1;

import android.graphics.Color;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.Matrix.setIdentityM;


public class WeddingParticleDrawer implements IParticleRender {


    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();
    /*
    // Maximum saturation and value.
    private final float[] hsv = {0f, 1f, 1f};*/

    private ParticleShaderProgram particleProgram;
    private WeddingParticleSystem particleSystem;
    private WeddingParticleShooter greenParticleShooter;
    private long globalStartTime;
    private int texture;

    public WeddingParticleDrawer() {
    }

    @Override
    public void onSurfaceCreated() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Enable additive blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);

        particleProgram = new ParticleShaderProgram(R.raw.theme_wedding_particle_vertex, R.raw.particle_fragment_shader);
        particleSystem = new WeddingParticleSystem(20);
        globalStartTime = System.currentTimeMillis();


        final float angleVarianceInDegrees = 5f;
        final float speedVariance = 1f;


        greenParticleShooter = new WeddingParticleShooter(
                new Geometry.Point(0f, 0f, 0f),
                Color.rgb(25, 255, 25),
                angleVarianceInDegrees,
                speedVariance, 10);
        texture = OpenGlUtils.loadTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding_one_particle" + ConstantMediaSize.SUFFIX));

    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {

//        ParticleUtil.perspectiveM(projectionMatrix, 45, (float) width
//                / (float) height, 1f, 10f);
//
//        setIdentityM(viewMatrix, 0);
//        translateM(viewMatrix, 0, 0f, -1.0f, -3f);
//        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0,
//                viewMatrix, 0);
    }

    @Override
    public void onDrawFrame() {
//        glClear(GL_COLOR_BUFFER_BIT);
        float currentTime = (System.currentTimeMillis() - globalStartTime) / 1000f;
        greenParticleShooter.addParticles(particleSystem, currentTime);
        particleProgram.useProgram();
         
        particleProgram.setUniforms(viewProjectionMatrix, currentTime, texture);
        particleSystem.bindData(particleProgram);
        particleSystem.draw();
    }

    @Override
    public void onDestroy() {
        if(particleProgram!=null){
            GLES20.glDeleteProgram(particleProgram.getPrograram());
        }
        GLES20.glDeleteTextures(1, new int[]{texture}, 0);
    }

    @Override
    public void onPlepare(int index) {

    }

    @Override
    public void setMatrix(float[] matrix) {
        viewProjectionMatrix = matrix;
        particleProgram.setMatrix(matrix);
    }
}
