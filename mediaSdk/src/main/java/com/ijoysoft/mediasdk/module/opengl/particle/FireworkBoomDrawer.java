package com.ijoysoft.mediasdk.module.opengl.particle;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import java.util.List;

/**
 * 烟花爆炸效果，设定固定时间，前3层时间由白光图片缩小效果
 * 后七层时间由粒子做扩散效果，整体为一个周期时间，不做生命周期更新
 * 粒子的初始分布，有大块粒子分布再外围，并且加速系数大
 */
public class FireworkBoomDrawer implements IParticleRender {
    //    private BaseThemeExample flashBody;//主题颜色的闪烁
    private ParticleShaderProgram particleProgram;
    private FireworkBoomSystem particleSystem;
    private long globalStartTime;
    private int[] textures;
    private int[] textureLocations;
    private ParticleBuilder builder;
    //烟花整体的生命周期
    private List<ThemeWidgetInfo> widgetInfoList;
    private int periodTime;
    public static float BOOM_TIME = 0.3f;


    public FireworkBoomDrawer(ParticleBuilder builder) {
        this.builder = builder;
        textures = new int[builder.listBitmaps.size()];
        textureLocations = new int[builder.listBitmaps.size()];
        particleSystem = new FireworkBoomSystem(builder);
//        flashZoomFilter = new FlashZoomFilter();

    }

    public void setInfo(float centerX, float centerY, long periodTime) {
        this.periodTime = (int) periodTime;
        particleSystem.setCenter(centerX, centerY);
//        flashZoomFilter.setProgressEvaluate(new AnimationBuilder(flashTime)
//                .setCusStartEnd(0.4f, 0.99f)
//                .build());
    }

    public void setShowTime(List<ThemeWidgetInfo> widgetInfos) {
        this.widgetInfoList = widgetInfos;
    }

    @Override
    public void onSurfaceCreated() {
        particleProgram = new ParticleShaderProgram(createVexFragment(builder), createFragment(builder));
        globalStartTime = System.currentTimeMillis();
        for (int i = 0; i < builder.listBitmaps.size(); i++) {
            textures[i] = OpenGlUtils.loadTexture(builder.listBitmaps.get(i));
        }
        for (int i = 0; i < textureLocations.length; i++) {
            textureLocations[i] = GLES20.glGetUniformLocation(particleProgram.getPrograram(), "u_Texture" + (i));
        }
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
        particleProgram.useProgram();
        onDrawPreview();
        //更新时间
        particleProgram.setFloat(particleProgram.getuTimeLocation(), currentTime);
        particleSystem.onDrawFrame(particleProgram, currentTime);
        onDrawAfter();
        //检测是否再范围内
//        if (frameIndex < flashCount) {
//            flashZoomFilter.draw();
//            frameIndex++;
//        }
    }

    private boolean checkInRange(int position) {
        if (ObjectUtils.isEmpty(widgetInfoList)) {
            return false;
        }
        for (ThemeWidgetInfo widgetInfo : widgetInfoList) {
            if (widgetInfo.isInRange(position % periodTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 绘画走此入口
     * 不过当前的position要进行周期值的余运算
     *
     * @param position
     */
    public void onDrawFrame(int position) {
//        flashZoomFilter.setTextureId(textureId);
//        flashZoomFilter.setVertex(coord);
        if (checkInRange(position)) {
            onDrawFrame();
        }
    }

    /**
     * 循环使用
     */
    public void reset() {
        particleSystem.reset();
    }


    @Override
    public void onDestroy() {
        if (particleProgram != null) {
            GLES20.glDeleteProgram(particleProgram.getPrograram());
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
        particleProgram.setMatrix(matrix);
    }


    /**
     * 创建顶点
     *
     * @return
     */
    private String createVexFragment(ParticleBuilder builder) {
        StringBuffer sb = new StringBuffer();
        sb.append("#version 100\n " +
                "uniform mat4 u_Matrix;\n" +
                "uniform float u_Time;\n" +
                "attribute float a_Alpha;\n" +
                "attribute vec3 a_Color;\n" +
                "attribute vec3 a_Position;  \n" +
                "attribute float a_Paticle;\n" +
                "attribute vec3 a_DirectionVector;\n" +
                "attribute float a_ParticleStartTime;\n" +
                "attribute float a_PointSize;\n" +
                "varying float v_ElapsedTime;\n" +
                "varying float v_Alpha;\n" +
                "varying vec3 v_Color;\n" +
                "varying float v_Paticle;\n" +
                "void main()                    \n" +
                "{                                 \n" +
                "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
                "    float gravityFactor = v_ElapsedTime;\n" +
                "    vec3 currentPosition = a_Position + (a_DirectionVector * gravityFactor);\n" +
                "    gl_Position = vec4(currentPosition, 1.0);\n" +
                "    gl_PointSize = a_PointSize;\n" +
                "    v_Alpha = a_Alpha;\n" +
                "    v_Color = a_Color;\n" +
                "    v_Paticle=a_Paticle;\n" +
                "}\n");
        LogUtils.d("particle-vertex:", sb.toString());
        return sb.toString();
    }

    /**
     * 创建片段
     *
     * @return
     */
    private String createFragment(ParticleBuilder builder) {
        String alpha = builder.isAlpha ? "*v_Alpha;" : ";";
        String color = builder.isColor ? "vec4(v_Color, 1.0) *" : "";
        String selfRotate = builder.isSelfRotate ? "vec2( nx+0.5,ny+0.5)" : "gl_PointCoord";
        StringBuffer sb = new StringBuffer();
        sb.append("precision mediump float; \n");
        for (int i = 0; i < builder.listBitmaps.size(); i++) {
            sb.append("uniform sampler2D ").append("u_Texture").append(i).append("; \n");
        }
        sb.append("varying float v_ElapsedTime;\n" +
                "varying float v_Alpha; \n" +
                "varying float v_Paticle;\n" +
                "varying vec3 v_Color;\n" +
                "void main()\n" +
                "{\n");
        if (builder.isSelfRotate) {
            sb.append("    float x=gl_PointCoord.x-0.5;\n" +
                    "    float y=gl_PointCoord.y-0.5;\n" +
                    "    float nx=(cos(v_ElapsedTime)*x - sin(v_ElapsedTime )* y);\n" +
                    "    float ny = (sin( v_ElapsedTime )*x + cos( v_ElapsedTime )*y);\n");
        }
        sb.append("if(v_Paticle==0.0){\n")
                .append("gl_FragColor=").append(color).append("texture2D(u_Texture0,").append(selfRotate).append(")").append(alpha).append("\n}\n");
        if (builder.listBitmaps.size() > 1) {
            for (int i = 1; i < builder.listBitmaps.size(); i++) {
                sb.append("else if(v_Paticle==").append(i + ".0").append(")").append("{\n").append(" gl_FragColor= ").append(color).append("texture2D(")
                        .append("u_Texture" + i).append(",").append(selfRotate).append(")").append(alpha).append("}\n");
            }
        }
        sb.append("}");
        LogUtils.d("particle-fragment:", sb.toString());
        return sb.toString();
    }

}
