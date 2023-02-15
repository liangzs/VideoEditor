package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine10.particle;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import static android.opengl.Matrix.setIdentityM;
import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

/**
 * 渲染执行形状星型，一会排成个一字，一会排成个人字，秋天来了
 */
public class DBTenDrawerTwo implements IParticleRender {
    private static final String VERTEXT = "uniform mat4 u_Matrix;\n" +
            "uniform float u_Time;\n" +
            "\n" +
            "attribute vec3 a_Position;  \n" +
            "attribute float a_Alpha;\n" +
            "attribute float a_ParticleStartTime;\n" +
            "attribute float a_PointSize;\n" +
            "attribute vec3 a_DirectionVector;\n" +
            "varying float v_Alpha;\n" +
            "varying float v_ElapsedTime;\n" +
            "\n" +
            "void main()                    \n" +
            "{                                 \n" +
            "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
            "    vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);" +
            "    gl_Position = u_Matrix * vec4(currentPosition, 1.0);\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "    v_Alpha = a_Alpha;\n" +
            "}\n";

    private static final String FRAGMENT = "precision mediump float; \n" +
            "uniform sampler2D u_TextureUnit;\n" +
            " varying float v_Alpha;  \n" +
            "void main()                   \n" +
            "{\n" +
            "    gl_FragColor = texture2D(u_TextureUnit, gl_PointCoord);\n" +
            "}\n";

    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();
    private ParticleShaderProgram particleProgram;
    private BDTenSystemTwo particleSystem;
    private long globalStartTime;
    private int texture;

    public DBTenDrawerTwo() {
        particleProgram = new ParticleShaderProgram(VERTEXT, FRAGMENT);
        globalStartTime = System.currentTimeMillis();
        particleSystem = new BDTenSystemTwo();
    }

    public void init(Bitmap bitmap) {
        texture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE);
    }

    @Override
    public void onSurfaceCreated() {

    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {

    }

    @Override
    public void onDrawFrame() {
        float currentTime = (System.currentTimeMillis() - globalStartTime) / 1000f;
        particleProgram.useProgram();
         
        particleSystem.updateParticle(currentTime);
        particleProgram.setUniforms(viewProjectionMatrix, currentTime, texture);//默认第一个纹理
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
