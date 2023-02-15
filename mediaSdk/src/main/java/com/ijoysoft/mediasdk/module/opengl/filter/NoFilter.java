package com.ijoysoft.mediasdk.module.opengl.filter;

import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;

import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.orthoM;

/**
 * Description:
 */
public class NoFilter extends AFilter {
    private final float[] projectionMatrix = new float[16];


    @Override
    protected void onCreate() {
        createProgramByAssetsFile(OpenGlUtils.uRes("shader/base_vertex.sh"),
                OpenGlUtils.uRes("shader/base_fragment.sh"));

//        float[]  OM= MatrixUtils.getOriginalMatrix();
//        MatrixUtils.flip(OM,false,true);//矩阵上下翻转
//        mFilter.setMatrix(OM);

//        float[] coord = new float[]{
//                0.0f, 1.0f,
//                0.0f, 0.0f,
//                1.0f, 1.0f,
//                1.0f, 0.0f,
//        };
//
//        mTexBuffer.clear();
//        mTexBuffer.put(coord);
//        mTexBuffer.position(0);
    }


    @Override
    public void draw() {
        glUniformMatrix4fv(mHMatrix, 1, false, projectionMatrix, 0);
        super.draw();
    }

    @Override
    public void onSizeChanged(int width, int height) {
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        if (width > height) {
            // Landscape
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            // Portrait or square
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }
}
