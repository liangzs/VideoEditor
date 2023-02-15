package com.ijoysoft.mediasdk.module.opengl.theme;

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.entity.DoodleItem;
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseThemeExample;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1.textdoodle.WeddingTextOne;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1.textdoodle.WeddingTextThree;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1.textdoodle.WeddingTextTwo;
import com.ijoysoft.mediasdk.module.playControl.IRender;

import java.util.ArrayList;
import java.util.List;

/**
 * 文字的显示应根据时间周期，依次显示
 */
public class ThemeTextDoodleManager implements IRender {
    private List<DoodleItem> doodleLists;
    private List<BaseThemeExample> textRenders;//需要整体进行初始化，然后直接调用

    public ThemeTextDoodleManager() {
        textRenders = new ArrayList<>();
        doodleLists = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            DoodleItem doodleItem = new DoodleItem();
//            doodleItem.setDurationInterval(new DurationInterval(i * 6000, (i + 1) * 6000));
//            doodleLists.add(doodleItem);
//        }
        textRenders.add(new WeddingTextOne(6000));
        textRenders.add(new WeddingTextTwo(6000, ConstantMediaSize.showViewWidth, ConstantMediaSize.showViewHeight));
        textRenders.add(new WeddingTextThree(6000, ConstantMediaSize.showViewWidth, ConstantMediaSize.showViewHeight));
    }

    public void onDrawFrame(int durationPosition) {
        for (int i = 0; i < doodleLists.size(); i++) {
            if (doodleLists.get(i).getDurationInterval().isInRange(durationPosition)) {
//                Log.i("test", "durationPosition:" + durationPosition);
                textRenders.get(i).drawFrame(durationPosition);
            }
        }
    }


    @Override
    public void onSurfaceCreated() {
        for (BaseThemeExample render : textRenders) {
            render.create();
        }
    }

    @Override
    public void onSurfaceChanged(int offsetX, int offsetY, int width, int height, int screenWidth, int screenHeight) {
        for (BaseThemeExample render : textRenders) {
            render.onSizeChanged(width, height);
        }
    }

    @Override
    public void onDrawFrame() {

    }

    public void reset() {
        for (BaseThemeExample render : textRenders) {
            render.prepare();
        }
    }

    @Override
    public void onDestroy() {
        for (BaseThemeExample render : textRenders) {
            render.onDestroy();
        }
    }


    public List<DoodleItem> getDoodleLists() {
        return doodleLists;
    }

    public void setDoodleLists(List<DoodleItem> doodleLists) {
        this.doodleLists = doodleLists;
    }
}
