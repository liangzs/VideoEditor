package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine15;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import java.util.List;

import static android.opengl.Matrix.setIdentityM;
import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

/**
 * 烟花
 */
public class VTFifteenDrawer implements IParticleRender {
    private final String VERTEX = "uniform mat4 u_Matrix;\n" +
            "attribute vec3 a_Position;\n" +
            "attribute float a_PointSize;\n" +
            "attribute float a_Alpha;\n" +
            "varying float v_Alpha;\n" +
            "attribute float a_Paticle;\n" +
            "varying float v_Paticle;\n" +
            "\n" +
            "void main()                    \n" +
            "{                                \n" +
            "    v_Alpha = a_Alpha;\n" +
            "    v_Paticle=a_Paticle;\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "    gl_Position = u_Matrix * vec4(a_Position, 1.0);\n" +
            "}\n";

    private final String FRAGMENT = "precision mediump float; \n" +
            "varying float v_Paticle;\n" +
            "uniform sampler2D u_Texture1;\n" +
            "uniform sampler2D u_Texture2;\n" +
            "uniform sampler2D u_Texture3;\n" +
            "uniform sampler2D u_Texture4;\n" +
            "varying float v_Alpha;\n" +
            "void main(){\n" +
            "   if(v_Paticle==0.0){\n" +
            "       gl_FragColor=texture2D(u_Texture1, gl_PointCoord)*v_Alpha;\n" +
            "    }else if(v_Paticle==1.0){\n" +
            "        gl_FragColor= texture2D(u_Texture2, gl_PointCoord)*v_Alpha;\n" +
            "    }else if(v_Paticle==2.0){\n" +
            "        gl_FragColor= texture2D(u_Texture3, gl_PointCoord)*v_Alpha;\n" +
            "    }else if(v_Paticle==3.0){\n" +
            "        gl_FragColor= texture2D(u_Texture4, gl_PointCoord)*v_Alpha;\n" +
            "    }\n" +
            "}\n";

    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    private ParticleShaderProgram particleProgram;
    private VTFifteenSystem particleSystem;
    private long globalStartTime;
    private int[] textures;
    private int[] textureLocations;
    private List<Bitmap> listMimpaps;


    public VTFifteenDrawer() {
        particleSystem = new VTFifteenSystem(3500);
        particleProgram = new ParticleShaderProgram(VERTEX, FRAGMENT);
    }

    @Override
    public void onSurfaceCreated() {
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {

    }

    public void init(List<Bitmap> mimaps) {
        listMimpaps = mimaps;
        textures = new int[listMimpaps.size()];
        textureLocations = new int[listMimpaps.size()];

        globalStartTime = System.currentTimeMillis();
        for (int i = 0; i < listMimpaps.size(); i++) {
            textures[i] = OpenGlUtils.loadTexture(listMimpaps.get(i), NO_TEXTURE);
        }
        for (int i = 0; i < textureLocations.length; i++)
            textureLocations[i] = GLES20.glGetUniformLocation(particleProgram.getPrograram(), "u_Texture" + (1 + i));
        particleSystem.setNum(mimaps.size());
        particleSystem.addParticles();
    }


    protected void onDrawAfter() {
        if (ObjectUtils.isEmpty(textures)) {
            return;
        }
        for (int i = 0; i < textures.length
                && textures[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + ((i + 3)));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
    }

    protected void onDrawPreview() {
        if (ObjectUtils.isEmpty(textures)) {
            return;
        }
        for (int i = 0; i < textures.length
                && textures[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + ((i + 3)));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);
            GLES20.glUniform1i(textureLocations[i], ((i + 3)));
        }
    }

    @Override
    public void onDrawFrame() {
        float currentTime = (System.currentTimeMillis() - globalStartTime) / 1000f;
        particleProgram.useProgram();
        onDrawPreview();
         
        particleProgram.setUniforms(viewProjectionMatrix, currentTime);//默认第一个纹理
        particleSystem.bindData(particleProgram);
        particleSystem.draw();
        onDrawAfter();
    }

    @Override
    public void onDestroy() {
        if (particleProgram != null) {
            GLES20.glDeleteProgram(particleProgram.getPrograram());
        }
        if (ObjectUtils.isEmpty(textures)) {
            return;
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
