
package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday2.particletwo;

import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.MatrixUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.particle.Geometry;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleShaderProgram;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.Matrix.setIdentityM;

/**
 * 不是一直触发，根据主题元素的出现，不一样的元素序列值展示不一样的值
 */
public class BDTwoDrawerTwo implements IParticleRender {
    private float[] viewProjectionMatrix = MatrixUtils.getOriginalMatrix();
    private ParticleShaderProgram particleProgram;
    private BDTwoSystemTwo particleSystem1;
    private BDTwoSystemTwo particleSystem2;
    private BDTwoShooterTwo particleShooter1;
    private BDTwoShooterTwo particleShooter2;
    private long globalStartTime;
    private int texture;


    public BDTwoDrawerTwo() {
    }

    @Override
    public void onSurfaceCreated() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Enable additive blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        particleProgram = new ParticleShaderProgram(R.raw.theme2_particle_vertex_shader,
                R.raw.theme2_particle_fragment_shader);
        particleSystem1 = new BDTwoSystemTwo(25);
        particleSystem2 = new BDTwoSystemTwo(25);
        globalStartTime = System.currentTimeMillis();
        texture = OpenGlUtils.loadTexture(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/bd_two_particle4"));

    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
    }

    @Override
    public void onDrawFrame() {
        float currentTime = (System.currentTimeMillis() - globalStartTime) / 1000f;
        if (particleShooter1 != null) {
            particleShooter1.addParticles(particleSystem1, currentTime);
        }
        if (particleShooter2 != null) {
            particleShooter2.addParticles(particleSystem2, currentTime);
        }
        particleProgram.useProgram();
         
        particleProgram.setUniforms(viewProjectionMatrix, currentTime, texture);
        particleSystem1.bindData(particleProgram);
        particleSystem1.draw();
        particleSystem2.bindData(particleProgram);
        particleSystem2.draw();
    }

    @Override
    public void onDestroy() {
        if(particleProgram!=null){
            GLES20.glDeleteProgram(particleProgram.getPrograram());
        }
        GLES20.glDeleteTextures(1, new int[]{texture}, 0);
    }

    @Override
    public void onPlepare(int index) {
        if (particleSystem1 == null) {
            return;
        }
        switch (index % 9) {
            case 0:
                globalStartTime = System.currentTimeMillis();
                List<Geometry.Point> points = new ArrayList<>();
                points.add(new Geometry.Point(-1.5f, 1.5f, 0));
                points.add(new Geometry.Point(1.5f, -1.5f, 0));
                particleShooter1 = new BDTwoShooterTwo(points,
                        4000, 25);
                particleShooter1.initParticles(particleSystem1);
                points = new ArrayList<>();
                points.add(new Geometry.Point(1.5f, 1.5f, 0));
                points.add(new Geometry.Point(-1.5f, -1.5f, 0));
                particleShooter2 = new BDTwoShooterTwo(points,
                        4000, 25);
                particleShooter2.initParticles(particleSystem2);
                break;
            case 2:
                globalStartTime = System.currentTimeMillis();
                points = new ArrayList<>();
                points.add(new Geometry.Point(-1.2f, -1.2f, 0));
                points.add(new Geometry.Point(0f, 0f, 0));
                points.add(new Geometry.Point(-1.2f, 0f, 0));
                particleShooter1 = new BDTwoShooterTwo(points,
                        4000, 25);
                particleShooter1.initParticles(particleSystem1);
                points = new ArrayList<>();
                points.add(new Geometry.Point(1.2f, -1.2f, 0));
                points.add(new Geometry.Point(0f, 0, 0));
                points.add(new Geometry.Point(1.2f, 0, 0));
                particleShooter2 = new BDTwoShooterTwo(points,
                        4000, 25);
                particleShooter2.initParticles(particleSystem2);
                break;
            case 4:
                globalStartTime = System.currentTimeMillis();
                points = new ArrayList<>();
                points.add(new Geometry.Point(-1f, -1f, 0));
                points.add(new Geometry.Point(1f, -0.34f, 0));
                points.add(new Geometry.Point(-1.2f, 0.32f, 0));
                points.add(new Geometry.Point(1f, 0.9f, 0));
                particleShooter1 = new BDTwoShooterTwo(points,
                        4000, 25);
                particleShooter1.initParticles(particleSystem1);
                particleShooter2 = null;
                break;
            case 6:
                globalStartTime = System.currentTimeMillis();
                points = new ArrayList<>();
                points.add(new Geometry.Point(-1.2f, -1.2f, 0));
                points.add(new Geometry.Point(1.2f, 1.2f, 0));
                particleShooter1 = new BDTwoShooterTwo(points,
                        3000, 25);
                points = new ArrayList<>();
                points.add(new Geometry.Point(1.2f, -1.2f, 0));
                points.add(new Geometry.Point(-1.2f, 1.2f, 0));
                particleShooter2 = new BDTwoShooterTwo(points,
                        3000, 25);
                break;
            case 7:
                globalStartTime = System.currentTimeMillis();
                points = new ArrayList<>();
                points.add(new Geometry.Point(-1.2f, -1.2f, 0));
                points.add(new Geometry.Point(0f, 0f, 0));
                points.add(new Geometry.Point(0f, 1.2f, 0));
                particleShooter1 = new BDTwoShooterTwo(points,
                        4000, 25);

                points = new ArrayList<>();
                points.add(new Geometry.Point(1.2f, -1.2f, 0));
                points.add(new Geometry.Point(0f, 0, 0));
                points.add(new Geometry.Point(0f, -1.2f, 0));
                particleShooter2 = new BDTwoShooterTwo(points,
                        4000, 25);
                break;
        }
    }

    @Override
    public void setMatrix(float[] matrix) {
        viewProjectionMatrix = matrix;
        particleProgram.setMatrix(matrix);
    }
}