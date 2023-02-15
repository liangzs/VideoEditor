package com.ijoysoft.mediasdk.module.opengl.particle;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.Matrix.setIdentityM;

import static com.ijoysoft.mediasdk.module.opengl.particle.ShaderProgram.U_TEXTURE_UNIT;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 渲染执行形状星型，一字和心形
 * fork from {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenStarDrawer}
 */
public class StarDrawer implements IParticleRender {
    private static final String VERTEXT = "uniform mat4 u_Matrix;\n" +
            "attribute vec3 a_Position;\n" +
            "attribute vec3 a_Color;\n" +
            "attribute float a_PointSize;\n" +
            "attribute float a_Alpha;\n" +
            "attribute float a_Paticle;\n" +
            "varying float v_Alpha;\n" +
            "varying vec3 v_Color;\n" +
            "varying float v_Paticle;\n" +
            "\n" +
            "\n" +
            "void main()                    \n" +
            "{                                \t  \t  \n" +
            "   v_Paticle = a_Paticle;\n" +
            "    v_Color = a_Color;\n" +
            "    v_Alpha = a_Alpha;\n" +
            "    gl_PointSize = a_PointSize;\n" +
            "    gl_Position = u_Matrix * vec4(a_Position, 1.0);\n" +
            "}\n";

//    private static final String FRAGMENT = "precision mediump float; \n" +
//            "uniform sampler2D u_TextureUnit;\n" +
//            "varying float v_Alpha;\n" +
//            "varying float v_Paticle;\n" +
//            "void main()\n" +
//            "{\n" +
//            "   if (v_Paticle == 0.0) {" +
//            " gl_FragColor=texture2D(u_TextureUnit, gl_PointCoord)*v_Alpha;\n" +
//            "   }\n" +
//            "}\n";


    /**
     * glsl语句缓存
     * index：随机粒子种类数量
     * string: 对应数量的glsl语句
     */
    private static Map<Integer, String> fragmentCache = new HashMap<>();

    private static final String FRAGMENT_START = "precision mediump float";

    private static final String DEF_LINE = "; \nuniform sampler2D u_Texture";

    private static final String MID = ";\nvarying float v_Paticle;\n" +
            "varying float v_Alpha;\n" +
            "void main()                    \n" +
            "{\n";

    private static final String FRAGMENT_END = "}\n";

    private static final String FRAGMENT_LINE_ONE = " if(v_Paticle==";


    private static final String FRAGMENT_LINE_TWO = ".0){\n" +
            "       gl_FragColor=texture2D(u_Texture";


    private static final String FRAGMENT_LINE_THREE = ", gl_PointCoord)*v_Alpha;\n" +
            "    }";

    private static final String ELSE = " else ";


    private String fragment;


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
        sb.append(FRAGMENT_LINE_ONE).append(0).append(FRAGMENT_LINE_TWO).append(1).append(FRAGMENT_LINE_THREE);
        for (int i = 1; i < textures.length; i++) {
            sb.append(ELSE).append(FRAGMENT_LINE_ONE).append(i).append(FRAGMENT_LINE_TWO).append(i + 1).append(FRAGMENT_LINE_THREE);
        }
        sb.append(FRAGMENT_END);
        fragment = sb.toString();
        fragmentCache.put(textures.length, fragment);
//        LogUtils.i(getClass().getSimpleName(), "GLSL: \n" + fragment);
    }


    private final float[] viewProjectionMatrix = new float[16];
    private ParticleShaderProgram particleProgram;
    private StarSystem particleSystem;
    private long globalStartTime;
    private int texture;

    public StarDrawer(StarSystem particleSystem) {
        this.particleSystem = particleSystem;
        particleProgram = new ParticleShaderProgram(VERTEXT, fragment);
    }

    public StarDrawer() {
    }

    @Override
    public void onSurfaceCreated() {

    }

    /**
     * 画面比例更改监听器
     */
    private OnSurfaceChangedListener onSurfaceChangedListener = null;

//    /**
//     * 用更改图案监听器嵌入画面比例更改监听器
//     * @param changedListener 更改图案监听器
//     */
//    public void setOnSurfaceChangedListener(ChangeStarGraphListener changedListener) {
//        this.onSurfaceChangedListener = new OnSurfaceChangedListener() {
//
//            final ChangeStarGraphListener changeStarGraphListener = changedListener;
//
//            @Override
//            public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
//                changeStarGraphListener.onRatioChange(width, height, particleSystem);
//            }
//        };
//    }


    //    /**
//     * 比例变更导致图案变更监听器
//     */
//    public interface ChangeStarGraphListener {
//        void onRatioChange(int width, int height, StarSystem starSystem);
//    }
//
//
//
    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
//        if (onSurfaceChangedListener != null) {
//            onSurfaceChangedListener.onSurfaceChanged(offsetX, offsetY, width, height, screenWidth, screenHeight);
//        }
    }

    private int[] textures;
    private int[] textureLocations;

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
        particleProgram = new ParticleShaderProgram(VERTEXT, fragment);
        particleSystem.setTextureTypeCount(bitmaps.size());

        for (int i = 0; i < textures.length; i++) {
            textures[i] = OpenGlUtils.loadTexture(bitmaps.get(i));
        }

        for (int i = 0; i < textureLocations.length; i++) {
            textureLocations[i] = GLES20.glGetUniformLocation(particleProgram.getPrograram(), "u_Texture" + (1 + i));
        }

    }

//    public void init(Bitmap bitmap) {
//        texture = OpenGlUtils.loadTexture(bitmap, OpenGlUtils.NO_TEXTURE);
//        globalStartTime = System.currentTimeMillis();
//        particleSystem.addParticles();
//    }


    protected void onDrawAfter() {
        for (int i = 0; i < textures.length
                && textures[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + ((i + 3)));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }

        //先回收
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

    }

    protected void onDrawPreview() {
        for (int i = 0; i < textures.length
                && textures[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + ((i + 3)));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);
            GLES20.glUniform1i(textureLocations[i], ((i + 3)));
        }
//        int uTextureUnitLocation = glGetUniformLocation(particleProgram.program, U_TEXTURE_UNIT);
//        glActiveTexture(GLES20.GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, texture);
//        glUniform1i(uTextureUnitLocation, 0);
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
        GLES20.glDeleteTextures(1, new int[]{texture}, 0);
    }

    @Override
    public void onPlepare(int index) {

    }

    @Override
    public void setMatrix(float[] matrix) {
        particleProgram.setMatrix(matrix);
    }

    public void setParticleSystem(StarSystem particleSystem) {
        this.particleSystem = particleSystem;
    }

    public StarSystem getParticleSystem() {
        return particleSystem;
    }
}
