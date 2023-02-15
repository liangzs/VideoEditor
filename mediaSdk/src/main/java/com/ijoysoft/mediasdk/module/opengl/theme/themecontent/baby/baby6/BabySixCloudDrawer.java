package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby6;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import static android.opengl.Matrix.setIdentityM;
import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

/**
 * 用顶点数组集合去绘画各种集合的花，提换每朵花作为控件绘画，很大程度提高渲染速度
 */
public class BabySixCloudDrawer implements IParticleRender {
    private final String vertext = "uniform mat4 u_Matrix;\n" +
            "uniform float u_Time;\n" +
            "attribute vec3 a_Position;  \n" +
            "attribute float a_Paticle;\n" +
            "attribute float a_ParticleStartTime;\n" +
            "attribute vec3 a_DirectionVector;\n" +
            "attribute float a_PointSize;\n" +
            "attribute float a_Alpha;\n" +
            "varying float v_ElapsedTime;\n" +
            "varying float v_Alpha;\n" +
            "void main()\n" +
            "{                                \t  \t  \n" +
            "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
            "    vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);\n" +
            "    gl_Position = u_Matrix * vec4(currentPosition, 1.0);\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "    v_Alpha = a_Alpha;\n" +
            "}\n";
    private final String fragment = "precision mediump float; \n" +
            "uniform sampler2D u_TextureUnit;\n" +
            " varying float v_Alpha;  \n" +
            "void main()                  \n" +
            "{\n" +
            "    gl_FragColor=texture2D(u_TextureUnit,gl_PointCoord)*v_Alpha;\n" +
            "}\n";
    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();
    private ParticleShaderProgram particleProgram;
    private BaseParticleSystem particleSystem;
    private long globalStartTime;
    private int texture;

    public BabySixCloudDrawer(BaseParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
        particleProgram = new ParticleShaderProgram(vertext, fragment);
    }

    @Override
    public void onSurfaceCreated() {
    }

    public void init(Bitmap bitmap) {

        particleSystem.addParticles();
        texture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE);
        globalStartTime = System.currentTimeMillis();
        particleSystem.addParticles();
    }

    @Override
    public void onSurfaceChanged(int offsetx, int offsety, int width, int height, int screenWidth, int screenHeight) {

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
