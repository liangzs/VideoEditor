package com.ijoysoft.mediasdk.module.opengl.particle;

/**
 * 简单封装一下粒子类型
 */
public enum ParticleType {


    //粒子类型
    FALLING(0, "飘落"),
    HOVER(1, "悬停"),
    EXPAND(2, "放大扩散"),
    //属于悬停的一种，因为太常用分离出来，用于片段内部
    FIX_COORD(3, "缩放旋转"),
    RANDOM_FADE(4, "随机渐变出现消失"),
    RANDOM(5, "随机移动"),
    ROTATE_IMAGE(6, "放大旋转"),
    SCALE_FADE(7, "烟花"),
    MOVING(8, "移动"),
    STAR_LINE(9, "线"),
    STAR_HEART(10, ""),
    START_V_SHADE(11, ""),
    FLYING(12, "模拟飞行"),
    CENTER_EXPAND(2, "中心扩散"),
    SCALE_LOOP(13, "缩放"),


    ;


    int type;
    String name;

    ParticleType(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
