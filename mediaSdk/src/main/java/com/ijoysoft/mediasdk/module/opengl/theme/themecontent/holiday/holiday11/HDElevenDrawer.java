package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday11;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import static android.opengl.Matrix.setIdentityM;

/**
 * 飘雪
 */
public class HDElevenDrawer implements IParticleRender {
    private static final String VERTEXT = "uniform mat4 u_Matrix;\n" +
            "uniform float u_Time;\n" +
            "\n" +
            "attribute vec3 a_Position;  \n" +
            "attribute float a_Alpha;\n" +
            "attribute float a_Paticle;\n" +
            "attribute float a_ParticleStartTime;\n" +
            "attribute float a_PointSize;\n" +
            "varying float v_Alpha;\n" +
            "varying float v_ElapsedTime;\n" +
            "varying float v_Paticle;\n" +
            "\n" +
            "void main()                    \n" +
            "{                                 \n" +
            "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
            "    float gravityFactor = v_ElapsedTime * v_ElapsedTime / 18.0;\n" +
            "    vec3 currentPosition = vec3(a_Position.x, a_Position.y+gravityFactor,0);" +
            "    gl_Position = u_Matrix * vec4(currentPosition, 1.0);\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "    v_Alpha = a_Alpha;\n" +
            "    v_Paticle=a_Paticle;\n" +
            "}\n";

    private static final String FRAGMENT = "precision mediump float; \n" +
            "uniform sampler2D u_Texture2;\n" +
            "uniform sampler2D u_Texture3;\n" +
            "varying float v_Paticle;\n" +
            " varying float v_Alpha;  \n" +
            "void main()                   \n" +
            "{\n" +
            "   if(v_Paticle==0.0){\n" +
            "    gl_FragColor = texture2D(u_Texture2, gl_PointCoord)*v_Alpha;\n" +
            "    }else if(v_Paticle==1.0){\n" +
            "    gl_FragColor = texture2D(u_Texture3, gl_PointCoord)*v_Alpha;\n" +
            "}\n" +
            "}\n";

    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    private ParticleShaderProgram particleProgram;
    private HDElevenSystem particleSystem;
    private long globalStartTime;
    private int[] textures = new int[2];
    private int[] textureLocations = new int[2];

    @Override
    public void onSurfaceCreated() {
        particleProgram = new ParticleShaderProgram(VERTEXT, FRAGMENT);
        particleSystem = new HDElevenSystem(40);
        globalStartTime = System.currentTimeMillis();
        textures[0] = OpenGlUtils.loadTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_eleven_particle2" + ConstantMediaSize.SUFFIX));
        textures[1] = OpenGlUtils.loadTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/hd_eleven_particle1" + ConstantMediaSize.SUFFIX));

        for (int i = 0; i < textureLocations.length; i++)
            textureLocations[i] = GLES20.glGetUniformLocation(particleProgram.getPrograram(), "u_Texture" + (2 + i));

    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
    }

    protected void onDrawAfter() {
        for (int i = 0; i < textures.length
                && textures[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + ((i+3)));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
    }

    protected void onDrawPreview() {
        for (int i = 0; i < textures.length
                && textures[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + ((i+3)));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);
            GLES20.glUniform1i(textureLocations[i], ((i+3)));
        }
    }


    @Override
    public void onDrawFrame() {
        float currentTime = (System.currentTimeMillis() - globalStartTime) / 1000f;
        particleSystem.addParticle(currentTime);
        particleProgram.useProgram();
        onDrawPreview();
         
        particleProgram.setUniforms(viewProjectionMatrix, currentTime);//默认第一个纹理
        particleSystem.bindData(particleProgram);
        particleSystem.draw();
    }

    @Override
    public void onDestroy() {
        if(particleProgram!=null){
            GLES20.glDeleteProgram(particleProgram.getPrograram());
        }
        GLES20.glDeleteTextures(textures.length, textures, 0);
        for (int i = 0; i < textures.length; i++)
            textures[i] = -1;
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
