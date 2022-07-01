package com.ijoysoft.mediasdk.module.opengl.gpufilter.helper;


import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicAntiqueFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicBeautyFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicBrannanFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicCalmFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicCoolFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicCrackedFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicFreudFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicHefeFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicHudsonFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicInkwellFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicInvertColorFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicLatteFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicLolitaFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicLomoFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicN1977Filter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicNashvilleFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicPinkFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicPixelizeFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicRetroFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicRococoFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicRomanceFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicRosyFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicSketchFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicSunRiseFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicSunsetFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicSweetyFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicVintageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.MagicWarmFilter;

public class MagicFilterFactory {

    private static MagicFilterType filterType = MagicFilterType.NONE;

    public static GPUImageFilter initFilters(MagicFilterType type) {
        if (type == null) {
            return null;
        }
        filterType = type;
        switch (type) {
            case ANTIQUE:
                return new MagicAntiqueFilter(MagicFilterType.ANTIQUE);
            case BRANNAN:
                return new MagicBrannanFilter(MagicFilterType.BRANNAN);
            case SKETCH:
                return new MagicSketchFilter(MagicFilterType.SKETCH);
            case PORTRAIT:
                return new MagicFreudFilter(MagicFilterType.PORTRAIT);
            case HEFE:
                return new MagicHefeFilter(MagicFilterType.HEFE);
            case HUDSON:
                return new MagicHudsonFilter(MagicFilterType.HUDSON);
            case INKWELL:
                return new MagicInkwellFilter(MagicFilterType.INKWELL);
            case N1977:
                return new MagicN1977Filter(MagicFilterType.N1977);
            case NASHVILLE:
                return new MagicNashvilleFilter(MagicFilterType.NASHVILLE);
            case COOL:
                return new MagicCoolFilter(MagicFilterType.COOL);
            case WARM:
                return new MagicWarmFilter(MagicFilterType.WARM);
            case BEAUTY:
                return new MagicBeautyFilter(MagicFilterType.BEAUTY);
            case SWEETY:
                return new MagicSweetyFilter(MagicFilterType.SWEETY);
            case ROSY:
                return new MagicRosyFilter(MagicFilterType.ROSY);
            case LOLITA:
                return new MagicLolitaFilter(MagicFilterType.LOLITA);
            case PINK:
                return new MagicPinkFilter(MagicFilterType.PINK);
            case SUN_RISE:
                return new MagicSunRiseFilter(MagicFilterType.SUN_RISE);
            case SUN_SET:
                return new MagicSunsetFilter(MagicFilterType.SUN_SET);
            case VINTAGE:
                return new MagicVintageFilter(MagicFilterType.VINTAGE);
            case ROCOCO:
                return new MagicRococoFilter(MagicFilterType.ROCOCO);
            case ROMANCE:
                return new MagicRomanceFilter(MagicFilterType.ROMANCE);
            case RETRO:
                return new MagicRetroFilter(MagicFilterType.RETRO);
            case LOMO:
                return new MagicLomoFilter(MagicFilterType.LOMO);
            case LATTE:
                return new MagicLatteFilter(MagicFilterType.LATTE);
            case CALM:
                return new MagicCalmFilter(MagicFilterType.CALM);
            case INVERT_COLOR:
                return new MagicInvertColorFilter(MagicFilterType.INVERT_COLOR);
            case PIXELIZE:
                return new MagicPixelizeFilter(MagicFilterType.PIXELIZE);
            case CRACKED:
                return new MagicCrackedFilter(MagicFilterType.CRACKED);
            default:
                return new GPUImageFilter();
        }
    }

    public MagicFilterType getCurrentFilterType() {
        return filterType;
    }

}
