package com.ijoysoft.mediasdk.module.opengl.particle;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.Matrix.setIdentityM;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 随机粒子绘制
 *
 * @author hayring
 * fork from: {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1.BDOneDrawer}
 */
public class RandomParticlesDrawer implements IParticleRender {

    /**
     * glsl语句缓存
     * index：随机粒子种类数量
     * string: 对应数量的glsl语句
     */
    private static Map<Integer, String> fragmentCache = new HashMap<>();

    private final String vertex = "uniform mat4 u_Matrix;\n" +
            "uniform float u_Time;\n" +
            "\n" +
            "attribute vec3 a_Position;  \n" +
            "attribute float a_Paticle;\n" +
            "attribute vec3 a_DirectionVector;\n" +
            "attribute float a_ParticleStartTime;\n" +
            "attribute float a_PointSize;\n" +
            "attribute vec3 a_Color;\n" +
            "attribute float a_Alpha;\n" +
            "varying float v_Alpha;\n" +
            "varying vec3 v_Color;\n" +
            "varying float v_ElapsedTime;\n" +
            "varying float v_Paticle;\n" +
            "\n" +
            "void main()                    \n" +
            "{                                \t  \t  \n" +
            "    v_Color = a_Color;\n" +
            "    v_Alpha = a_Alpha;\n" +
            "    v_Paticle=a_Paticle;\n" +
            "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
            "    float gravityFactor = v_ElapsedTime;\n" +
            "    vec3 currentPosition = a_Position;" + // + (a_DirectionVector * v_ElapsedTime);\n" +
            "    gl_Position = u_Matrix * vec4(currentPosition, 1.0);\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "}\n";

    private static final String FRAGMENT_START = "precision mediump float";

    private static final String DEF_LINE = "; \nuniform sampler2D u_Texture";

    private static final String MID = ";\nvarying float v_Paticle;\n" +
            "varying vec3 v_Color;\n" +
            "varying float v_Alpha;\n" +
            "varying float v_ElapsedTime;     \n" +
            "void main()                    \n" +
            "{\n";
//            "    float x=gl_PointCoord.x-0.5;\n" +
//            "    float y=gl_PointCoord.y-0.5;\n" +
//            "    float nx=(cos(v_ElapsedTime)*x - sin(v_ElapsedTime )* y);\n" +
//            "    float ny = (sin( v_ElapsedTime )*x + cos( v_ElapsedTime )*y);\n";

    private static final String FRAGMENT_END = "}\n";

    private static final String FRAGMENT_LINE_ONE = " if(v_Paticle==";


    private static final String FRAGMENT_LINE_TWO = ".0){\n" +
            "       gl_FragColor=vec4(v_Color, 1.0) *texture2D(u_Texture";


    private static final String FRAGMENT_LINE_THREE = ", vec2(gl_PointCoord.x, 1.0-gl_PointCoord.y))* v_Alpha;\n" +
            "    }";

    private static final String FRAGMENT_LINE_THREE_FLIP = ", vec2(gl_PointCoord.x,gl_PointCoord.y))*v_Alpha;\n" +
            "    }";
    private static final String ELSE = " else ";


    private String fragment;
    private boolean isFlip = true;

    /**
     * 构建glsl语句
     */
    private void buildFragment() {
        //从缓存中获取
        if (fragmentCache.containsKey(textures.length)) {
            fragment = fragmentCache.get(textures.length);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(FRAGMENT_START);
        for (int i = 0; i < textures.length; i++) {
            sb.append(DEF_LINE).append(i + 1);
        }
        sb.append(MID);
        sb.append(FRAGMENT_LINE_ONE).append(0).append(FRAGMENT_LINE_TWO).append(1).append(isFlip ? FRAGMENT_LINE_THREE : FRAGMENT_LINE_THREE_FLIP);
        for (int i = 1; i < textures.length; i++) {
            sb.append(ELSE).append(FRAGMENT_LINE_ONE).append(i).append(FRAGMENT_LINE_TWO).append(i + 1).append(isFlip ? FRAGMENT_LINE_THREE : FRAGMENT_LINE_THREE_FLIP);
        }
        sb.append(FRAGMENT_END);
        fragment = sb.toString();
        fragmentCache.put(textures.length, fragment);
        LogUtils.i(getClass().getSimpleName(), "GLSL: \n" + fragment);
    }

    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    private ParticleShaderProgram particleProgram;
    private RandomParticleSystem particleSystem;
    private long globalStartTime;
    private int[] textures;
    private int[] textureLocations;
    private static final String A_PARTICLE = "a_Paticle";
    private int particleLocation;


    /**
     * builder专用构造器
     */
    public RandomParticlesDrawer() {
    }


    /**
     * 初始化方法
     *
     * @param bitmaps 颗粒素材
     */
    protected void init(List<Bitmap> bitmaps/*, int maxParticlesCount, Float ratio*/) {
//        if (ratio == null) {
//            particleSystem = new RandomParticleSystem(bitmaps.size(), maxParticlesCount);
//        } else {
//            particleSystem = new RandomParticleSystem(bitmaps.size(), maxParticlesCount, ratio);
//        }
        if (bitmaps.size() > 7) {
            LogUtils.w(getClass().getSimpleName(), "There are too much particle types");
        }
        textures = new int[bitmaps.size()];
        textureLocations = new int[bitmaps.size()];
        buildFragment();
        LogUtils.i(getClass().getSimpleName(), "RandomParticlesDrawer Createrd, size: " + bitmaps.size());
        particleProgram = new ParticleShaderProgram(vertex, fragment);


        for (int i = 0; i < textures.length; i++) {
            textures[i] = OpenGlUtils.loadTexture(bitmaps.get(i));
        }

        for (int i = 0; i < textureLocations.length; i++) {
            textureLocations[i] = GLES20.glGetUniformLocation(particleProgram.getPrograram(), "u_Texture" + (1 + i));
        }

    }

    @Override
    public void onSurfaceCreated() {
        onDrawAfter();
        globalStartTime = System.currentTimeMillis();
        particleLocation = glGetAttribLocation(particleProgram.getPrograram(), A_PARTICLE);
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
    }


    protected void onDrawAfter() {
        for (int i = 0; i < textures.length
                && textures[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + ((i + 3)));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
    }

    protected void onDrawPreview() {
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
        particleSystem.addParticle(currentTime);
        particleProgram.useProgram();
        onDrawPreview();
        particleProgram.setUniforms(viewProjectionMatrix, currentTime);
        particleSystem.bindData(particleProgram, particleLocation);
        particleSystem.draw();
        onDrawAfter();
    }

    @Override
    public void onDestroy() {
        if (particleProgram != null) {
            GLES20.glDeleteProgram(particleProgram.getPrograram());
        }
        GLES20.glDeleteTextures(textures.length, textures, 0);
        Arrays.fill(textures, -1);
    }

    @Override
    public void onPlepare(int index) {

    }

    @Override
    public void setMatrix(float[] matrix) {
        viewProjectionMatrix = matrix;
        particleProgram.setMatrix(matrix);
    }

    protected void setParticleSystem(RandomParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
    }

    public void setFlip(boolean flip) {
        isFlip = flip;
    }
}
