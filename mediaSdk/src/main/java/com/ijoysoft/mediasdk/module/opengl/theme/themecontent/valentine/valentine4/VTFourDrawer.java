package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine4;

import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
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
public class VTFourDrawer implements IParticleRender {
    private static final String VERTEXT = "uniform mat4 u_Matrix;\n" +
            "uniform float u_Time;\n" +
            "\n" +
            "attribute vec3 a_Position;  \n" +
            "attribute float a_Alpha;\n" +
            "attribute float a_ParticleStartTime;\n" +
            "attribute float a_PointSize;\n" +
            "varying float v_Alpha;\n" +
            "varying float v_ElapsedTime;\n" +
            "\n" +
            "void main()                    \n" +
            "{                                 \n" +
            "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
            "    float gravityFactor = v_ElapsedTime * v_ElapsedTime / 18.0;\n" +
            "    vec3 currentPosition = vec3(a_Position.x, a_Position.y+gravityFactor,0);" +
            "    gl_Position = u_Matrix * vec4(currentPosition, 1.0);\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "    v_Alpha = a_Alpha;\n" +
            "}\n";

    private static final String FRAGMENT = "precision mediump float; \n" +
            "uniform sampler2D u_TextureUnit;\n" +
            " varying float v_Alpha;  \n" +
            "void main()                   \n" +
            "{\n" +
            "    gl_FragColor = texture2D(u_TextureUnit, gl_PointCoord)*v_Alpha;\n" +
            "}\n";

    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    private ParticleShaderProgram particleProgram;
    private VTFourSystem particleSystem;
    private long globalStartTime;
    private int texture;

    @Override
    public void onSurfaceCreated() {
        particleProgram = new ParticleShaderProgram(VERTEXT, FRAGMENT);
        particleSystem = new VTFourSystem(20);
        globalStartTime = System.currentTimeMillis();
        texture = OpenGlUtils.loadTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_four_particle1"));


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
