package com.ijoysoft.mediasdk.module.opengl.gpufilter.utils;


import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.MagicLookupFilter64;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.popular.MagicBrannanFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.popular.MagicCalmFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.popular.MagicCrackedFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.popular.MagicHefeFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.GPUImageToonFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicBlueOrangeFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicCrayonFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicDistanceFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicEdgeDetectionFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicFrostedFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicInkFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicInterferenceFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicInvertFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicLogefiedFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicNoiseWarpFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicPixelizeFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicReliefFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicSketchFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicThermalFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicTileMosaicFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicTrianglesMosaicFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.art.MagicWaveFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw.GpuSelectiveColorFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw.MagicBWFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw.MagicBWFilter1;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw.MagicBWFilter2;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw.MagicBWFilter3;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw.MagicBWFilter4;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw.MagicInkwellFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw.MagicSteelFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.bw.MagicYellowingFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.color.GPUImageMultiplyBlendFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicHudsonFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicLolitaFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicLomoFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicNashvilleFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicPinkFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicRococoFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicRomanceFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicRosyFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.beauty.MagicBeautyFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.beauty.MagicBeautyFilter1;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.beauty.MagicBeautyFilter2;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicRetroFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicVintageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicN1977Filter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicEveningGlowFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicHighWarmFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicNormalWarmFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicNostalgiaFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicSkinWhitenFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicSunriseFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicSunsetFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.daily.MagicYellowFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download.MagicAdoreFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download.MagicBleachFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download.MagicBlueCrushFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download.MagicInstantFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download.MagicProcessFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download.MagicPunchFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download.MagicWashoutColorFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.download.MagicWashoutFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicAmaroFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicAmaroFilter1;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicAmaroFilter2;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicEarlyBirdFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicEmeraldFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicEvergreenFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicHealthyFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicKevinFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicPixarFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicRiseFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicSakuraFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicTenderFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicToasterFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicWaldenFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.nature.MagicWaldenFilter1;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.popular.MagicWarmFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicAntiqueFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicBlackCatFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicBrooklynFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicContractFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicCoolFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicDayLightFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicFogFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicFreudFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicHeightLightFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicLatteFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicNoNameFilter3;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicNoNameFilter4;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicSutroFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicSweetsFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.filter.sky.MagicWhiteCatFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;

public class FilterHelper {

    private static MagicFilterType filterType = MagicFilterType.NONE;


    public static GPUImageFilter initFilters(MagicFilterType type, String downPath) {
        if (type == null) {
            return null;
        }
        filterType = type;
        switch (type) {
            /*daily 日程系列*/
            case HighWarm:
                return new MagicHighWarmFilter(MagicFilterType.HighWarm);
            case Normalwarm:
                return new MagicWarmFilter(MagicFilterType.Normalwarm);
            case Nostalgia:
                return new MagicNostalgiaFilter(MagicFilterType.Nostalgia);
            case Yellow:
                return new MagicYellowFilter(MagicFilterType.Yellow);
            case EveningGlow:
                return new MagicEveningGlowFilter(MagicFilterType.EveningGlow);
            case N1977:
                return new MagicN1977Filter(MagicFilterType.N1977);
            case SkinWhiten:
                return new MagicSkinWhitenFilter(MagicFilterType.SkinWhiten);
            case Sunrise:
                return new MagicSunriseFilter(MagicFilterType.Sunrise);
            case Sunset:
                return new MagicSunsetFilter(MagicFilterType.Sunset);
            case Romance:
                return new MagicRomanceFilter(MagicFilterType.Romance);
            case Nashville:
                return new MagicNashvilleFilter(MagicFilterType.Nashville);
            case Lomo:
                return new MagicLomoFilter(MagicFilterType.Lomo);
            case Pink:
                return new MagicPinkFilter(MagicFilterType.Pink);
            case Hudson:
                return new MagicHudsonFilter(MagicFilterType.Hudson);
            case Vintage:
                return new MagicVintageFilter(MagicFilterType.Vintage);
            case Retro:
                return new MagicRetroFilter(MagicFilterType.Retro);
            case Lolita:
                return new MagicLolitaFilter(MagicFilterType.Lolita);
            case Rococo:
                return new MagicRococoFilter(MagicFilterType.Rococo);
            case Rosy:
                return new MagicRosyFilter(MagicFilterType.Rosy);
            /*美肤系列*/

            case Beauty1:
                return new MagicBeautyFilter1(MagicFilterType.Beauty1);
            case Beauty2:
                return new MagicBeautyFilter2(MagicFilterType.Beauty2, 5);
            case Beauty3:
                return new MagicBeautyFilter2(MagicFilterType.Beauty2, 3);
            case Beauty4:
                return new MagicBeautyFilter(MagicFilterType.Beauty4, 0.33f, 0.63f, 0.4F, 0.35F);
            case Beauty5:
                return new MagicBeautyFilter(MagicFilterType.Beauty5, 0.33f, 0.63f, 0.4F, 0.6F);
            case Beauty6:
                return new MagicBeautyFilter(MagicFilterType.Beauty6, 0.33f, 0.63f, 0.7F, 0.35F);
            case Beauty7:
                return new MagicBeautyFilter(MagicFilterType.Beauty7, 0.33f, 0.63f, 0.7F, 0.6F);
            case Beauty8:
                return new MagicBeautyFilter(MagicFilterType.Beauty8, 0.33f, 0.3f, 0.7F, 0.6F);
            case Beauty9:
                return new MagicBeautyFilter(MagicFilterType.Beauty9, 0.0f, 0.99f, 0.7F, 0.6F);
            case Beauty10:
                return new MagicBeautyFilter(MagicFilterType.Beauty10, 0.33F, 0.63F, 0.4F, 0.0F);
            case Beauty11:
                return new MagicBeautyFilter(MagicFilterType.Beauty11, 0.33F, 0.63F, 1F, 0.0F);
            case Beauty12:
                return new MagicBeautyFilter(MagicFilterType.Beauty12, 0.33F, 0.63F, 0.4F, 1F);

            /*颜色系列*/
            case MultiplyBlend1:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend1, R.raw.blend1, 200);
            case MultiplyBlend2:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend2, R.raw.blend2, 200);
            case MultiplyBlend3:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend3, R.raw.blend3, 200);
            case MultiplyBlend4:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend4, R.raw.blend4, 200);
            case MultiplyBlend5:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend5, R.drawable.graduient_background0, 100);
            case MultiplyBlend6:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend6, R.drawable.graduient_background1, 100);
            case MultiplyBlend7:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend7, R.drawable.graduient_background2, 100);
            case MultiplyBlend8:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend8, R.drawable.graduient_background3, 100);
            case MultiplyBlend9:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend9, R.drawable.graduient_background4, 100);
            case MultiplyBlend10:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend10, R.drawable.graduient_background5, 100);
            case MultiplyBlend11:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend11, R.drawable.graduient_background6, 100);
            case MultiplyBlend12:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend12, R.drawable.graduient_background7, 100);
            case MultiplyBlend13:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend13, R.drawable.graduient_background8, 100);
            case MultiplyBlend14:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend14, R.drawable.graduient_background9, 100);
            case MultiplyBlend15:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend15, R.drawable.graduient_background10, 100);
            case MultiplyBlend16:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend16, R.drawable.graduient_background11, 100);
            case MultiplyBlend17:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend17, R.drawable.graduient_background12, 100);
            case MultiplyBlend18:
                return new GPUImageMultiplyBlendFilter(MagicFilterType.MultiplyBlend18, R.drawable.graduient_background13, 100);
            /*download系列*/
//            magicFairytaleFilter = new MagicFairytaleFilter(mContext, R.drawable.filter_thumb_download2);
//            magicVintageFilter = new MagicVintageFilter(mContext, R.drawable.filter_thumb_download3);
            case Adore:
                return new MagicAdoreFilter(MagicFilterType.Adore, downPath);
            case WashoutColor:
                return new MagicWashoutFilter(MagicFilterType.WashoutColor, downPath);
            case Bleach:
                return new MagicBleachFilter(MagicFilterType.Bleach, downPath);
            case BlueCrush:
                return new MagicBlueCrushFilter(MagicFilterType.BlueCrush, downPath);
            case Instant:
                return new MagicInstantFilter(MagicFilterType.Instant, downPath);
            case Process:
                return new MagicProcessFilter(MagicFilterType.Process, downPath);
            case Punch:
                return new MagicPunchFilter(MagicFilterType.Punch, downPath);
            case Washout:
                return new MagicWashoutColorFilter(MagicFilterType.Washout, downPath);

            /*sky 系列*/
            case HeightLight:
                return new MagicHeightLightFilter(MagicFilterType.HeightLight);
            case WhiteCat:
                return new MagicWhiteCatFilter(MagicFilterType.WhiteCat);
            case BlackCat:
                return new MagicBlackCatFilter(MagicFilterType.BlackCat);
            case DayLight:
                return new MagicDayLightFilter(MagicFilterType.DayLight);
            case Fog:
                return new MagicFogFilter(MagicFilterType.Fog);
            case Sutro:
                return new MagicSutroFilter(MagicFilterType.Sutro);
            case Sweets:
                return new MagicSweetsFilter(MagicFilterType.Sweets);
            case Freud:
                return new MagicFreudFilter(MagicFilterType.Freud);
            case Contract:
                return new MagicContractFilter(MagicFilterType.Contract);
            case Antique:
                return new MagicAntiqueFilter(MagicFilterType.Antique, 0.85098F);
            case Antique1:
                return new MagicAntiqueFilter(MagicFilterType.Antique, 0.1f);
            case Cool:
                return new MagicCoolFilter(MagicFilterType.Cool);
            case Brooklyn:
                return new MagicBrooklynFilter(MagicFilterType.Brooklyn);
            case Latte:
                return new MagicLatteFilter(MagicFilterType.Latte, 0.15f);
            case Latte1:
                return new MagicLatteFilter(MagicFilterType.Latte1, 0.8f);
            case NashvilleGreen:
                return new MagicNoNameFilter3(MagicFilterType.NashvilleGreen);
            case NashvillePink:
                return new MagicNoNameFilter4(MagicFilterType.NashvillePink);

            /*BW系列*/
            case BW1:
                return new MagicBWFilter(MagicFilterType.BW1);
            case BW2:
                return new MagicBWFilter1(MagicFilterType.BW2);
            case BW3:
                return new MagicBWFilter2(MagicFilterType.BW3);
            case BW4:
                return new MagicBWFilter3(MagicFilterType.BW4);
            case BW5:
                return new MagicBWFilter4(MagicFilterType.BW5);
            case Steel:
                return new MagicSteelFilter(MagicFilterType.Steel);
            case Inkwell:
                return new MagicInkwellFilter(MagicFilterType.Inkwell);
            case Yellowing:
                return new MagicYellowingFilter(MagicFilterType.Yellowing);
            case SelectiveColor:
                return new GpuSelectiveColorFilter(MagicFilterType.SelectiveColor);

            /*art系列*/
            case Thermal:
                return new MagicThermalFilter(MagicFilterType.Thermal);
            case Ink:
                return new MagicInkFilter(MagicFilterType.Ink);
            case Relief:
                return new MagicReliefFilter(MagicFilterType.Relief);
            case Crayon:
                return new MagicCrayonFilter(MagicFilterType.Crayon);
            case Frosted:
                return new MagicFrostedFilter(MagicFilterType.Frosted);
            case Pixelize:
                return new MagicPixelizeFilter(MagicFilterType.Pixelize);
            case TileMosaic:
                return new MagicTileMosaicFilter(MagicFilterType.TileMosaic);
            case NoiseWarp:
                return new MagicNoiseWarpFilter(MagicFilterType.NoiseWarp);
            case Triangles:
                return new MagicTrianglesMosaicFilter(MagicFilterType.Triangles);
            case Logefied:
                return new MagicLogefiedFilter(MagicFilterType.Logefied);
            case Wave:
                return new MagicWaveFilter(MagicFilterType.Wave);
            case EdgeDetection:
                return new MagicEdgeDetectionFilter(MagicFilterType.EdgeDetection);
            case Distance:
                return new MagicDistanceFilter(MagicFilterType.Distance);
            case BlueOrange:
                return new MagicBlueOrangeFilter(MagicFilterType.BlueOrange);
            case Invert:
                return new MagicInvertFilter(MagicFilterType.Invert);
            case Interference:
                return new MagicInterferenceFilter(MagicFilterType.Interference);
            case Sketch:
                return new MagicSketchFilter(MagicFilterType.Sketch);
            case Toon:
                return new GPUImageToonFilter(MagicFilterType.Toon);
            case Cracke:
                return new MagicCrackedFilter(MagicFilterType.Cracke);

            /*MOVIE*/
            case Lookup:
                return new MagicLookupFilter64(MagicFilterType.Lookup, R.raw.lut_movie1);
            case Lookup1:
                return new MagicLookupFilter64(MagicFilterType.Lookup1, R.raw.lut_movie2);
            case Lookup2:
                return new MagicLookupFilter64(MagicFilterType.Lookup2, R.raw.lut_movie3);
            case Lookup3:
                return new MagicLookupFilter64(MagicFilterType.Lookup3, R.raw.lut_movie4);
            case Lookup4:
                return new MagicLookupFilter64(MagicFilterType.Lookup4, R.raw.lut_movie5);
            case Lookup5:
                return new MagicLookupFilter64(MagicFilterType.Lookup5, R.raw.lut_movie6);
            case Lookup6:
                return new MagicLookupFilter64(MagicFilterType.Lookup6, R.raw.lut_movie7);
            case Lookup7:
                return new MagicLookupFilter64(MagicFilterType.Lookup7, R.raw.lut_movie8);
            case Lookup8:
                return new MagicLookupFilter64(MagicFilterType.Lookup8, R.raw.lut_movie9);
            case Lookup9:
                return new MagicLookupFilter64(MagicFilterType.Lookup9, R.raw.lut_movie10);
            case Lookup10:
                return new MagicLookupFilter64(MagicFilterType.Lookup10, R.raw.lut_movie11);
            case Lookup11:
                return new MagicLookupFilter64(MagicFilterType.Lookup11, R.raw.lut_movie12);
            case Lookup12:
                return new MagicLookupFilter64(MagicFilterType.Lookup12, R.raw.lut_movie13);
            case Lookup13:
                return new MagicLookupFilter64(MagicFilterType.Lookup13, R.raw.lut_movie14);

            /*FOOD*/
            case Lookup14:
                return new MagicLookupFilter64(MagicFilterType.Lookup14, R.raw.lut_food1);
            case Lookup15:
                return new MagicLookupFilter64(MagicFilterType.Lookup15, R.raw.lut_food2);
            case Lookup16:
                return new MagicLookupFilter64(MagicFilterType.Lookup16, R.raw.lut_food3);
            case Lookup17:
                return new MagicLookupFilter64(MagicFilterType.Lookup17, R.raw.lut_food4);
            case Lookup18:
                return new MagicLookupFilter64(MagicFilterType.Lookup18, R.raw.lut_food5);
            case Lookup19:
                return new MagicLookupFilter64(MagicFilterType.Lookup19, R.raw.lut_food6);
            case Lookup20:
                return new MagicLookupFilter64(MagicFilterType.Lookup20, R.raw.lut_food7);
            case Lookup21:
                return new MagicLookupFilter64(MagicFilterType.Lookup21, R.raw.lut_food8);
            case Lookup22:
                return new MagicLookupFilter64(MagicFilterType.Lookup22, R.raw.lut_food9);
            case Lookup23:
                return new MagicLookupFilter64(MagicFilterType.Lookup23, R.raw.lut_food10);
            case Lookup24:
                return new MagicLookupFilter64(MagicFilterType.Lookup24, R.raw.lut_food11);
            case Lookup25:
                return new MagicLookupFilter64(MagicFilterType.Lookup25, R.raw.lut_food12);
            case Lookup26:
                return new MagicLookupFilter64(MagicFilterType.Lookup26, R.raw.lut_food13);
            case Lookup27:
                return new MagicLookupFilter64(MagicFilterType.Lookup27, R.raw.lut_food14);
            case Lookup28:
                return new MagicLookupFilter64(MagicFilterType.Lookup28, R.raw.lut_food15);
            case Lookup29:
                return new MagicLookupFilter64(MagicFilterType.Lookup29, R.raw.lut_food16);

            /*NATURAL*/
            case Sakura:
                return new MagicSakuraFilter(MagicFilterType.Sakura);
            case Pixar:
                return new MagicPixarFilter(MagicFilterType.Pixar);
            case Healthy:
                return new MagicHealthyFilter(MagicFilterType.Healthy);
            case Amaro:
                return new MagicAmaroFilter(MagicFilterType.Amaro);
            case Amaro1:
                return new MagicAmaroFilter1(MagicFilterType.Amaro1);
            case Amaro2:
                return new MagicAmaroFilter2(MagicFilterType.Amaro2);
            case Calm:
                return new MagicCalmFilter(MagicFilterType.Calm);
            case Brannan:
                return new MagicBrannanFilter(MagicFilterType.Brannan);
            case Rise:
                return new MagicRiseFilter(MagicFilterType.Rise);
            case Walden:
                return new MagicWaldenFilter(MagicFilterType.Walden);
            case Walden1:
                return new MagicWaldenFilter1(MagicFilterType.Walden1);
            case Hefe:
                return new MagicHefeFilter(MagicFilterType.Hefe);
            case EarlyBird:
                return new MagicEarlyBirdFilter(MagicFilterType.EarlyBird);
            case Kevin:
                return new MagicKevinFilter(MagicFilterType.Kevin);
            case Toaster:
                return new MagicToasterFilter(MagicFilterType.Toaster);
            case Tender:
                return new MagicTenderFilter(MagicFilterType.Tender);
            case Emerald:
                return new MagicEmeraldFilter(MagicFilterType.Emerald);
            case Evergreen:
                return new MagicEvergreenFilter(MagicFilterType.Evergreen);

            /*popular 原有滤镜*/
            case PopularBEAUTY:
                return new MagicBeautyFilter1(MagicFilterType.PopularBEAUTY);
            case PopularANTIQUE:
                return new MagicAntiqueFilter(MagicFilterType.PopularANTIQUE, 0.85098F);
            case PopularCOOL:
                return new MagicCoolFilter(MagicFilterType.PopularCOOL);
            case PopularBRANNAN:
                return new MagicBrannanFilter(MagicFilterType.PopularBRANNAN);
            case PopularSKETCH:
                return new MagicSketchFilter(MagicFilterType.PopularSKETCH);
            case PopularPORTRAIT:
                return new MagicFreudFilter(MagicFilterType.PopularPORTRAIT);
            case PopularHEFE:
                return new MagicHefeFilter(MagicFilterType.PopularHEFE);
            case PopularRETRO:
                return new MagicRetroFilter(MagicFilterType.PopularRETRO);
            case PopularHUDSON:
                return new MagicHudsonFilter(MagicFilterType.PopularHUDSON);
            case PopularPopularINKWELL:
                return new MagicInkwellFilter(MagicFilterType.PopularPopularINKWELL);
            case PopularN1977:
                return new MagicN1977Filter(MagicFilterType.PopularN1977);
            case PopularNASHVILLE:
                return new MagicNashvilleFilter(MagicFilterType.PopularNASHVILLE);
            case PopularWARM:
                return new MagicBeautyFilter1(MagicFilterType.PopularBEAUTY);
            case PopularSWEETY:
                return new MagicSweetsFilter(MagicFilterType.PopularSWEETY);
            case PopularROSY:
                return new MagicRosyFilter(MagicFilterType.PopularROSY);
            case PopularLOLITA:
                return new MagicLolitaFilter(MagicFilterType.PopularLOLITA);
            case PopularPINK:
                return new MagicPinkFilter(MagicFilterType.PopularPINK);
            case PopularSUNRISE:
                return new MagicSunriseFilter(MagicFilterType.PopularSUNRISE);
            case PopularSUNSET:
                return new MagicSunsetFilter(MagicFilterType.PopularSUNSET);
            case PopularVINTAGE:
                return new MagicVintageFilter(MagicFilterType.PopularVINTAGE);
            case PopularROCOCO:
                return new MagicRococoFilter(MagicFilterType.PopularROCOCO);
            case PopularROMANCE:
                return new MagicRomanceFilter(MagicFilterType.PopularROMANCE);
            case PopularLOMO:
                return new MagicLomoFilter(MagicFilterType.PopularLOMO);
            case PopularLATTE:
                return new MagicLatteFilter(MagicFilterType.PopularLATTE, 0.15f);
            case PopularCALM:
                return new MagicCalmFilter(MagicFilterType.PopularCALM);
            case PopularINVERTCOLOR:
                return new MagicInvertFilter(MagicFilterType.PopularINVERTCOLOR);
            case PopularPIXELIZE:
                return new MagicPixelizeFilter(MagicFilterType.PopularPIXELIZE);
            case PopularCRACKED:
                return new MagicCrackedFilter(MagicFilterType.PopularCRACKED);
        }
        return null;
    }


    public MagicFilterType getCurrentFilterType() {
        return filterType;
    }

}
