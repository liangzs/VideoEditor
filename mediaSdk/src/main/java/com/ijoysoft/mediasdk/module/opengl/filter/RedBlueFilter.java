package com.ijoysoft.mediasdk.module.opengl.filter;

import android.opengl.GLES20;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.utils.OpenGlUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.EmbeddedFilter;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionFilter;

/**
 * 转场应该作用于全显示范围，即showwidth、showHeight，所以定点坐标应该是满的，然后viewport应该赋值为showWidth,showHeight
 * @author hayring
 * fork from {@link TransitionFilter}
 */
public class RedBlueFilter extends EmbeddedFilter {

    public RedBlueFilter() {
        mVertexShader = OpenGlUtils.uRes(DEFAULT_VERTEX_PATH);
        mFragmentShader = OpenGlUtils.uRes(FRAGMENT_PATH);
    }


    private static final String FRAGMENT_PATH = "shader/redblue_transition_fragment.glsl";






}
