package com.ijoysoft.mediasdk.module.opengl.particle;

import static android.opengl.Matrix.setIdentityM;

import static com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils.NO_TEXTURE;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.playControl.GifDecoder;

import java.util.List;

/**
 * 1：1 gif drawer
 *
 * @author hayring
 * fork from: {@link com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration6.CDSixDrawer}
 */
public class GifDrawer implements IParticleRender {
//    private static final String VERTEXT = "uniform mat4 u_Matrix;\n" +
//            "uniform float u_Time;\n" +
//            "\n" +
//            "attribute vec3 a_Position;  \n" +
//            "attribute float a_ParticleStartTime;\n" +
//            "attribute float a_PointSize;\n" +
//            "varying float v_ElapsedTime;\n" +
//            "void main()\n" +
//            "{                                \t  \t  \n" +
//            "    v_ElapsedTime = u_Time - a_ParticleStartTime;\n" +
//            "    gl_Position = u_Matrix * vec4(a_Position, 1.0);\n" +
//            "    gl_PointSize = a_PointSize;\n" +
//            "}\n";
//
//    private static final String FRAGMENT = "precision mediump float; \n" +
//            "uniform sampler2D u_TextureUnit;\n" +
//            "varying float v_ElapsedTime;\n" +
//            "void main()                    \t\t\n" +
//            "{\n" +
//            "    float x=gl_PointCoord.x-0.5;\n" +
//            "    float y=gl_PointCoord.y-0.5;\n" +
//            "    float nx=(cos(v_ElapsedTime)*x - sin(v_ElapsedTime )* y);\n" +
//            "    float ny = (sin( v_ElapsedTime )*x + cos( v_ElapsedTime )*y);\n" +
//            "    gl_FragColor=texture2D(u_TextureUnit, vec2( nx+0.5,ny+0.5));\n" +
//            "}\n";

    private final String VERTEX = "uniform mat4 u_Matrix;\n" +
            "attribute vec3 a_Position;\n" +
            "attribute float a_PointSize;\n" +
            "\n" +
            "void main()                    \n" +
            "{                                \n" +
            "    gl_PointSize = a_PointSize;\n" +
            "    gl_Position = vec4(a_Position, 1.0);\n" +
            "}\n";

    private final String FRAGMENT = "#version 100\n" +
            "//gif fragment\n" +
            "precision mediump float; \n" +
            "uniform sampler2D vTexture;\n" +
            "void main()                    \n" +
            "{\n" +
            "   gl_FragColor = texture2D(vTexture, vec2(gl_PointCoord.x, 1.0-gl_PointCoord.y));\n" +
            "}\n";

    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();
    private ParticleShaderProgram particleProgram;
    private GifSystem particleSystem;
    private long globalStartTime;
    private int texture;


    public GifDrawer(float[][] points) {
        this.particleSystem = new GifSystem(points);
        particleProgram = new ParticleShaderProgram(VERTEX, FRAGMENT);
    }


    private List<GifDecoder.GifFrame> frames;


    /**
     * gif中帧的位置
     */
    private int frameIndex;


    /**
     * 过去的时间
     */
    private int passedMillisecond;


    /**
     * builder使用的空构造函数
     */
    public GifDrawer() {
    }

    @Override
    public void onSurfaceCreated() {
    }

    public void init(List<GifDecoder.GifFrame> gif) {
        texture = OpenGlUtils.loadTexture(gif.get(0).image, OpenGlUtils.NO_TEXTURE);
        globalStartTime = System.currentTimeMillis();
        particleSystem.addParticles();
        frames = gif;
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {

    }


    @Override
    public void onDrawFrame() {
        if (!ObjectUtils.isEmpty(frames)) {
            //获取下一帧
            GifDecoder.GifFrame frame = frames.get(frameIndex);
            float currentTime = (System.currentTimeMillis() - globalStartTime) / 1000f;
            particleProgram.useProgram();
             
            //加载下一帧
            OpenGlUtils.loadTexture(frame.image, texture);
            particleProgram.setUniforms(viewProjectionMatrix, currentTime, texture);//默认第一个纹理
            particleSystem.bindData(particleProgram);
            particleSystem.draw();

            passedMillisecond += ConstantMediaSize.PHTOTO_FPS_TIME;
            //指向下一帧
            while (passedMillisecond >= frame.delay) {
                passedMillisecond -= frame.delay;
                nextFrame();
            }
        }


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
        viewProjectionMatrix = matrix;
        particleProgram.setMatrix(matrix);
    }


    public void setParticleSystem(GifSystem particleSystem) {
        this.particleSystem = particleSystem;
    }


    private void nextFrame() {
        frameIndex = (frameIndex + 1) % frames.size();
    }
}
