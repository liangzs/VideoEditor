package com.ijoysoft.mediasdk.module.opengl.gpufilter.helper;

public enum MagicFilterType {
    NONE(0),
    BEAUTY(13),
    ANTIQUE(7),
    COOL(2),
    BRANNAN(8),
    SKETCH(4),
    PORTRAIT(5),
    HEFE(9),
    RETRO(6),
    HUDSON(3),
    INKWELL(10),
    N1977(11),
    NASHVILLE(12),
    WARM(1),
    SWEETY(14),
    ROSY(15),
    LOLITA(16),
    PINK(17),
    SUN_RISE(18),
    SUN_SET(19),
    VINTAGE(20),
    ROCOCO(21),
    ROMANCE(22),
    LOMO(23),
    LATTE(24),
    CALM(25),
    INVERT_COLOR(26),
    PIXELIZE(27),
    CRACKED(28);
    
    int index;
    
    MagicFilterType(int index) {
        this.index=index;
    }
    
    public int getIndex() {
        return index;
    }
}
