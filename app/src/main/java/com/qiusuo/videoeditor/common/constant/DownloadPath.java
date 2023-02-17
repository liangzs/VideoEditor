package com.qiusuo.videoeditor.common.constant;


import com.google.android.exoplayer2.offline.DownloadHelper;

/**
 * Created by luotangkang on 2019/9/4.
 */
public class DownloadPath {
    //**********************************生产服务器***********************************
    /**
     * @date 2022/05/26
     * 改动的最新地址
     */
    public static String BASE_PATH = "https://gslideshowres.ijoysoftconnect.com";
    public static String PHOTO_LIB = "https://photoeditlib.ijoysoftconnect.com/";
    public static String EDIT_LIB = "https://editlibres.ijoysoftconnect.com/";
    //之前用到的下载地址
    public static String BASE_PATH_REDIRECT = "https://gslideshowres.oss-us-west-1.aliyuncs.com";
    public static String PHOTO_LIB_REDIRECT = "https://photoeditlib.oss-us-west-1.aliyuncs.com/";
    public static String EDIT_LIB_REDIRECT = "https://editlibres.oss-us-west-1.aliyuncs.com/";

    /*隐私政策*/
    //**********************************测试服务器***********************************
//    public static final String BASE_PATH = "http://192.168.1.172:9090/files/videomaker";

    //提前更新素材，防止新增旧应用的拉取数据影响，不覆盖删除线上生产配置文件
    public static final String THEME_VERSION = "_v212";
//    public static final String THEME_VERSION_ETRA = "_v210";
    //测试
    //public static final String currentVersion = "";

    //theme
    public static final String THEME_VERSION_REQUEST = BASE_PATH + "/config/vm/theme_version.xml";
    public static final String THEME_DATA_REQUEST = BASE_PATH + "/config/vm/theme" + THEME_VERSION + ".xml";
    public static final String THEME_RESOURCE_REQUEST = BASE_PATH + "/config/vm/resource" + THEME_VERSION + ".xml";
    //主题排版
    public static final String THEME_SORT_REQUEST = BASE_PATH + "/config/vm/theme_sort" + THEME_VERSION + ".json";
    //theme 版本信息下载保存路径
    public static final String THEME_DATA_SAVE = "";
    public static final String THEME_SORT_SAVE =  "";
    public static final String THEME_VERSION_SAVE =  "";
    public static final String THEME_RESOURCE_SAVE =  "";

    public static final String THEME_RESOURCE_IMAGE = "";
    //music
    public static final String MUSIC_VERSION_REQUEST = BASE_PATH + "/config/music/music_version.xml";
    public static final String MUSIC_DATA_REQUEST = BASE_PATH + "/config/music/music_common.xml";
    public static final String MUSIC_VERSION_SAVE = "";
    public static final String MUSIC_DATA_SAVE = "";

    //InnerBorder
    /**
     * 边框资源清单文件结构版本
     */
    public static final String INNER_BORDER_STRUCTURE_VERSION = "_pv101";
    //边框资源数据版本
    public static final String INNERBORDER_VERSION_REQUEST = BASE_PATH + "/innerborder_version.json";
//    public static final String INNERBORDER_VERSION_SAVE = DownloadHelper.BASE_DOWNLOAD_PATH + "/innerborder_version.json";
    public static final String INNERBORDER_DATA_REQUEST = BASE_PATH + "/innerborder" + INNER_BORDER_STRUCTURE_VERSION + ".json";
//    public static final String INNERBORDER_DATA_SAVE = DownloadHelper.BASE_DOWNLOAD_PATH + "/innerborder.json";
    public static final String INNERBORDER_REQUEST = BASE_PATH + "/innerborder";

    /**
     * 图片资源请求地址
     */
    public static final String IMAGE_MATERIAL_REUEST = BASE_PATH + "/image_material";

    /**
     * 图片资源保存地址
     */
//    public static final String IMAGE_MATERIAL_SAVE = DownloadHelper.BASE_DOWNLOAD_PATH + "/image_material";

    //imageBg
    //更多资源版本信息
    public static final String IMAGE_VERSION_PATH = EDIT_LIB + "version_4.json";
    //存储文件信息的JSON
    public static final String IMAGE_DATA_PATH = EDIT_LIB + "list_4.json";
    //背景下载保存目录
//    public static final String BACKGROUND_SAVE_PATH = DownloadHelper.BASE_DOWNLOAD_PATH + "/Background/";
//    //每组资源预览图保存目录
//    public static final String GROUP_SAVE_PATH = DownloadHelper.BASE_DOWNLOAD_PATH + "/Group/";

    //Filter Path
    public static String[] FILTER_URL = {
            PHOTO_LIB.concat("filters/lut_adore"),
            PHOTO_LIB.concat("filters/lut_washout"),
            PHOTO_LIB.concat("filters/lut_washout_color"),
            PHOTO_LIB.concat("filters/lut_bleach"),
            PHOTO_LIB.concat("filters/lut_blue_crush"),
            PHOTO_LIB.concat("filters/lut_instant"),
            PHOTO_LIB.concat("filters/lut_process"),
            PHOTO_LIB.concat("filters/lut_punch")};


    /**
     * 对url进行过滤，把新的url头部替换成
     *
     * @param url
     * @return
     */
    public static String replacePath2Save(String url) {
        //主题,音频，贴纸下载
        if (url.contains(BASE_PATH)) {
            return url.replace(BASE_PATH, BASE_PATH_REDIRECT);
        }
        //滤镜下载
        if (url.contains(PHOTO_LIB)) {
            return url.replace(PHOTO_LIB, PHOTO_LIB_REDIRECT);
        }
        //背景下载
        if (url.contains(EDIT_LIB)) {
            return url.replace(EDIT_LIB, EDIT_LIB_REDIRECT);
        }
        return url;
    }

}
