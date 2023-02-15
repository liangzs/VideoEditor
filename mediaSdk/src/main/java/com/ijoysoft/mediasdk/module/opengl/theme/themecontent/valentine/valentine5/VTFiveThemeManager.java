package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine5;

import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager;
import com.ijoysoft.mediasdk.module.opengl.theme.action.IAction;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;
import com.ijoysoft.mediasdk.module.opengl.transition.WipeTransitionFilter;

/**
 * @Override public void adjustImageScaling(int width, int height) {
 * adjustImageScalingStretch(width, height);
 * int w = bitmap.getWidth();
 * int h = bitmap.getHeight();
 * float sWH = w / (float) h;
 * float sWidthHeight = width / (float) height;
 * if (width > height) {
 * if (sWH > sWidthHeight) {
 * Matrix.orthoM(projectionMatrix, 0, -sWidthHeight * sWH, sWidthHeight * sWH, -1, 1, 3, 7);
 * } else {
 * Matrix.orthoM(projectionMatrix, 0, -sWidthHeight / sWH, sWidthHeight / sWH, -1, 1, 3, 7);
 * }
 * } else {
 * if (sWH > sWidthHeight) {
 * Matrix.orthoM(projectionMatrix, 0, -1, 1, -1 / sWidthHeight * sWH, 1 / sWidthHeight * sWH, 3, 7);
 * } else {
 * Matrix.orthoM(projectionMatrix, 0, -1, 1, -sWH / sWidthHeight, sWH / sWidthHeight, 3, 7);
 * }
 * }
 * //设置相机位置
 * Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
 * //计算变换矩阵
 * Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
 * }
 */
public class VTFiveThemeManager extends ThemeOpenglManager {



    @Override
    public TransitionType themeTransition() {
        return TransitionType.WIPE_LEFT;
    }
    @Override
    public IAction drawPrepareIndex(MediaItem mediaItem, int index, int width, int height) {
        switch (index % 6) {
            case 0:
                actionRender = new VTFiveThemeOne((int) mediaItem.getDuration(), true, width, height);
                break;
            case 1:
                actionRender = new VTFiveThemeTwo((int) mediaItem.getDuration(), true);
                break;
            case 2:
                actionRender = new VTFiveThemeThree((int) mediaItem.getDuration(), true);
                break;
            case 3:
                actionRender = new VTFiveThemeFour((int) mediaItem.getDuration(), true);
                break;
            case 4:
                actionRender = new VTFiveThemeFive((int) mediaItem.getDuration(), true);
                break;
            case 5:
                actionRender = new VTFiveThemeSix((int) mediaItem.getDuration(), true);
                break;
        }
        return actionRender;
    }
}
