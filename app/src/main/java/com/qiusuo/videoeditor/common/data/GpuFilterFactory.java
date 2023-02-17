package com.qiusuo.videoeditor.common.data;

import com.google.android.exoplayer2.offline.DownloadHelper;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.basefilter.GPUImageFilter;
import com.ijoysoft.mediasdk.module.opengl.gpufilter.helper.MagicFilterType;
import com.ijoysoft.mediasdk.module.opengl.theme.FilterConstant;
import com.qiusuo.videoeditor.R;
import com.qiusuo.videoeditor.common.bean.FilterEntity;
import com.qiusuo.videoeditor.common.constant.DownloadPath;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片编辑的GpuImageFilter的Factory
 * Created by luotangkang on 2019/5/22.
 */
public class GpuFilterFactory {


    private static class SingleTonHoler {
        private static GpuFilterFactory INSTANCE = new GpuFilterFactory();
    }

    public static GpuFilterFactory getInstance() {
        return SingleTonHoler.INSTANCE;
    }


    public static int getFilterSetPosition(MagicFilterType filterType) {
        if (filterType == null || filterType == MagicFilterType.NONE) {
            return 0;
        }
        switch (filterType.getType()) {
            case FilterConstant.PEOPLE:
                return 1;
            case FilterConstant.COLORFUL:
                return 2;
            case FilterConstant.NATURAL:
                return 3;
            case FilterConstant.FOOD:
                return 4;
            case FilterConstant.MOVIE:
                return 5;
            case FilterConstant.ART:
                return 6;
            case FilterConstant.SKY:
                return 7;
            case FilterConstant.DAILY:
                return 8;
            case FilterConstant.BW:
                return 9;
            case FilterConstant.DOWNLOAD:
                return 10;
            default:
                return 0;
        }
    }

    public List<MagicFilterType> getFilters(@FilterConstant.FilterType int type) {
        switch (type) {
            case FilterConstant.PEOPLE:
                return getPeopleFilter();
            case FilterConstant.COLORFUL:
                return getColorfulFilter();
            case FilterConstant.NATURAL:
                return getNaturalFilter();
            case FilterConstant.FOOD:
                return getFoodFilter();
            case FilterConstant.MOVIE:
                return getMovieFilter();
            case FilterConstant.ART:
                return getArtFilter();
            case FilterConstant.SKY:
                return getSkyFilter();
            case FilterConstant.DAILY:
                return getDailyFilter();
            case FilterConstant.BW:
                return getBWFilter();
            case FilterConstant.DOWNLOAD:
                return getDownloadFilter();
            case FilterConstant.POPULAR:
                return getPopularFilter();
            default:
                return getPeopleFilter();
        }
    }

    protected GPUImageFilter originalFilter;
    protected final ArrayList<MagicFilterType> dailyFilters = new ArrayList<>(19);
    protected final ArrayList<MagicFilterType> colorfulFilter = new ArrayList<>(17);
    protected final ArrayList<MagicFilterType> naturalFilter = new ArrayList<>(18);
    protected final ArrayList<MagicFilterType> foodFilters = new ArrayList<>(16);
    protected final ArrayList<MagicFilterType> movieFilters = new ArrayList<>(14);
    protected final ArrayList<MagicFilterType> artFilters = new ArrayList<>(18);
    protected final ArrayList<MagicFilterType> bwFilters = new ArrayList<>(9);
    protected final ArrayList<MagicFilterType> skyFilters = new ArrayList<>(17);
    protected final ArrayList<MagicFilterType> downloadFilters = new ArrayList<>(10);
    protected final ArrayList<MagicFilterType> peopleFilters = new ArrayList<>(8);
    protected final ArrayList<MagicFilterType> popularFilters = new ArrayList<>(8);


    public GPUImageFilter getOriginalFilter() {
        if (originalFilter == null) {
            originalFilter = new GPUImageFilter();
        }
        return originalFilter;
    }


    protected List<MagicFilterType> getDailyFilter() {
        if (dailyFilters.isEmpty()) {
            dailyFilters.add(MagicFilterType.HighWarm);
            dailyFilters.add(MagicFilterType.Normalwarm);
            dailyFilters.add(MagicFilterType.Nostalgia);
            dailyFilters.add(MagicFilterType.Yellow);
            dailyFilters.add(MagicFilterType.EveningGlow);
            dailyFilters.add(MagicFilterType.N1977);
            dailyFilters.add(MagicFilterType.SkinWhiten);
            dailyFilters.add(MagicFilterType.Sunrise);
            dailyFilters.add(MagicFilterType.Sunset);
            dailyFilters.add(MagicFilterType.Romance);
            dailyFilters.add(MagicFilterType.Nashville);
            dailyFilters.add(MagicFilterType.Lomo);
            dailyFilters.add(MagicFilterType.Pink);
            dailyFilters.add(MagicFilterType.Hudson);
            dailyFilters.add(MagicFilterType.Vintage);
            dailyFilters.add(MagicFilterType.Retro);
            dailyFilters.add(MagicFilterType.Lolita);
            dailyFilters.add(MagicFilterType.Rococo);
            dailyFilters.add(MagicFilterType.Rosy);
        }
        return dailyFilters;
    }

    /**
     * 人物
     *
     * @return
     */
    protected List<MagicFilterType> getPeopleFilter() {
        if (peopleFilters.isEmpty()) {
            peopleFilters.add(MagicFilterType.Beauty1);
            peopleFilters.add(MagicFilterType.Beauty2);
            peopleFilters.add(MagicFilterType.Beauty3);
            peopleFilters.add(MagicFilterType.Beauty4);
            peopleFilters.add(MagicFilterType.Beauty5);
            peopleFilters.add(MagicFilterType.Beauty6);
            peopleFilters.add(MagicFilterType.Beauty7);
            peopleFilters.add(MagicFilterType.Beauty8);
            peopleFilters.add(MagicFilterType.Beauty9);
            peopleFilters.add(MagicFilterType.Beauty10);
            peopleFilters.add(MagicFilterType.Beauty11);
            peopleFilters.add(MagicFilterType.Beauty12);
        }
        return peopleFilters;
    }

    /**
     * 多彩
     *
     * @return
     */
    protected List<MagicFilterType> getColorfulFilter() {
        if (colorfulFilter.isEmpty()) {
            colorfulFilter.add(MagicFilterType.MultiplyBlend1);
            colorfulFilter.add(MagicFilterType.MultiplyBlend2);
            colorfulFilter.add(MagicFilterType.MultiplyBlend3);
            colorfulFilter.add(MagicFilterType.MultiplyBlend4);
            colorfulFilter.add(MagicFilterType.MultiplyBlend5);
            colorfulFilter.add(MagicFilterType.MultiplyBlend6);
            colorfulFilter.add(MagicFilterType.MultiplyBlend7);
            colorfulFilter.add(MagicFilterType.MultiplyBlend8);
            colorfulFilter.add(MagicFilterType.MultiplyBlend9);
            colorfulFilter.add(MagicFilterType.MultiplyBlend10);
            colorfulFilter.add(MagicFilterType.MultiplyBlend11);
            colorfulFilter.add(MagicFilterType.MultiplyBlend12);
            colorfulFilter.add(MagicFilterType.MultiplyBlend13);
            colorfulFilter.add(MagicFilterType.MultiplyBlend14);
            colorfulFilter.add(MagicFilterType.MultiplyBlend15);
            colorfulFilter.add(MagicFilterType.MultiplyBlend16);
            colorfulFilter.add(MagicFilterType.MultiplyBlend17);
            colorfulFilter.add(MagicFilterType.MultiplyBlend18);
        }
        return colorfulFilter;
    }


    /**
     * 流行
     *
     * @return
     */
    protected List<MagicFilterType> getPopularFilter() {
        if (popularFilters.isEmpty()) {
            popularFilters.add(MagicFilterType.PopularBEAUTY);
            popularFilters.add(MagicFilterType.PopularANTIQUE);
            popularFilters.add(MagicFilterType.PopularCOOL);
            popularFilters.add(MagicFilterType.PopularBRANNAN);
            popularFilters.add(MagicFilterType.PopularSKETCH);
            popularFilters.add(MagicFilterType.PopularPORTRAIT);
            popularFilters.add(MagicFilterType.PopularHEFE);
            popularFilters.add(MagicFilterType.PopularRETRO);
            popularFilters.add(MagicFilterType.PopularHUDSON);
            popularFilters.add(MagicFilterType.PopularPopularINKWELL);
            popularFilters.add(MagicFilterType.PopularN1977);
            popularFilters.add(MagicFilterType.PopularNASHVILLE);
            popularFilters.add(MagicFilterType.PopularWARM);
            popularFilters.add(MagicFilterType.PopularSWEETY);
            popularFilters.add(MagicFilterType.PopularROSY);
            popularFilters.add(MagicFilterType.PopularLOLITA);
            popularFilters.add(MagicFilterType.PopularPINK);
            popularFilters.add(MagicFilterType.PopularSUNRISE);
            popularFilters.add(MagicFilterType.PopularSUNSET);
            popularFilters.add(MagicFilterType.PopularVINTAGE);
            popularFilters.add(MagicFilterType.PopularROCOCO);
            popularFilters.add(MagicFilterType.PopularROMANCE);
            popularFilters.add(MagicFilterType.PopularLOMO);
            popularFilters.add(MagicFilterType.PopularLATTE);
            popularFilters.add(MagicFilterType.PopularCALM);
            popularFilters.add(MagicFilterType.PopularINVERTCOLOR);
            popularFilters.add(MagicFilterType.PopularPIXELIZE);
            popularFilters.add(MagicFilterType.PopularCRACKED);
        }
        return popularFilters;
    }

    /**
     * 自然
     *
     * @return
     */
    protected List<MagicFilterType> getNaturalFilter() {
        if (naturalFilter.isEmpty()) {
            naturalFilter.add(MagicFilterType.Sakura);
            naturalFilter.add(MagicFilterType.Pixar);
            naturalFilter.add(MagicFilterType.Healthy);
            naturalFilter.add(MagicFilterType.Amaro);
            naturalFilter.add(MagicFilterType.Amaro1);
            naturalFilter.add(MagicFilterType.Amaro2);
            naturalFilter.add(MagicFilterType.Calm);
            naturalFilter.add(MagicFilterType.Brannan);
            naturalFilter.add(MagicFilterType.Rise);
            naturalFilter.add(MagicFilterType.Walden);
            naturalFilter.add(MagicFilterType.Walden1);
            naturalFilter.add(MagicFilterType.Hefe);
            naturalFilter.add(MagicFilterType.EarlyBird);
            naturalFilter.add(MagicFilterType.Kevin);
            naturalFilter.add(MagicFilterType.Toaster);
            naturalFilter.add(MagicFilterType.Tender);
            naturalFilter.add(MagicFilterType.Emerald);
            naturalFilter.add(MagicFilterType.Evergreen);
        }
        return naturalFilter;
    }


    protected List<MagicFilterType> getFoodFilter() {
        if (foodFilters.isEmpty()) {
            foodFilters.add(MagicFilterType.Lookup14);
            foodFilters.add(MagicFilterType.Lookup15);
            foodFilters.add(MagicFilterType.Lookup16);
            foodFilters.add(MagicFilterType.Lookup17);
            foodFilters.add(MagicFilterType.Lookup18);
            foodFilters.add(MagicFilterType.Lookup19);
            foodFilters.add(MagicFilterType.Lookup20);
            foodFilters.add(MagicFilterType.Lookup21);
            foodFilters.add(MagicFilterType.Lookup22);
            foodFilters.add(MagicFilterType.Lookup23);
            foodFilters.add(MagicFilterType.Lookup24);
            foodFilters.add(MagicFilterType.Lookup25);
            foodFilters.add(MagicFilterType.Lookup26);
            foodFilters.add(MagicFilterType.Lookup27);
            foodFilters.add(MagicFilterType.Lookup28);
            foodFilters.add(MagicFilterType.Lookup29);
        }
        return foodFilters;
    }


    protected List<MagicFilterType> getMovieFilter() {
        if (movieFilters.isEmpty()) {
            movieFilters.add(MagicFilterType.Lookup);
            movieFilters.add(MagicFilterType.Lookup1);
            movieFilters.add(MagicFilterType.Lookup2);
            movieFilters.add(MagicFilterType.Lookup3);
            movieFilters.add(MagicFilterType.Lookup4);
            movieFilters.add(MagicFilterType.Lookup5);
            movieFilters.add(MagicFilterType.Lookup6);
            movieFilters.add(MagicFilterType.Lookup7);
            movieFilters.add(MagicFilterType.Lookup8);
            movieFilters.add(MagicFilterType.Lookup9);
            movieFilters.add(MagicFilterType.Lookup10);
            movieFilters.add(MagicFilterType.Lookup11);
            movieFilters.add(MagicFilterType.Lookup12);
            movieFilters.add(MagicFilterType.Lookup13);
        }
        return movieFilters;
    }


    protected List<MagicFilterType> getArtFilter() {
        if (artFilters.isEmpty()) {
            artFilters.add(MagicFilterType.Thermal);
            artFilters.add(MagicFilterType.Ink);
            artFilters.add(MagicFilterType.Relief);
            artFilters.add(MagicFilterType.Crayon);
            artFilters.add(MagicFilterType.Frosted);
            artFilters.add(MagicFilterType.Pixelize);
            artFilters.add(MagicFilterType.TileMosaic);
            artFilters.add(MagicFilterType.NoiseWarp);
            artFilters.add(MagicFilterType.Triangles);
            artFilters.add(MagicFilterType.Logefied);
            artFilters.add(MagicFilterType.Wave);
            artFilters.add(MagicFilterType.EdgeDetection);
            artFilters.add(MagicFilterType.Distance);
            artFilters.add(MagicFilterType.BlueOrange);
            artFilters.add(MagicFilterType.Invert);
            artFilters.add(MagicFilterType.Interference);
            artFilters.add(MagicFilterType.Sketch);
            artFilters.add(MagicFilterType.Toon);
            artFilters.add(MagicFilterType.Cracke);
        }
        return artFilters;
    }


    protected List<MagicFilterType> getBWFilter() {
        if (bwFilters.isEmpty()) {
            bwFilters.add(MagicFilterType.BW1);
            bwFilters.add(MagicFilterType.BW2);
            bwFilters.add(MagicFilterType.BW3);
            bwFilters.add(MagicFilterType.BW4);
            bwFilters.add(MagicFilterType.BW5);
            bwFilters.add(MagicFilterType.Steel);
            bwFilters.add(MagicFilterType.Inkwell);
            bwFilters.add(MagicFilterType.Yellowing);
            bwFilters.add(MagicFilterType.SelectiveColor);
        }
        return bwFilters;
    }


    protected List<MagicFilterType> getSkyFilter() {
        if (skyFilters.isEmpty()) {
            skyFilters.add(MagicFilterType.HeightLight);
            skyFilters.add(MagicFilterType.WhiteCat);
            skyFilters.add(MagicFilterType.BlackCat);
            skyFilters.add(MagicFilterType.DayLight);
            skyFilters.add(MagicFilterType.Fog);
            skyFilters.add(MagicFilterType.Sutro);
            skyFilters.add(MagicFilterType.Sweets);
            skyFilters.add(MagicFilterType.Freud);
            skyFilters.add(MagicFilterType.Contract);
            skyFilters.add(MagicFilterType.Antique);
            skyFilters.add(MagicFilterType.Antique1);
            skyFilters.add(MagicFilterType.Cool);
            skyFilters.add(MagicFilterType.Brooklyn);
            skyFilters.add(MagicFilterType.Latte);
            skyFilters.add(MagicFilterType.Latte1);
            skyFilters.add(MagicFilterType.NashvilleGreen);
            skyFilters.add(MagicFilterType.NashvillePink);
        }
        return skyFilters;
    }

    protected List<MagicFilterType> getDownloadFilter() {
        if (downloadFilters.isEmpty()) {
            downloadFilters.add(MagicFilterType.Adore);
            downloadFilters.add(MagicFilterType.Washout);
            downloadFilters.add(MagicFilterType.WashoutColor);
            downloadFilters.add(MagicFilterType.Bleach);
            downloadFilters.add(MagicFilterType.BlueCrush);
            downloadFilters.add(MagicFilterType.Instant);
            downloadFilters.add(MagicFilterType.Process);
            downloadFilters.add(MagicFilterType.Punch);
        }
        return downloadFilters;
    }

    public static List<FilterEntity> getFilterSets() {
        ArrayList<FilterEntity> filterSets = new ArrayList<>();
        filterSets.add(new FilterEntity(R.string.filter_none, "N", R.drawable.vector_filter_none, 0xCC2E2623, FilterConstant.NONE));
        filterSets.add(new FilterEntity(R.string.filter_set_popular, "P", R.drawable.retro, 0xCC0090FF, FilterConstant.POPULAR));
        filterSets.add(new FilterEntity(R.string.filter_set_people, "P", R.drawable.filter_thumb_people, 0xCC35BDC7, FilterConstant.PEOPLE));
        filterSets.add(new FilterEntity(R.string.filter_set_colorful, "C", R.drawable.filter_thumb_colorful, 0xCCFF994B, FilterConstant.COLORFUL));
        filterSets.add(new FilterEntity(R.string.filter_set_natural, "N", R.drawable.filter_thumb_natural, 0xCC59B620, FilterConstant.NATURAL));
        filterSets.add(new FilterEntity(R.string.filter_set_food, "F", R.drawable.filter_thumb_food, 0xCC35BDC7, FilterConstant.FOOD));
        filterSets.add(new FilterEntity(R.string.filter_set_movie, "M", R.drawable.filter_thumb_movie, 0xCCFB8E67, FilterConstant.MOVIE));
        filterSets.add(new FilterEntity(R.string.filter_set_art, "A", R.drawable.filter_thumb_art, 0xCCA3AF59, FilterConstant.ART));
        filterSets.add(new FilterEntity(R.string.filter_set_sky, "S", R.drawable.filter_thumb_sky, 0xCCB69D41, FilterConstant.SKY));
        filterSets.add(new FilterEntity(R.string.filter_set_daily, "D", R.drawable.filter_thumb_daily, 0xCC57C78F, FilterConstant.DAILY));
        filterSets.add(new FilterEntity(R.string.filter_set_bw, "B", R.drawable.filter_thumb_bw, 0xCC4A4A4A, FilterConstant.BW));
        filterSets.add(new FilterEntity(R.string.filter_set_download, "D", R.drawable.filter_thumb_download, 0xCCD58FC7, FilterConstant.DOWNLOAD));
        return filterSets;
    }


    /**
     * 获取滤镜下载路径
     *
     * @param magicFilterType
     * @return
     */
    public static String getDownPath(MagicFilterType magicFilterType) {
        if (magicFilterType == null) {
            return "";
        }
        switch (magicFilterType) {
            case Adore:
                return DownloadPath.FILTER_URL[0];
            case Washout:
                return DownloadPath.FILTER_URL[1];
            case WashoutColor:
                return DownloadPath.FILTER_URL[2];
            case Bleach:
                return DownloadPath.FILTER_URL[3];
            case BlueCrush:
                return DownloadPath.FILTER_URL[4];
            case Instant:
                return DownloadPath.FILTER_URL[5];
            case Process:
                return DownloadPath.FILTER_URL[6];
            case Punch:
                return DownloadPath.FILTER_URL[7];
            default:
                break;
        }
        return "";
    }

    public static String getDownPathLocal(MagicFilterType magicFilterType) {
        return "";
//        return DownloadHelper.getDownloadPath(getDownPath(magicFilterType));
    }


}
