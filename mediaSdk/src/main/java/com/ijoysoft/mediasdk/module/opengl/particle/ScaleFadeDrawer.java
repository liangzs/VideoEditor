package com.ijoysoft.mediasdk.module.opengl.particle;

import static android.opengl.Matrix.setIdentityM;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局烟花
 *
 * @author hayring
 * fork from: {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday6.HDSixDrawer}
 */
public class ScaleFadeDrawer implements IParticleRender {

    /**
     * glsl语句缓存
     * index：随机烟花种类数量
     * string: 对应数量的glsl语句
     */
    private static Map<Integer, String> fragmentCache = new HashMap<>();

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

    private static final String FRAGMENT_START = "precision mediump float; \n" +
            "varying float v_Paticle";

    private static final String DEF_LINE = ";\nuniform sampler2D u_Texture";

    private static final String MID =
            ";\n" +
                    "varying float v_Alpha;\n" +
                    "void main(){\n";

    private static final String FRAGMENT_END = "}\n";

    private static final String FRAGMENT_LINE_ONE =
            "  if(v_Paticle==";

    private static final String FRAGMENT_LINE_TWO = ".0){\n" +
            "       gl_FragColor=texture2D(u_Texture";
    private static final String FRAGMENT_LINE_THREE = ", vec2(gl_PointCoord.x, 1.0-gl_PointCoord.y))*v_Alpha;\n" +
            "    }";
    private static final String FRAGMENT_LINE_THREE_FLIP = ", vec2(gl_PointCoord.x,gl_PointCoord.y))*v_Alpha;\n" +
            "    }";
    private static final String ELSE = " else ";

    private String fragment;


    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    private ParticleShaderProgram particleProgram;
    private ScaleFadeSystem particleSystem;
    private long globalStartTime;
    private int[] textures;
    private int[] textureLocations;


    /**
     * 素材源
     */
    List<Bitmap> bitmaps;

    private boolean isFlip = true;

    /**
     * builder专用构造器
     */
    public ScaleFadeDrawer() {
    }

    /**
     * 初始化
     */
    void init(List<Bitmap> bitmaps) {
        this.bitmaps = new ArrayList<>(bitmaps.size());
        this.bitmaps.addAll(bitmaps);
        if (bitmaps.size() > 7) {
            LogUtils.w(getClass().getSimpleName(), "There are too much particle types");
        }
        textures = new int[bitmaps.size()];
        textureLocations = new int[bitmaps.size()];
        buildFragment();
        LogUtils.i(getClass().getSimpleName(), "ScaleFadeDrawer Createrd, size: " + bitmaps.size());
        particleProgram = new ParticleShaderProgram(VERTEX, fragment);


        for (int i = 0; i < textures.length; i++) {
            textures[i] = OpenGlUtils.loadTexture(bitmaps.get(i));
        }

        for (int i = 0; i < textureLocations.length; i++) {
            textureLocations[i] = GLES20.glGetUniformLocation(particleProgram.getPrograram(), "u_Texture" + (1 + i));
        }
    }


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
//        LogUtils.i(getClass().getSimpleName(), "GLSL: \n" + fragment);
    }

    @Override
    public void onSurfaceCreated() {
        onDrawAfter();
        globalStartTime = System.currentTimeMillis();
        particleSystem.addParticles();
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {

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
//        LogUtils.i(getClass().getSimpleName(), "draw");
        particleProgram.useProgram();
        onDrawPreview();

        particleSystem.updateParticle();


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
        for (int i = 0; i < textures.length; i++) {
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


    public void setParticleSystem(ScaleFadeSystem particleSystem) {
        this.particleSystem = particleSystem;
    }

    public boolean isFlip() {
        return isFlip;
    }

    public void setFlip(boolean flip) {
        isFlip = flip;
    }
}
