package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday12;

import android.opengl.GLES20;
import android.util.Log;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import static android.opengl.Matrix.setIdentityM;

/**
 * 这里还是需要激活多个纹理单元才行,然后每个纹理绑定不同的通道，
 * 最后在fragment中，重写计划
 */
public class BDTwelveParticleDrawer implements IParticleRender {
    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();
    private ParticleShaderProgram particleProgram;
    private BDTwelveParticleSystem particleSystem;
    private int[] textures = new int[8];
    private int[] textureLocations = new int[8];
    private long globalStartTime;

    public BDTwelveParticleDrawer() {
        particleSystem = new BDTwelveParticleSystem();

    }

    @Override
    public void onSurfaceCreated() {

        globalStartTime = System.currentTimeMillis();
        particleProgram = new ParticleShaderProgram(R.raw.theme_congra_particle_two_vertex, R.raw.theme_congra_particle_two_fragment);
        textures[0] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), R.mipmap.theme_congra_two_particle1);
        textures[1] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), R.mipmap.theme_congra_two_particle2);
        textures[2] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), R.mipmap.theme_congra_two_particle3);
        textures[3] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), R.mipmap.theme_congra_two_particle4);
        textures[4] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), R.mipmap.theme_congra_two_particle5);
        textures[5] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), R.mipmap.theme_congra_two_particle6);
        textures[6] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), R.mipmap.theme_congra_two_particle7);
        textures[7] = OpenGlUtils.loadTexture(MediaSdk.getInstance().getContext(), R.mipmap.theme_congra_two_particle8);
        Log.i("test2:", (System.currentTimeMillis() - globalStartTime) + "");
        for (int i = 0; i < textureLocations.length; i++)
            textureLocations[i] = GLES20.glGetUniformLocation(particleProgram.getPrograram(), "u_Texture" + (1 + i));
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
        particleProgram.useProgram();
        onDrawPreview();
         
        particleProgram.setUniforms(viewProjectionMatrix, currentTime);
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
        if(particleSystem==null){
            return;
        }
        switch (index % 5) {
            case 1:
                particleSystem.addParticles(1f);
                particleSystem.setDelayStart(1000);
                break;
            case 3:
                particleSystem.addParticles(-1f);
                particleSystem.setDelayStart(1000);
                break;
        }
    }

    @Override
    public void setMatrix(float[] matrix) {
        viewProjectionMatrix = matrix;
        particleProgram.setMatrix(matrix);
    }
}
