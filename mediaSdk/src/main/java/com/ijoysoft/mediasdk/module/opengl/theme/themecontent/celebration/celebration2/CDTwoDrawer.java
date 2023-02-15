package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration2;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import static android.opengl.Matrix.setIdentityM;

/**
 * 这里还是需要激活多个纹理单元才行,然后每个纹理绑定不同的通道，
 * 最后在fragment中，重写计划
 */
public class CDTwoDrawer implements IParticleRender {


    private final String vertex = "uniform mat4 u_Matrix;\n" +
            "uniform float u_Time;\n" +
            "\n" +
            "attribute vec3 a_Position;  \n" +
            "attribute vec3 a_Color;\n" +
            "attribute vec3 a_DirectionVector;\n" +
            "attribute float a_ParticleStartTime;\n" +
            "attribute float a_PointSize;\n" +
            "varying vec3 v_Color;\n" +
            "varying float v_ElapsedTime;\n" +
            "\n" +
            "void main()                    \n" +
            "{                                \t  \t  \n" +
            "    v_Color = a_Color;\n" +
            "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
            "    float gravityFactor = v_ElapsedTime;\n" +
            "    vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);\n" +
            "    gl_Position = u_Matrix * vec4(currentPosition, 1.0);\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "}\n";

    private final String fragment = "precision mediump float; \n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "varying vec3 v_Color;\n" +
            "void main()                    \n" +
            "{\n" +
            "      gl_FragColor=vec4(v_Color, 1.0) * texture2D(u_TextureUnit,gl_PointCoord);\n" +
            "}\n";

    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    private ParticleShaderProgram particleProgram;
    private CDTwoSystem particleSystem;
    private long globalStartTime;
    private int texture;

    public CDTwoDrawer() {

    }

    @Override
    public void onSurfaceCreated() {
        particleProgram = new ParticleShaderProgram(vertex, fragment);
        particleSystem = new CDTwoSystem(10);
        globalStartTime = System.currentTimeMillis();
        texture = OpenGlUtils.loadTexture(loadBitmap());
    }

    public Bitmap loadBitmap() {
        return BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/cd_two_particle" + ConstantMediaSize.SUFFIX);
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
    }


    @Override
    public void onDrawFrame() {
        float currentTime = (System.currentTimeMillis() - globalStartTime) / 1000f;
        particleSystem.addParticle(currentTime);
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
