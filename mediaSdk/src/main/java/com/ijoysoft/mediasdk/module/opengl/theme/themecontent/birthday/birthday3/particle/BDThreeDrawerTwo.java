package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3.particle;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.BaseParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import java.util.List;

import static android.opengl.Matrix.setIdentityM;
import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

/**
 * 这里还是需要激活多个纹理单元才行,然后每个纹理绑定不同的通道，
 * 最后在fragment中，重写计划
 */
public class BDThreeDrawerTwo implements IParticleRender {
    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    private ParticleShaderProgram particleProgram;
    private BaseParticleSystem particleSystem;
    private long globalStartTime;
    private int[] textures;
    private int[] textureLocations;
    private List<Bitmap> listMimpaps;

    public BDThreeDrawerTwo(BaseParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
    }

    public void init(List<Bitmap> mimaps, int resVetex, int resFragment) {
        listMimpaps = mimaps;
        textures = new int[listMimpaps.size()];
        textureLocations = new int[listMimpaps.size()];
        particleProgram = new ParticleShaderProgram(resVetex, resFragment);
        globalStartTime = System.currentTimeMillis();
        for (int i = 0; i < listMimpaps.size(); i++) {
            textures[i] = OpenGlUtils.loadTexture(listMimpaps.get(i), NO_TEXTURE);
        }
        for (int i = 0; i < textureLocations.length; i++)
            textureLocations[i] = GLES20.glGetUniformLocation(particleProgram.getPrograram(), "u_Texture" + (1 + i));
        particleSystem.addParticles();
    }

    public void init(List<Bitmap> mimaps, String resVetex, String resFragment) {
        listMimpaps = mimaps;
        textures = new int[listMimpaps.size()];
        textureLocations = new int[listMimpaps.size()];
        particleProgram = new ParticleShaderProgram(resVetex, resFragment);
        globalStartTime = System.currentTimeMillis();
        for (int i = 0; i < listMimpaps.size(); i++) {
            textures[i] = OpenGlUtils.loadTexture(listMimpaps.get(i), NO_TEXTURE);
        }
        for (int i = 0; i < textureLocations.length; i++)
            textureLocations[i] = GLES20.glGetUniformLocation(particleProgram.getPrograram(), "u_Texture" + (1 + i));
        particleSystem.addParticles();
         

    }


    @Override
    public void onSurfaceCreated() {
        globalStartTime = System.currentTimeMillis();
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
        if(ObjectUtils.isEmpty(particleProgram)){
            return;
        }
        particleProgram.useProgram();
        onDrawPreview();

        particleProgram.setUniforms(viewProjectionMatrix, currentTime);//默认第一个纹理
        particleSystem.bindData(particleProgram);
        particleSystem.draw();
        onDrawAfter();
    }

    @Override
    public void onDestroy() {
        if(particleProgram!=null){
            GLES20.glDeleteProgram(particleProgram.getPrograram());
        }
        if(!ObjectUtils.isEmpty(textures)){
            GLES20.glDeleteTextures(textures.length, textures, 0);
            for (int i = 0; i < textures.length; i++)
                textures[i] = -1;
        }

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
