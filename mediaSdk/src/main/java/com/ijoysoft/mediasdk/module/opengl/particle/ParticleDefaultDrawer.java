package com.ijoysoft.mediasdk.module.opengl.particle;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

/**
 * 简单集成通用型粒子渲染
 */
public class ParticleDefaultDrawer implements IParticleRender, Resettable {

    private ParticleShaderProgram particleProgram;
    private BaseParticleSystem particleSystem;
    private long globalStartTime;
    private int[] textures;
    private int[] textureLocations;
    private ParticleBuilder builder;
    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();

    public ParticleDefaultDrawer() {

    }

    public ParticleDefaultDrawer(ParticleBuilder builder) {
        this.builder = builder;
        textures = new int[builder.listBitmaps.size()];
        textureLocations = new int[builder.listBitmaps.size()];

        if (builder.customSystom != null) {
            particleSystem = builder.customSystom;
            particleSystem.setBuilder(builder);
        } else {
            particleSystem = new ParticleDefaultSystem(builder);
        }
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
//        particleProgram.onDrawPreview();
    }

    @Override
    public void onDrawFrame() {
        resettable = true;
        float currentTime = (System.currentTimeMillis() - globalStartTime) / 1000f;
        particleProgram.useProgram();
        onDrawPreview();
        //更新时间
        particleProgram.setMatrix(viewProjectionMatrix);
        particleProgram.setFloat(particleProgram.getuTimeLocation(), currentTime);
        particleSystem.onDrawFrame(particleProgram, currentTime);
        onDrawAfter();
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
        viewProjectionMatrix = matrix;
        if (particleProgram != null) {
            particleProgram.setMatrix(matrix);
        }
    }


    /**
     * 创建顶点
     *
     * @return
     */
    private String createVexFragment(ParticleBuilder builder) {
        StringBuffer sb = new StringBuffer();
        switch (builder.particleType) {
            case FALLING:
                sb.append(
                        "uniform mat4 u_Matrix;\n" +
                                "uniform float u_Time;\n" +
                                "attribute float a_Alpha;\n" +
                                "attribute vec3 a_Color;\n" +
                                "attribute vec3 a_Position;  \n" +
                                "attribute float a_Paticle;\n" +
                                "attribute vec3 a_DirectionVector;\n" +
                                "attribute float a_ParticleStartTime;\n" +
                                "attribute float a_PointSize;\n" +
                                "attribute float a_Accelerate;//加速度方向\n" +
                                "varying float v_ElapsedTime;\n" +
                                "varying float v_Alpha;\n" +
                                "varying vec3 v_Color;\n" +
                                "varying float v_Paticle;\n" +
                                "void main()                    \n" +
                                "{                                \t  \t  \n" +
                                "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
                                "    float gravityFactor = a_Accelerate*v_ElapsedTime * v_ElapsedTime / 18.0;\n" +
                                "    vec3 currentPosition = a_Position + (a_DirectionVector * a_Accelerate*v_ElapsedTime);\n" +
                                "    currentPosition.y += gravityFactor;\n" +
                                "    gl_Position = u_Matrix*vec4(currentPosition, 1.0);\n" +
                                "    gl_PointSize = a_PointSize;\n" +
                                "    v_Alpha = a_Alpha;\n" +
                                "    v_Color = a_Color;\n" +
                                "    v_Paticle=a_Paticle;\n" +
                                "}\n");

                break;
            case CENTER_EXPAND:
                sb.append(
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
                                "varying vec2 v_OutPosition;\n" +
                                "varying vec3 v_Color;\n" +
                                "varying float v_Paticle;\n" +
                                "void main()                    \n" +
                                "{                                 \n" +
                                "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
                                "    float gravityFactor = v_ElapsedTime;\n" +
                                "    vec3 currentPosition = a_Position;\n" +
                                "    gl_Position =u_Matrix*vec4(currentPosition, 1.0);\n" +
                                "    gl_PointSize = a_PointSize;\n" +
                                "    v_Alpha = a_Alpha;\n" +
                                "    v_Color = a_Color;\n" +
                                "    v_Paticle=a_Paticle;\n" +
                                "    v_OutPosition = a_Position.xy;\n" +
                                "}\n");
                break;
            case SCALE_LOOP:
                sb.append(
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
                                "    gl_Position =u_Matrix*vec4(a_Position, 1.0);\n" +
                                "    gl_PointSize = a_PointSize;\n" +
                                "    v_Alpha = a_Alpha;\n" +
                                "    v_Color = a_Color;\n" +
                                "    v_Paticle=a_Paticle;\n" +
                                "}\n");
                break;
            default:
                sb.append(
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
                                "    gl_Position =u_Matrix*vec4(currentPosition, 1.0);\n" +
                                "    gl_PointSize = a_PointSize;\n" +
                                "    v_Alpha = a_Alpha;\n" +
                                "    v_Color = a_Color;\n" +
                                "    v_Paticle=a_Paticle;\n" +
                                "}\n");
                break;
        }
        LogUtils.d("particle-vertex:", sb.toString());
        return sb.toString();
    }

    /**
     * 创建片段
     *
     * @return
     */
    private String createFragment(ParticleBuilder builder) {
        String alpha = "*v_Alpha;";
//        String alpha = builder.isAlpha ? "*v_Alpha;" : ";";
        String color = builder.isColor ? "vec4(v_Color, 1.0) *" : "";
        String coord = "gl_PointCoord";
        if (builder.flipTexture != null) {
            coord = builder.flipTexture.isFLip() ? "1.0-gl_PointCoord" : "gl_PointCoord";
        }
        String selfRotate = builder.isSelfRotate ? "vec2( nx+0.5,ny+0.5)" : coord;
        StringBuffer sb = new StringBuffer();
        sb.append("precision mediump float; \n");
        switch (builder.particleType) {
            case FALLING:
                for (int i = 0; i < builder.listBitmaps.size(); i++) {
                    sb.append("uniform sampler2D ").append("u_Texture").append(i).append("; \n");
                }
                sb.append("varying float v_ElapsedTime;\n" +
                        "varying float v_Alpha;\n" +
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
                        sb.append("else if(v_Paticle==").append(i + ".0").append(")").append("{\n").append(color).append(" gl_FragColor= texture2D(")
                                .append("u_Texture" + i).append(",").append(selfRotate).append(")").append(alpha).append("\n}\n");
                    }
                }
                sb.append("}");
                break;
            case CENTER_EXPAND:
                for (int i = 0; i < builder.listBitmaps.size(); i++) {
                    sb.append("uniform sampler2D ").append("u_Texture").append(i).append("; \n");
                }
                sb.append("varying float v_ElapsedTime;\n" +
                        "varying float v_Alpha; \n" +
                        "varying float v_Paticle;\n" +
                        "varying vec3 v_Color;\n" +
                        "varying vec2 v_OutPosition;\n" +
                        "void main()\n" +
                        "{\n");
                if (builder.isSelfRotate) {
                    sb.append("    float x=gl_PointCoord.x-0.5;\n" +
                            "    float y=gl_PointCoord.y-0.5;\n" +
                            "   float sita = v_ElapsedTime*3.0;\n" +
                            "   sita += acos(v_OutPosition.x);\n" +
                            "   if (asin(v_OutPosition.y) < 0.0) sita += 3.14159265358;\n" +
                            "    float nx=(cos(sita)*x - sin(sita)* y);\n" +
                            "    float ny = (sin(sita)*x + cos(sita)*y);\n");
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
                break;
            default:
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
                break;
        }
        LogUtils.d("particle-fragment:", sb.toString());
        return sb.toString();
    }

    @Override
    public void reset() {
        particleSystem.particles = new float[particleSystem.particles.length];
        particleSystem.currentParticleCount = 0;
        resettable = false;
//        LogUtils.i(getClass().getSimpleName(), "resetted");
    }


    boolean resettable = false;

    @Override
    public boolean isResettable() {
        return resettable;
    }
}
