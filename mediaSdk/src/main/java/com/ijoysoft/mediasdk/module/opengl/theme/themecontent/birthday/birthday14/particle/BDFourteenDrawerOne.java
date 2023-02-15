package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14.particle;

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
 * 烟花
 */
public class BDFourteenDrawerOne implements IParticleRender {


    private final String VERTEX = "uniform mat4 u_Matrix;\n" +
            "attribute vec3 a_Position;\n" +
            "attribute vec3 a_Color;\n" +
            "attribute float a_PointSize;\n" +
            "attribute float a_Alpha;\n" +
            "varying float v_Alpha;\n" +
            "varying vec3 v_Color;\n" +
            "\n" +
            "\n" +
            "void main()                    \n" +
            "{                                \n" +
            "    v_Color = a_Color;\n" +
            "    v_Alpha = a_Alpha;\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "    gl_Position = u_Matrix * vec4(a_Position, 1.0);\n" +
            "}\n";

    private final String FRAGMENT = "precision mediump float; \n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "varying vec3 v_Color;\n" +
            "varying float v_Alpha;\n" +
            "void main()\n" +
            "{\n" +
            " gl_FragColor=vec4(v_Color, 1.0) * texture2D(u_TextureUnit, gl_PointCoord)*v_Alpha;\n" +
            "}\n";

    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    private ParticleShaderProgram particleProgram;
    private BaseParticleSystem particleSystem;
    private long globalStartTime;
    private int texture;


    public BDFourteenDrawerOne(BaseParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
        particleProgram = new ParticleShaderProgram(VERTEX, FRAGMENT);
    }

    @Override
    public void onSurfaceCreated() {
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {

    }

    /**
     * 单个纹理
     */
    public void init(Bitmap bitmap) {
        globalStartTime = System.currentTimeMillis();
        particleSystem.addParticles();
        texture = OpenGlUtils.loadTexture(bitmap, NO_TEXTURE);
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
        viewProjectionMatrix = matrix;
        particleProgram.setMatrix(matrix);
    }
}
