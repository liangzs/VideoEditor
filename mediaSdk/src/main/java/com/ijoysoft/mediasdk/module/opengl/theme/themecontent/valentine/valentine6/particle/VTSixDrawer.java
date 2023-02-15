package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine6.particle;

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
public class VTSixDrawer implements IParticleRender {


    private final String vertex = "uniform mat4 u_Matrix;\n" +
            "uniform float u_Time;\n" +
            "\n" +
            "attribute vec3 a_Position;  \n" +
            "attribute float a_Paticle;\n" +
            "attribute vec3 a_DirectionVector;\n" +
            "attribute float a_ParticleStartTime;\n" +
            "attribute float a_PointSize;\n" +
            "varying float v_ElapsedTime;\n" +
            "\n" +
            "void main()                    \n" +
            "{                               \n" +
            "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
            "    float gravityFactor = v_ElapsedTime;\n" +
            "    vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);\n" +
            "    gl_Position = u_Matrix * vec4(currentPosition, 1.0);\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "}\n";

    private final String fragment = "precision mediump float; \n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "varying float v_Paticle;\n" +
            "varying float v_ElapsedTime;     \n" +
            "void main()                    \n" +
            "{\n" +
            "    float x=gl_PointCoord.x-0.5;\n" +
            "    float y=gl_PointCoord.y-0.5;\n" +
            "    float nx=(cos(v_ElapsedTime)*x - sin(v_ElapsedTime )* y);\n" +
            "    float ny = (sin( v_ElapsedTime )*x + cos( v_ElapsedTime )*y);\n" +
            "    gl_FragColor= texture2D(u_TextureUnit, vec2( nx+0.5,ny+0.5));\n" +
            "}\n";

    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    private ParticleShaderProgram particleProgram;
    private VTSixSystem particleSystem;
    private long globalStartTime;
    private int texture = -1;


    @Override
    public void onSurfaceCreated() {
        particleProgram = new ParticleShaderProgram(vertex, fragment);
        particleSystem = new VTSixSystem(4);
        globalStartTime = System.currentTimeMillis();
        texture = OpenGlUtils.loadTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/vt_six_particle1" + ConstantMediaSize.SUFFIX));


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
