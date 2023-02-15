package com.ijoysoft.mediasdk.module.entity;

public enum FrameType {
    _24F(0, 24),
    _25F(1, 25),
    _30F(2, 30),
    _50(3, 50),
    _60(4, 60),
    _DEFAULT_(100, 25)

    //
    ;

    private int id;
    private int frame;

    FrameType(int id, int frame) {
        this.id = id;
        this.frame = frame;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public static FrameType getFameType(int id) {
        for (FrameType frameType : FrameType.values()) {
            if (frameType.id == id) {
                return frameType;
            }
        }
        return _25F;
    }

    public static FrameType getFameTypeByName(String name) {
        for (FrameType frameType : FrameType.values()) {
            if (frameType.name().equals(name)) {
                return frameType;
            }
        }
        return _25F;
    }

    /**
     * 基础frame设置为25
     *
     * @return
     */
    public float getMatchFrameRatio() {
        return frame / 25f;
    }
}
