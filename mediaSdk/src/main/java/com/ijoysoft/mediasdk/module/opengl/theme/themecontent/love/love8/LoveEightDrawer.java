package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love8;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.Matrix.setIdentityM;

/**
 * @ author       lfj
 * @ date         2020/10/23
 * @ description
 */
public class LoveEightDrawer implements IParticleRender {


    private final String vertex = "uniform mat4 u_Matrix;\n" +
            "uniform float u_Time;\n" +
            "\n" +
            "attribute vec3 a_Position;  \n" +
            "attribute float a_Paticle;\n" +
            "attribute vec3 a_DirectionVector;\n" +
            "attribute float a_ParticleStartTime;\n" +
            "attribute float a_PointSize;\n" +
            "varying float v_ElapsedTime;\n" +
            "varying float v_Paticle;\n" +
            "\n" +
            "void main()                    \n" +
            "{                                \t  \t  \n" +
            "    v_Paticle=a_Paticle;\n" +
            "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
            "    float gravityFactor = v_ElapsedTime;\n" +
            "    vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);\n" +
            "    gl_Position = u_Matrix * vec4(currentPosition, 1.0);\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "}\n";

    private final String fragment = "precision mediump float; \n" +
            "uniform sampler2D u_Texture1;\n" +
            "varying float v_Paticle;\n" +
            "varying float v_ElapsedTime;     \n" +
            "void main()                    \n" +
            "{\n" +
            "    float x=gl_PointCoord.x-0.5;\n" +
            "    float y=gl_PointCoord.y-0.5;\n" +
            "    float nx=(cos(v_ElapsedTime)*x - sin(v_ElapsedTime )* y);\n" +
            "    float ny = (sin( v_ElapsedTime )*x + cos( v_ElapsedTime )*y);\n" +
            "    gl_FragColor=texture2D(u_Texture1, vec2( nx+0.5,ny+0.5));\n" +
            "}\n";

    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    private ParticleShaderProgram particleProgram;
    private LoveEightSystem particleSystem;
    private long globalStartTime;
    private int[] textures = new int[1];
    private int[] textureLocations = new int[1];
    private static final String A_PARTICLE = "a_Paticle";
    private int particleLocation;

    public LoveEightDrawer() {

    }

    @Override
    public void onSurfaceCreated() {
        particleProgram = new ParticleShaderProgram(vertex, fragment);
        particleSystem = new LoveEightSystem(7);
        globalStartTime = System.currentTimeMillis();

        textures[0] = OpenGlUtils.loadTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_eight_theme_particle1" + ConstantMediaSize.SUFFIX));

        for (int i = 0; i < textureLocations.length; i++)
            textureLocations[i] = GLES20.glGetUniformLocation(particleProgram.getPrograram(), "u_Texture" + (1 + i));
        particleLocation = glGetAttribLocation(particleProgram.getPrograram(), A_PARTICLE);
    }

    @Override
    public void onSurfaceChanged(int offsetx, int offsety, int width, int height, int screenWidth, int screenHeight) {
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
        particleSystem.bindData(particleProgram, particleLocation);
        particleSystem.draw();
        onDrawAfter();
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
