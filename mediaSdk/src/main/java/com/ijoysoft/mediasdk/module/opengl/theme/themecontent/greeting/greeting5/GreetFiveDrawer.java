package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
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
public class GreetFiveDrawer implements IParticleRender {
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    private ParticleShaderProgram particleProgram;
    private GreetFiveSystem particleSystem;
    private long globalStartTime;
    private int[] textures = {-1, -1, -1};
    private int[] textureLocations = {-1, -1, -1};
    private int delayTime;

    public GreetFiveDrawer() {

    }

    @Override
    public void onSurfaceCreated() {
//        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        glEnable(GL_BLEND);
//        glBlendFunc(GL_ONE, GL_ONE);

        particleProgram = new ParticleShaderProgram(R.raw.bd_particle_two_vertex, R.raw.bd_particle_two_fragment);
        particleSystem = new GreetFiveSystem(8);
        particleSystem.setDelayStart(delayTime);
        globalStartTime = System.currentTimeMillis();

        textures[0] = OpenGlUtils.loadTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_five_theme_particle1" + ConstantMediaSize.SUFFIX));
        textures[1] = OpenGlUtils.loadTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_five_theme_particle2" + ConstantMediaSize.SUFFIX));
        textures[2] = OpenGlUtils.loadTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet_five_theme_particle3" + ConstantMediaSize.SUFFIX));

        for (int i = 0; i < textureLocations.length; i++)
            textureLocations[i] = GLES20.glGetUniformLocation(particleProgram.getPrograram(), "u_Texture" + (2 + i));

    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
//        glViewport(0, 0, width, height);

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

    public void setDelayStart(int delayTime) {
        this.delayTime = delayTime;
    }
}
