package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday8;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import static android.opengl.Matrix.setIdentityM;

/**
 * 渲染执行形状星型，一会排成个一字，一会排成个人
 */
public class BDEightStarClusterDrawer implements IParticleRender {
    private static final String VERTEXT = "uniform mat4 u_Matrix;\n" +
            "attribute vec3 a_Position;\n" +
            "attribute vec3 a_Color;\n" +
            "attribute float a_PointSize;\n" +
            "attribute float a_Alpha;\n" +
            "varying float v_Alpha;\n" +
            "varying vec3 v_Color;\n" +
            "\n" +
            "\n" +
            "void main()                    \n" +
            "{                                \t  \t  \n" +
            "    v_Color = a_Color;\n" +
            "    v_Alpha = a_Alpha;\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "    gl_Position = u_Matrix * vec4(a_Position, 1.0);\n" +
            "}\n";

    private static final String FRAGMENT = "precision mediump float; \n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "varying float v_Alpha;\n" +
            "void main()\n" +
            "{\n" +
            " gl_FragColor=texture2D(u_TextureUnit, gl_PointCoord)*v_Alpha;\n" +
            "}\n";


    private final float[] viewProjectionMatrix = new float[16];
    private ParticleShaderProgram particleProgram;
    private BaseParticleSystem particleSystem;
    private long globalStartTime;
    private int texture;

    public BDEightStarClusterDrawer(BaseParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
        particleProgram = new ParticleShaderProgram(VERTEXT, FRAGMENT);
    }

    @Override
    public void onSurfaceCreated() {

    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {

    }

    public void init(Bitmap bitmap) {
        texture = OpenGlUtils.loadTexture(bitmap, OpenGlUtils.NO_TEXTURE);

        globalStartTime = System.currentTimeMillis();
        particleSystem.addParticles();
    }

    @Override
    public void onDrawFrame() {
        float currentTime = (System.currentTimeMillis() - globalStartTime) / 1000f;
        particleProgram.useProgram();
         
        particleProgram.setUniforms(viewProjectionMatrix, currentTime, texture);//默认第一个纹理
        particleSystem.bindData(particleProgram);
        particleSystem.draw();
    }

    @Override
    public void onDestroy() {
        if (particleProgram != null) {
            GLES20.glDeleteProgram(particleProgram.getPrograram());
        }
        GLES20.glDeleteTextures(1, new int[]{texture}, 0);
    }

    @Override
    public void onPlepare(int index) {

    }

    @Override
    public void setMatrix(float[] matrix) {
    }


}
