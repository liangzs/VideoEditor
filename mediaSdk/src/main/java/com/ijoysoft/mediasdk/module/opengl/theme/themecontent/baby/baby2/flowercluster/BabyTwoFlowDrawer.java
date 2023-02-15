package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby2.flowercluster;

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
public class BabyTwoFlowDrawer implements IParticleRender {
    private final String vertext = "uniform mat4 u_Matrix;\n" +
            "uniform float u_Time;\n" +
            "\n" +
            "attribute vec3 a_Position;  \n" +
            "attribute float a_Paticle;\n" +
            "attribute float a_ParticleStartTime;\n" +
            "attribute float a_PointSize;\n" +
            "varying float v_ElapsedTime;\n" +
            "varying float v_Paticle;\n" +
            "void main()\n" +
            "{                                \t  \t  \n" +
            "    v_Paticle=a_Paticle;\n" +
            "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
            "    gl_Position = u_Matrix * vec4(a_Position, 1.0);\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "}\n";
    private final String fragment = "precision mediump float; \n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "varying float v_Paticle;\n" +
            "varying float v_ElapsedTime;\n" +
            "void main()                    \t\t\n" +
            "{\n" +
            "\n" +
            "    float x=gl_PointCoord.x-0.5;\n" +
            "    float y=gl_PointCoord.y-0.5;\n" +
            "    float nx=(cos(v_ElapsedTime)*x - sin(v_ElapsedTime )* y);\n" +
            "    float ny = (sin( v_ElapsedTime )*x + cos( v_ElapsedTime )*y);\n" +
            "    gl_FragColor=texture2D(u_TextureUnit, vec2( nx+0.5,ny+0.5));\n" +
            "}\n";
    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();
    private ParticleShaderProgram particleProgram;
    private BaseParticleSystem particleSystem;
    private long globalStartTime;
    private int texture;

    public BabyTwoFlowDrawer(BaseParticleSystem particleSystem) {
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
