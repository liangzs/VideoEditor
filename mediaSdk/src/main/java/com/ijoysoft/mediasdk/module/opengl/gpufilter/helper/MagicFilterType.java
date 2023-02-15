package com.ijoysoft.mediasdk.module.opengl.gpufilter.helper;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.module.opengl.theme.FilterConstant;

import java.util.Random;

public enum MagicFilterType {


    NONE(R.drawable.filter_thumb_daily, R.string.filter_name_none, 0),

    HighWarm(R.drawable.filter_thumb_daily1, R.string.filter_name_story, FilterConstant.DAILY),
    Normalwarm(R.drawable.filter_thumb_daily2, R.string.filter_name_warm, FilterConstant.DAILY),
    Nostalgia(R.drawable.filter_thumb_daily3, R.string.filter_name_nostalgia, FilterConstant.DAILY),
    Yellow(R.drawable.filter_thumb_daily4, R.string.filter_name_yellow, FilterConstant.DAILY),
    EveningGlow(R.drawable.filter_thumb_daily6, R.string.filter_name_glow, FilterConstant.DAILY),
    N1977(R.drawable.filter_thumb_daily5, R.string.filter_name_n1997, FilterConstant.DAILY),
    SkinWhiten(R.drawable.filter_thumb_daily7, R.string.filter_name_skinwhiten, FilterConstant.DAILY),
    Sunrise(R.drawable.filter_thumb_daily8, R.string.filter_name_sunrise, FilterConstant.DAILY),
    Sunset(R.drawable.filter_thumb_daily9, R.string.filter_name_sunset, FilterConstant.DAILY),
    Romance(R.drawable.filter_thumb_daily10, R.string.filter_name_romance, FilterConstant.DAILY),
    Nashville(R.drawable.filter_thumb_daily11, R.string.filter_name_nashville, FilterConstant.DAILY),
    Lomo(R.drawable.filter_thumb_daily12, R.string.filter_name_lomo, FilterConstant.DAILY),
    Pink(R.drawable.filter_thumb_daily13, R.string.filter_name_spring, FilterConstant.DAILY),
    Hudson(R.drawable.filter_thumb_daily14, R.string.filter_name_hudson, FilterConstant.DAILY),
    Vintage(R.drawable.filter_thumb_daily15, R.string.filter_name_vintage, FilterConstant.DAILY),
    Retro(R.drawable.filter_thumb_daily16, R.string.filter_name_retro, FilterConstant.DAILY),
    Lolita(R.drawable.filter_thumb_daily17, R.string.slideshow_filter_lolita, FilterConstant.DAILY),
    Rococo(R.drawable.filter_thumb_daily18, R.string.slideshow_filter_rococo, FilterConstant.DAILY),
    Rosy(R.drawable.filter_thumb_daily19, R.string.filter_name_rosy, FilterConstant.DAILY),

    Beauty1(R.drawable.filter_thumb_people1, R.string.filter_beauty, FilterConstant.PEOPLE),
    Beauty2(R.drawable.filter_thumb_people2, R.string.filter_name_depth, FilterConstant.PEOPLE),
    Beauty3(R.drawable.filter_thumb_people3, R.string.filter_name_pure, FilterConstant.PEOPLE),
    Beauty4(R.drawable.filter_thumb_people4, R.string.filter_name_whiten, FilterConstant.PEOPLE),
    Beauty5(R.drawable.filter_thumb_people5, R.string.filter_name_pink, FilterConstant.PEOPLE),
    Beauty6(R.drawable.filter_thumb_people6, R.string.filter_name_cherry, FilterConstant.PEOPLE),
    Beauty7(R.drawable.filter_thumb_people7, R.string.filter_name_sugar, FilterConstant.PEOPLE),
    Beauty8(R.drawable.filter_thumb_people8, R.string.filter_name_refreshing, FilterConstant.PEOPLE),
    Beauty9(R.drawable.filter_thumb_people9, R.string.filter_name_rosy, FilterConstant.PEOPLE),
    Beauty10(R.drawable.filter_thumb_people10, R.string.filter_name_gleam, FilterConstant.PEOPLE),
    Beauty11(R.drawable.filter_thumb_people11, R.string.filter_name_clean, FilterConstant.PEOPLE),
    Beauty12(R.drawable.filter_thumb_people12, R.string.filter_name_ruddy, FilterConstant.PEOPLE),


    MultiplyBlend1(R.drawable.filter_thumb_colorful1, R.string.filter_name_violet, FilterConstant.COLORFUL),
    MultiplyBlend2(R.drawable.filter_thumb_colorful2, R.string.filter_name_orange, FilterConstant.COLORFUL),
    MultiplyBlend3(R.drawable.filter_thumb_colorful3, R.string.filter_name_light_brown, FilterConstant.COLORFUL),
    MultiplyBlend4(R.drawable.filter_thumb_colorful4, R.string.filter_name_light_wood, FilterConstant.COLORFUL),
    MultiplyBlend5(R.drawable.filter_thumb_colorful5, R.string.filter_name_light_red, FilterConstant.COLORFUL),
    MultiplyBlend6(R.drawable.filter_thumb_colorful6, R.string.filter_name_lime1, FilterConstant.COLORFUL),
    MultiplyBlend7(R.drawable.filter_thumb_colorful7, R.string.filter_name_cranberry, FilterConstant.COLORFUL),
    MultiplyBlend8(R.drawable.filter_thumb_colorful8, R.string.filter_name_gold_orange, FilterConstant.COLORFUL),
    MultiplyBlend9(R.drawable.filter_thumb_colorful9, R.string.filter_name_wood_color, FilterConstant.COLORFUL),
    MultiplyBlend10(R.drawable.filter_thumb_colorful10, R.string.filter_name_fire_brick, FilterConstant.COLORFUL),
    MultiplyBlend11(R.drawable.filter_thumb_colorful11, R.string.filter_name_trendy_pink, FilterConstant.COLORFUL),
    MultiplyBlend12(R.drawable.filter_thumb_colorful12, R.string.filter_name_olive_drab, FilterConstant.COLORFUL),
    MultiplyBlend13(R.drawable.filter_thumb_colorful13, R.string.filter_name_light_sea, FilterConstant.COLORFUL),
    MultiplyBlend14(R.drawable.filter_thumb_colorful14, R.string.filter_name_teal, FilterConstant.COLORFUL),
    MultiplyBlend15(R.drawable.filter_thumb_colorful15, R.string.filter_name_sea_green, FilterConstant.COLORFUL),
    MultiplyBlend16(R.drawable.filter_thumb_colorful16, R.string.filter_name_sea_salt, FilterConstant.COLORFUL),
    MultiplyBlend17(R.drawable.filter_thumb_colorful17, R.string.filter_name_dark_cyan, FilterConstant.COLORFUL),
    MultiplyBlend18(R.drawable.filter_thumb_colorful18, R.string.filter_name_steel_blue, FilterConstant.COLORFUL),


    Adore(R.drawable.filter_thumb_download1, R.string.filter_name_adore, FilterConstant.DOWNLOAD),
    WashoutColor(R.drawable.filter_thumb_download5, R.string.filter_name_litho, FilterConstant.DOWNLOAD),
    Bleach(R.drawable.filter_thumb_download6, R.string.filter_name_bleach, FilterConstant.DOWNLOAD),
    BlueCrush(R.drawable.filter_thumb_download7, R.string.filter_name_blue, FilterConstant.DOWNLOAD),
    Instant(R.drawable.filter_thumb_download8, R.string.filter_name_instant, FilterConstant.DOWNLOAD),
    Process(R.drawable.filter_thumb_download9, R.string.filter_name_process, FilterConstant.DOWNLOAD),
    Punch(R.drawable.filter_thumb_download10, R.string.filter_name_punch, FilterConstant.DOWNLOAD),
    Washout(R.drawable.filter_thumb_download4, R.string.filter_name_latte, FilterConstant.DOWNLOAD),


    HeightLight(R.drawable.filter_thumb_sky1, R.string.filter_name_height_light, FilterConstant.SKY),
    WhiteCat(R.drawable.filter_thumb_sky2, R.string.filter_name_whitecat, FilterConstant.SKY),
    BlackCat(R.drawable.filter_thumb_sky3, R.string.filter_name_blackcat, FilterConstant.SKY),
    DayLight(R.drawable.filter_thumb_sky4, R.string.filter_name_daylight, FilterConstant.SKY),
    Fog(R.drawable.filter_thumb_sky5, R.string.filter_name_fog, FilterConstant.SKY),
    Sutro(R.drawable.filter_thumb_sky6, R.string.filter_name_sutro, FilterConstant.SKY),
    Sweets(R.drawable.filter_thumb_sky7, R.string.filter_name_sweets, FilterConstant.SKY),
    Freud(R.drawable.filter_thumb_sky8, R.string.filter_name_freud, FilterConstant.SKY),
    Contract(R.drawable.filter_thumb_sky9, R.string.filter_name_contract, FilterConstant.SKY),
    Antique(R.drawable.filter_thumb_sky10, R.string.filter_name_antique, FilterConstant.SKY),
    Antique1(R.drawable.filter_thumb_sky11, R.string.filter_name_ice, FilterConstant.SKY),
    Cool(R.drawable.filter_thumb_sky12, R.string.filter_name_cool, FilterConstant.SKY),
    Brooklyn(R.drawable.filter_thumb_sky13, R.string.filter_name_brooklyn, FilterConstant.SKY),
    Latte(R.drawable.filter_thumb_sky14, R.string.filter_name_cozy, FilterConstant.SKY),
    Latte1(R.drawable.filter_thumb_sky15, R.string.filter_name_tropic, FilterConstant.SKY),
    NashvilleGreen(R.drawable.filter_thumb_sky16, R.string.filter_name_rain, FilterConstant.SKY),
    NashvillePink(R.drawable.filter_thumb_sky17, R.string.filter_name_dry, FilterConstant.SKY),


    BW1(R.drawable.filter_thumb_bw1, R.string.filter_name_classical, FilterConstant.BW),
    BW2(R.drawable.filter_thumb_bw2, R.string.filter_name_vast, FilterConstant.BW),
    BW3(R.drawable.filter_thumb_bw3, R.string.filter_name_gray, FilterConstant.BW),
    BW4(R.drawable.filter_thumb_bw4, R.string.filter_name_decade, FilterConstant.BW),
    BW5(R.drawable.filter_thumb_bw5, R.string.filter_name_black, FilterConstant.BW),
    Steel(R.drawable.filter_thumb_bw6, R.string.filter_name_pale, FilterConstant.BW),
    Inkwell(R.drawable.filter_thumb_bw7, R.string.filter_name_inkwell, FilterConstant.BW),
    Yellowing(R.drawable.filter_thumb_bw8, R.string.filter_name_yellowing, FilterConstant.BW),
    SelectiveColor(R.drawable.filter_thumb_bw9, R.string.filter_name_silence, FilterConstant.BW),


    Thermal(R.drawable.filter_thumb_art1, R.string.filter_name_thermal, FilterConstant.ART),
    Ink(R.drawable.filter_thumb_art2, R.string.filter_name_ink, FilterConstant.ART),
    Relief(R.drawable.filter_thumb_art3, R.string.filter_name_relief, FilterConstant.ART),
    Crayon(R.drawable.filter_thumb_art4, R.string.filter_name_crayon, FilterConstant.ART),
    Frosted(R.drawable.filter_thumb_art5, R.string.filter_name_frosted, FilterConstant.ART),
    Pixelize(R.drawable.filter_thumb_art6, R.string.filter_name_pixelize, FilterConstant.ART),
    TileMosaic(R.drawable.filter_thumb_art7, R.string.filter_name_tile_mosaic, FilterConstant.ART),
    NoiseWarp(R.drawable.filter_thumb_art8, R.string.filter_name_wrap, FilterConstant.ART),
    Triangles(R.drawable.filter_thumb_art9, R.string.filter_name_triangles_mosaic, FilterConstant.ART),
    Logefied(R.drawable.filter_thumb_art10, R.string.filter_name_legofied, FilterConstant.ART),
    Wave(R.drawable.filter_thumb_art11, R.string.filter_name_wave, FilterConstant.ART),
    EdgeDetection(R.drawable.filter_thumb_art12, R.string.filter_name_edge_detection, FilterConstant.ART),
    Distance(R.drawable.filter_thumb_art13, R.string.filter_name_distance, FilterConstant.ART),
    BlueOrange(R.drawable.filter_thumb_art14, R.string.filter_name_blue_orange, FilterConstant.ART),
    Invert(R.drawable.filter_thumb_art15, R.string.filter_name_invert, FilterConstant.ART),
    Interference(R.drawable.filter_thumb_art16, R.string.filter_name_valencia, FilterConstant.ART),
    Sketch(R.drawable.filter_thumb_art17, R.string.filter_name_sketch, FilterConstant.ART),
    Toon(R.drawable.filter_thumb_art18, R.string.filter_name_comic, FilterConstant.ART),
    Cracke(R.drawable.filter_thumb_art19, R.string.slideshow_filter_cracked, FilterConstant.ART),


    //raw资源要放到raw
    Lookup(R.drawable.filter_thumb_movie1, R.string.filter_name_dawn, FilterConstant.MOVIE),
    Lookup1(R.drawable.filter_thumb_movie2, R.string.filter_name_old, FilterConstant.MOVIE),
    Lookup2(R.drawable.filter_thumb_movie3, R.string.filter_name_midnight, FilterConstant.MOVIE),
    Lookup3(R.drawable.filter_thumb_movie4, R.string.filter_name_retro, FilterConstant.MOVIE),
    Lookup4(R.drawable.filter_thumb_movie5, R.string.filter_name_biography, FilterConstant.MOVIE),
    Lookup5(R.drawable.filter_thumb_movie6, R.string.filter_name_gloomy, FilterConstant.MOVIE),
    Lookup6(R.drawable.filter_thumb_movie7, R.string.filter_name_lyrical, FilterConstant.MOVIE),
    Lookup7(R.drawable.filter_thumb_movie8, R.string.filter_name_dramatic, FilterConstant.MOVIE),
    Lookup8(R.drawable.filter_thumb_movie9, R.string.filter_name_journey, FilterConstant.MOVIE),
    Lookup9(R.drawable.filter_thumb_movie10, R.string.filter_name_spotlight, FilterConstant.MOVIE),
    Lookup10(R.drawable.filter_thumb_movie11, R.string.filter_name_scenes, FilterConstant.MOVIE),
    Lookup11(R.drawable.filter_thumb_movie12, R.string.filter_name_mood, FilterConstant.MOVIE),
    Lookup12(R.drawable.filter_thumb_movie13, R.string.filter_name_from, FilterConstant.MOVIE),
    Lookup13(R.drawable.filter_thumb_movie14, R.string.filter_name_plot, FilterConstant.MOVIE),


    Lookup14(R.drawable.filter_thumb_food1, R.string.filter_name_delicious, FilterConstant.FOOD),
    Lookup15(R.drawable.filter_thumb_food2, R.string.filter_name_peach, FilterConstant.FOOD),
    Lookup16(R.drawable.filter_thumb_food3, R.string.filter_name_honey, FilterConstant.FOOD),
    Lookup17(R.drawable.filter_thumb_food4, R.string.filter_name_lemon, FilterConstant.FOOD),
    Lookup18(R.drawable.filter_thumb_food5, R.string.filter_name_sweet, FilterConstant.FOOD),
    Lookup19(R.drawable.filter_thumb_food6, R.string.filter_name_fresh, FilterConstant.FOOD),
    Lookup20(R.drawable.filter_thumb_food7, R.string.filter_name_mint, FilterConstant.FOOD),
    Lookup21(R.drawable.filter_thumb_food8, R.string.filter_name_rum, FilterConstant.FOOD),
    Lookup22(R.drawable.filter_thumb_food9, R.string.filter_name_lime, FilterConstant.FOOD),
    Lookup23(R.drawable.filter_thumb_food10, R.string.filter_name_milk, FilterConstant.FOOD),
    Lookup24(R.drawable.filter_thumb_food11, R.string.filter_name_cream, FilterConstant.FOOD),
    Lookup25(R.drawable.filter_thumb_food12, R.string.filter_name_light, FilterConstant.FOOD),
    Lookup26(R.drawable.filter_thumb_food13, R.string.filter_name_coffee, FilterConstant.FOOD),
    Lookup27(R.drawable.filter_thumb_food14, R.string.filter_name_blueberry, FilterConstant.FOOD),
    Lookup28(R.drawable.filter_thumb_food15, R.string.filter_name_whisky, FilterConstant.FOOD),
    Lookup29(R.drawable.filter_thumb_food16, R.string.filter_name_caramel, FilterConstant.FOOD),


    Sakura(R.drawable.filter_thumb_natural1, R.string.filter_name_sakura, FilterConstant.NATURAL),
    Pixar(R.drawable.filter_thumb_natural2, R.string.filter_name_pixar, FilterConstant.NATURAL),
    Healthy(R.drawable.filter_thumb_natural3, R.string.filter_name_healthy, FilterConstant.NATURAL),
    Amaro(R.drawable.filter_thumb_natural4, R.string.filter_name_amaro, FilterConstant.NATURAL),
    Amaro1(R.drawable.filter_thumb_natural16, R.string.filter_name_coral, FilterConstant.NATURAL),
    Amaro2(R.drawable.filter_thumb_natural17, R.string.filter_name_grass, FilterConstant.NATURAL),
    Calm(R.drawable.filter_thumb_natural5, R.string.filter_name_calm, FilterConstant.NATURAL),
    Brannan(R.drawable.filter_thumb_natural6, R.string.filter_name_brannan, FilterConstant.NATURAL),
    Rise(R.drawable.filter_thumb_natural7, R.string.filter_name_rise, FilterConstant.NATURAL),
    Walden(R.drawable.filter_thumb_natural8, R.string.filter_name_walden, FilterConstant.NATURAL),
    Walden1(R.drawable.filter_thumb_natural18, R.string.filter_name_soft, FilterConstant.NATURAL),
    Hefe(R.drawable.filter_thumb_natural9, R.string.filter_name_hefe, FilterConstant.NATURAL),
    EarlyBird(R.drawable.filter_thumb_natural10, R.string.filter_name_earlybird, FilterConstant.NATURAL),
    Kevin(R.drawable.filter_thumb_natural11, R.string.filter_name_kevin, FilterConstant.NATURAL),
    Toaster(R.drawable.filter_thumb_natural12, R.string.filter_name_toaster2, FilterConstant.NATURAL),
    Tender(R.drawable.filter_thumb_natural13, R.string.filter_name_tender, FilterConstant.NATURAL),
    Emerald(R.drawable.filter_thumb_natural14, R.string.filter_name_emerald, FilterConstant.NATURAL),
    Evergreen(R.drawable.filter_thumb_natural15, R.string.filter_name_evergreen, FilterConstant.NATURAL),

    //popular
    PopularBEAUTY(R.drawable.beauty, R.string.slideshow_filter_beauty, FilterConstant.POPULAR),
    PopularANTIQUE(R.drawable.antique, R.string.slideshow_filter_antique, FilterConstant.POPULAR),
    PopularCOOL(R.drawable.cold, R.string.slideshow_filter_cold, FilterConstant.POPULAR),
    PopularBRANNAN(R.drawable.brannan, R.string.slideshow_filter_brannan, FilterConstant.POPULAR),
    PopularSKETCH(R.drawable.sketch, R.string.slideshow_filter_sketch, FilterConstant.POPULAR),
    PopularPORTRAIT(R.drawable.portrait, R.string.slideshow_filter_portrait, FilterConstant.POPULAR),
    PopularHEFE(R.drawable.hefe, R.string.slideshow_filter_hefe, FilterConstant.POPULAR),
    PopularRETRO(R.drawable.retro, R.string.slideshow_filter_retro, FilterConstant.POPULAR),
    PopularHUDSON(R.drawable.hudson, R.string.slideshow_filter_hudson, FilterConstant.POPULAR),
    PopularPopularINKWELL(R.drawable.inkwell, R.string.slideshow_filter_inkwell, FilterConstant.POPULAR),
    PopularN1977(R.drawable.n1977, R.string.slideshow_filter_n1977, FilterConstant.POPULAR),
    PopularNASHVILLE(R.drawable.nashville, R.string.slideshow_filter_nashville, FilterConstant.POPULAR),
    PopularWARM(R.drawable.warm, R.string.slideshow_filter_warm, FilterConstant.POPULAR),
    PopularSWEETY(R.drawable.sweety, R.string.slideshow_filter_sweety, FilterConstant.POPULAR),
    PopularROSY(R.drawable.rosy, R.string.slideshow_filter_rosy, FilterConstant.POPULAR),
    PopularLOLITA(R.drawable.lolita, R.string.slideshow_filter_lolita, FilterConstant.POPULAR),
    PopularPINK(R.drawable.pink, R.string.slideshow_filter_pink, FilterConstant.POPULAR),
    PopularSUNRISE(R.drawable.rise, R.string.slideshow_filter_rise, FilterConstant.POPULAR),
    PopularSUNSET(R.drawable.sunset, R.string.slideshow_filter_sunset, FilterConstant.POPULAR),
    PopularVINTAGE(R.drawable.vintage, R.string.slideshow_filter_vintage, FilterConstant.POPULAR),
    PopularROCOCO(R.drawable.rococo, R.string.slideshow_filter_rococo, FilterConstant.POPULAR),
    PopularROMANCE(R.drawable.romance, R.string.slideshow_filter_romance, FilterConstant.POPULAR),
    PopularLOMO(R.drawable.lomo, R.string.slideshow_filter_lomo, FilterConstant.POPULAR),
    PopularLATTE(R.drawable.latte, R.string.slideshow_filter_latte, FilterConstant.POPULAR),
    PopularCALM(R.drawable.calm, R.string.slideshow_filter_calm, FilterConstant.POPULAR),
    PopularINVERTCOLOR(R.drawable.invertcolor, R.string.slideshow_filter_invertColor, FilterConstant.POPULAR),
    PopularPIXELIZE(R.drawable.pixelize, R.string.slideshow_filter_pixelize, FilterConstant.POPULAR),
    PopularCRACKED(R.drawable.cracked, R.string.slideshow_filter_cracked, FilterConstant.POPULAR);


    int resourceId;
    int nameId;
    int type;


    MagicFilterType(int resourceId, int nameId, int type) {
        this.resourceId = resourceId;
        this.nameId = nameId;
        this.type = type;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static MagicFilterType randomMagic() {
        int random = new Random().nextInt(MagicFilterType.values().length - 1);
        return MagicFilterType.values()[random];
    }

}
