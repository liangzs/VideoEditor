package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine15;

import android.opengl.Matrix;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.opengl.theme.action.ActionStatus;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;

/**
 * 分解成四个步骤，进场，两个悬停动作，出场
 */
public class VTFifteenBaseExample extends BaseThemeExample {


    protected BaseEvaluate stayNext;
    protected int stayNextCount;
    /**
     * 平铺时间，占位剩余时间
     */
    protected int weightTime;

    public VTFifteenBaseExample(int duration, int enterTime, int stayTime, int stayNex, int outTime) {
        this.totalTime = duration;
        Matrix.perspectiveM(projectionMatrix, 0, 45, 1, 1f, 10f);
        status = ActionStatus.ENTER;
        frameIndex = 0;
        frameCount = (int) Math.ceil(duration * ConstantMediaSize.FPS / 1000f);
        stayCount = enterTime * ConstantMediaSize.FPS / 1000;
        weightTime = duration - enterTime - stayTime - outTime;
        weightTime = Math.max(stayTime, weightTime);
        stayNextCount = (enterTime + weightTime) * ConstantMediaSize.FPS / 1000;
        outCount = stayNextCount + stayNex * ConstantMediaSize.FPS / 1000;

        initWidget();
//        Log.i("test", "stayCount:" + stayCount + ",stayNextCount:" + stayNextCount + ",outCount:" + outCount);
    }


    /**
     * 精确到帧，跟图片源触发保持一致
     *
     * @return
     */
    @Override
    public void drawFrame() {
        drawFramePre();
//        Log.i("test", "status:" + status + ",frameIndex:" + frameIndex);
        switch (status) {
            case ENTER:
                if (enterAnimation != null) {
                    transitionOM = enterAnimation.draw();
                    isAlphaDraw = enterAnimation.checkAlphaDraw();
                    if (isAlphaDraw) {
                        alphaValue = enterAnimation.drawAlpha();
                    }
                }
                break;
            case STAY:
                if (stayAction == null) {
                    NoneStayAction();
                    break;
                }
                transitionOM = stayAction.draw();
                break;
            case STAT_NEXT:
                if (stayNext == null) {
                    NoneStayAction();
                    break;
                }
                transitionOM = stayNext.draw();
                break;
            case OUT:
                if (outAnimation != null) {
                    transitionOM = outAnimation.draw();
                    isAlphaDraw = outAnimation.checkAlphaDraw();
                    if (isAlphaDraw) {
                        alphaValue = outAnimation.drawAlpha();
                    }
                }
                break;
        }
        mViewMatrix = transitionOM;
        if (!isNoZaxis) {
            Matrix.multiplyMM(mViewMatrix, 0, projectionMatrix, 0, transitionOM, 0);
        }
        wedThemeSpecialDeal();
        setMatrix(mViewMatrix);
        if (isAlphaDraw) {
            setAlpha(alphaValue);
        }
        //这里实现滤镜的添加
        if (curFilter != null) {
            curFilter.setMatrix(mViewMatrix);
            curFilter.onDrawFrame(originTexture);
        } else {
            draw();
        }
        drawWiget();
        frameIndex++;
        if (frameIndex >= stayCount && status == ActionStatus.ENTER) {
            status = ActionStatus.STAY;
        }
        if (frameIndex >= stayNextCount && status == ActionStatus.STAY) {
            status = ActionStatus.STAT_NEXT;
        }
        if (frameIndex >= outCount && status == ActionStatus.STAT_NEXT) {
            status = ActionStatus.OUT;
        }
        if (frameIndex > stayNextCount) {
            drawStayNextLater();
        }
    }

    /**
     * 从staynext后开始绘画
     */
    protected void drawStayNextLater() {

    }

}
