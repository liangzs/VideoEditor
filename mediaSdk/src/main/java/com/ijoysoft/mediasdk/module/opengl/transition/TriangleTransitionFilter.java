package com.ijoysoft.mediasdk.module.opengl.transition;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;

/**
 * 三角形状过渡
 */
public class TriangleTransitionFilter extends TransitionFilter {
    private int mPrograssLocation;
    private static final String FRAGMENT = "precision mediump float;\n" +
            "varying vec2 textureCoordinate;\n" +
            "uniform float progress;\n" +
            "uniform sampler2D vTexture;\n" +
            "uniform sampler2D vTexture1;\n" +
            "\n" +
            "float check(vec2 p1, vec2 p2, vec2 p3)\n" +
            "{\n" +
            "  return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);\n" +
            "}\n" +
            "\n" +
            "bool PointInTriangle (vec2 pt, vec2 p1, vec2 p2, vec2 p3)\n" +
            "{\n" +
            "    bool b1, b2, b3;\n" +
            "    b1 = check(pt, p1, p2) < 0.0;\n" +
            "    b2 = check(pt, p2, p3) < 0.0;\n" +
            "    b3 = check(pt, p3, p1) < 0.0;\n" +
            "    return ((b1 == b2) && (b2 == b3));\n" +
            "}\n" +
            "\n" +
            "bool in_left_triangle(vec2 p){\n" +
            "  vec2 vertex1, vertex2, vertex3;\n" +
            "  vertex1 = vec2(progress, 0.5);\n" +
            "  vertex2 = vec2(0.0, 0.5-progress);\n" +
            "  vertex3 = vec2(0.0, 0.5+progress);\n" +
            "  if (PointInTriangle(p, vertex1, vertex2, vertex3))\n" +
            "  {\n" +
            "    return true;\n" +
            "  }\n" +
            "  return false;\n" +
            "}\n" +
            "float blur_edge(vec2 bot1, vec2 bot2, vec2 top, vec2 testPt)\n" +
            "{\n" +
            "  vec2 lineDir = bot1 - top;\n" +
            "  vec2 perpDir = vec2(lineDir.y, -lineDir.x);\n" +
            "  vec2 dirToPt1 = bot1 - testPt;\n" +
            "  float dist1 = abs(dot(normalize(perpDir), dirToPt1));\n" +
            "  \n" +
            "  lineDir = bot2 - top;\n" +
            "  perpDir = vec2(lineDir.y, -lineDir.x);\n" +
            "  dirToPt1 = bot2 - testPt;\n" +
            "  float min_dist = min(abs(dot(normalize(perpDir), dirToPt1)), dist1);\n" +
            "  \n" +
            "  if (min_dist < 0.005) {\n" +
            "    return min_dist / 0.005;\n" +
            "  }\n" +
            "  else  {\n" +
            "    return 1.0;\n" +
            "  };\n" +
            "}\n" +
            "void main () {\n" +
            "  if (in_left_triangle(textureCoordinate))\n" +
            "  {\n" +
            " if (progress < 0.1)\n" +
            "    {\n" +
            "      gl_FragColor= texture2D(vTexture1, textureCoordinate);\n" +
            "       return;" +
            "    }" +
            "      vec2 vertex1 = vec2(progress, 0.5);\n" +
            "      vec2 vertex2 = vec2(0.0, 0.5-progress);\n" +
            "      vec2 vertex3 = vec2(0.0, 0.5+progress);\n" +
            "      gl_FragColor= mix(\n" +
            "        texture2D(vTexture1, textureCoordinate),\n" +
            "        texture2D(vTexture, textureCoordinate),\n" +
            "        blur_edge(vertex2, vertex3, vertex1, textureCoordinate));\n" +
            "  }else {\n" +
            "    gl_FragColor= texture2D(vTexture1, textureCoordinate);\n" +
            "  }\n" +
            "}";

    public TriangleTransitionFilter(TransitionType transitionType) {
        super(transitionType, NO_FILTER_VERTEX_SHADER, FRAGMENT);

    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mPrograssLocation = GLES20.glGetUniformLocation(mProgram, "progress");
        progress = 0;
    }

    /**
     * 这里采用超出值域的，以便三角形能走完，故设最大值为2
     */
    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        if (progress > 2.0) {
            return;
        }
        GLES20.glUniform1f(mPrograssLocation, progress);
        progress += 0.06;
        LogUtils.i(TAG, "onDrawArraysPre->" + progress);
    }

    @Override
    public void seekTo(int duration) {
        float d = (float) duration / 1000.0f;
        if (d >= 2.0f) {
            progress = 2.0f;
        } else {
            progress = d / 2;
        }
        LogUtils.i(TAG, "seekTo:" + progress + ",origin:" + duration);
        GLES20.glUniform1f(mPrograssLocation, progress);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }
}
