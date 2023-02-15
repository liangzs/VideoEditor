package com.ijoysoft.mediasdk.module.opengl.particle;

/**
 * 可重置的Render
 * @author hayring
 * @date 2022/1/11  17:32
 */
public interface Resettable {
    /**
     * 重置
     */
    void reset();

    /**
     * 能否重置
     */
    boolean isResettable();

}
