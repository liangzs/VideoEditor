package com.qiusuo.videoeditor.common.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeConstant;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeEnum;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeHelper;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.IPretreatment;
import com.qiusuo.videoeditor.common.bean.ThemeEntity;
import com.qiusuo.videoeditor.common.bean.ThemeDownEntity;
import com.qiusuo.videoeditor.common.bean.ThemeGroupEntity;
import com.qiusuo.videoeditor.common.bean.ThemeResGroupEntity;
import com.qiusuo.videoeditor.common.bean.ThemeResourceEntity;
import com.qiusuo.videoeditor.common.constant.DownloadPath;
import com.qiusuo.videoeditor.common.constant.PathConstant;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThemeFactory {

    /**
     * 预处理对象
     *
     * @param themeEnum
     * @return
     */
    public static IPretreatment createPreTreatment(ThemeEnum themeEnum) {
        return ThemeHelper.createPreTreatment(themeEnum);
    }

    /**
     * 获取分类主题
     *
     * @return
     */
    public static List<ThemeEntity> getThemeData(@ThemeConstant.ThemeType int themeType) {
        String localMusicPath = "";
        List<ThemeEntity> list = new ArrayList<>();
        ThemeGroupEntity themeGroupEntity = MediaDataRepository.getInstance().getThemeGroupEntity();
        if (ObjectUtils.isEmpty(themeGroupEntity)) {
            //如果主题基础数据没有从服务端获取，则创建四个主题本地数据进行显示
            return createLocalTheme(themeType);
        }
        List<ThemeDownEntity> downEntities = themeGroupEntity.getThemeList(themeType);
        if (ObjectUtils.isEmpty(downEntities)) {
            return createLocalTheme(themeType);
        }
        ThemeResGroupEntity themeResGroupEntity = MediaDataRepository.getInstance().getThemeResGroupEntity();
        if (ObjectUtils.isEmpty(themeResGroupEntity)) {
            return createLocalTheme(themeType);
        }
        ThemeEntity slideshowEntity;
        ThemeEnum themeEnum;
        ThemeResourceEntity themeResourceEntity;
        for (ThemeDownEntity downEntity : downEntities) {
            if (ObjectUtils.isEmpty(downEntity.getName())) {
                LogUtils.v("", downEntity.toString());
                continue;
            }
            themeEnum = ThemeEnum.findThemNum(downEntity.getName());
            if (themeEnum == null) {
                continue;
            }
            themeResourceEntity = themeResGroupEntity.getIndexResourEntity(downEntity.getIndex(), downEntity.getName());
            slideshowEntity = new ThemeEntity();
            slideshowEntity.setName(downEntity.getName());
            slideshowEntity.setThemeEnum(themeEnum);
            slideshowEntity.setZipPath(DownloadPath.BASE_PATH + downEntity.getPath());
//            slideshowEntity.setPath(DownloadHelper.BASE_DOWNLOAD_PATH);
            slideshowEntity.setMusicDownPath(DownloadPath.BASE_PATH + downEntity.getMusicPath());
//            localMusicPath = PathUtil.MUSIC + downEntity.getMusicPath();
            localMusicPath = localMusicPath.substring(0, localMusicPath.lastIndexOf("."));
            slideshowEntity.setMusicLocalPath(localMusicPath);
            slideshowEntity.setSize(downEntity.getSize());
            slideshowEntity.setMusicName(downEntity.getMusicName());
            slideshowEntity.setThemeConstanType(slideshowEntity.getThemeEnum().getType());

            slideshowEntity.setHot(themeType == ThemeConstant.HOT);

            if (!ObjectUtils.isEmpty(themeResourceEntity)) {
                slideshowEntity.setResName(themeResourceEntity.getTitle().getTitle());
//                slideshowEntity.setResRequestPath(DownloadPath.BASE_PATH + themeResourceEntity.getPath());
//                slideshowEntity.setResLocalPath(DownloadHelper.BASE_DOWNLOAD_PATH + themeResourceEntity.getPath());
            }
            list.add(slideshowEntity);
        }
//        if (themeType == ThemeConstant.LOVE) {
//            list.addAll(createLoveTheme());
//        }
        return list;
    }


    public static List<ThemeEntity> getBottomSelectTheme() {
        List<ThemeEntity> themes = new ArrayList();
        themes.addAll(getThemeData(ThemeConstant.HOT));
        themes.addAll(getAllTheme());
        return themes;
    }


    /**
     * 获取分类主题
     *
     * @return
     */
    public static List<ThemeEntity> getAllTheme() {
        String localMusicPath = "";
        List<ThemeEntity> list = new ArrayList<>();
        ThemeGroupEntity themeGroupEntity = MediaDataRepository.getInstance().getThemeGroupEntity();
        if (ObjectUtils.isEmpty(themeGroupEntity)) {
            //如果主题基础数据没有从服务端获取，则创建四个主题本地数据进行显示
            return createCommonTheme();
        }
        List<ThemeDownEntity> downEntities = themeGroupEntity.getAllList();
        if (ObjectUtils.isEmpty(downEntities)) {
            return createCommonTheme();
        }
        ThemeResGroupEntity themeResGroupEntity = MediaDataRepository.getInstance().getThemeResGroupEntity();
        if (ObjectUtils.isEmpty(themeResGroupEntity) || ObjectUtils.isEmpty(themeResGroupEntity.getAllResource())) {
            return createCommonTheme();
        }
        ThemeEntity slideshowEntity;
        ThemeEnum themeEnum;
        ThemeResourceEntity themeResourceEntity;
        for (ThemeDownEntity downEntity : downEntities) {
            if (ObjectUtils.isEmpty(downEntity.getName())) {
                LogUtils.v("", downEntity.toString());
                continue;
            }
            themeEnum = ThemeEnum.findThemNum(downEntity.getName());
            if (themeEnum == null) {
                continue;
            }
            themeResourceEntity = themeResGroupEntity.getIndexResourEntity(downEntity.getIndex(), downEntity.getName());
            slideshowEntity = new ThemeEntity();
            slideshowEntity.setName(downEntity.getName());
            slideshowEntity.setThemeEnum(themeEnum);
            slideshowEntity.setZipPath(DownloadPath.BASE_PATH + downEntity.getPath());
//            slideshowEntity.setPath(DownloadHelper.BASE_DOWNLOAD_PATH);
            slideshowEntity.setMusicDownPath(DownloadPath.BASE_PATH + downEntity.getMusicPath());
            localMusicPath = PathConstant.MUSIC + downEntity.getMusicPath();
            localMusicPath = localMusicPath.substring(0, localMusicPath.lastIndexOf("."));
            slideshowEntity.setMusicLocalPath(localMusicPath);
            slideshowEntity.setSize(downEntity.getSize());
            slideshowEntity.setMusicName(downEntity.getMusicName());
            slideshowEntity.setThemeConstanType(slideshowEntity.getThemeEnum().getType());

            if (!ObjectUtils.isEmpty(themeResourceEntity)) {
                slideshowEntity.setResName(themeResourceEntity.getTitle().getTitle());
//                slideshowEntity.setResRequestPath(DownloadPath.BASE_PATH + themeResourceEntity.getPath());
//                slideshowEntity.setResLocalPath(DownloadHelper.BASE_DOWNLOAD_PATH + themeResourceEntity.getPath());
            }
            list.add(slideshowEntity);
        }
//        if (themeType == ThemeConstant.LOVE) {
//            list.addAll(createLoveTheme());
//        }
        return list;
    }

    /**
     * 获取已下载的主题
     *
     * @return
     */
    public static List<ThemeEntity> getDownloadTheme() {
        String localMusicPath;
        List<ThemeEntity> list = new ArrayList<>();
        ThemeGroupEntity themeGroupEntity = MediaDataRepository.getInstance().getThemeGroupEntity();
        if (ObjectUtils.isEmpty(themeGroupEntity)) {
            return list;
        }
        List<ThemeDownEntity> downEntities = themeGroupEntity.getDownLocalList();
        if (ObjectUtils.isEmpty(downEntities)) {
            return list;
        }
        ThemeResGroupEntity themeResGroupEntity = MediaDataRepository.getInstance().getThemeResGroupEntity();
        if (ObjectUtils.isEmpty(themeResGroupEntity)) {
            return list;
        }
        ThemeEntity slideshowEntity;
        ThemeEnum themeEnum;
        ThemeResourceEntity themeResourceEntity;
        for (ThemeDownEntity downEntity : downEntities) {
            themeEnum = ThemeEnum.findThemNum(downEntity.getName());
            themeResourceEntity = themeResGroupEntity.getIndexResourEntity(downEntity.getIndex(), downEntity.getName());
            if (ObjectUtils.isEmpty(themeResGroupEntity)) {
                continue;
            }
            slideshowEntity = new ThemeEntity();
            slideshowEntity.setName(downEntity.getName());
            slideshowEntity.setResName(themeResourceEntity.getTitle().getTitle());
//            slideshowEntity.setResRequestPath(DownloadPath.BASE_PATH + themeResourceEntity.getPath());
//            slideshowEntity.setResLocalPath(DownloadHelper.BASE_DOWNLOAD_PATH + themeResourceEntity.getPath());
            slideshowEntity.setThemeEnum(themeEnum);
            slideshowEntity.setZipPath(DownloadPath.BASE_PATH + downEntity.getPath());
//            slideshowEntity.setPath(DownloadHelper.BASE_DOWNLOAD_PATH);
            slideshowEntity.setMusicDownPath(DownloadPath.BASE_PATH + downEntity.getMusicPath());
            localMusicPath = PathConstant.MUSIC + downEntity.getMusicPath();
            localMusicPath = localMusicPath.substring(0, localMusicPath.lastIndexOf("."));
            slideshowEntity.setMusicLocalPath(localMusicPath);
            slideshowEntity.setMusicName(downEntity.getMusicName());
            slideshowEntity.setSize(downEntity.getSize());
            list.add(slideshowEntity);
        }
        return list;
    }

    public static boolean checkLockTheme(ThemeEnum themeEnum) {
        return false;
//        return themeEnum == ThemeEnum.COMMON_THIRDTEEN || themeEnum == ThemeEnum.COMMON_TWENTYONE ||
//                themeEnum == ThemeEnum.COMMON_TWENTYSEVEN || themeEnum == ThemeEnum.COMMON_TWENTYEIGHT;
    }


    public static List<ThemeEntity> createBeatTheme() {
        List<ThemeEntity> locals = new ArrayList<>();
        for (ThemeEnum themeEnum : ThemeEnum.values()) {
            if (themeEnum.getType() == ThemeConstant.BEAT) {
                ThemeEntity entity = new ThemeEntity();
                entity.setResName(themeEnum.getName());
                entity.setThemeEnum(themeEnum);
                entity.setZipPath(DownloadPath.BASE_PATH + "/theme/birthday/birthday1.zip");
                entity.setMusicLocalPath(PathConstant.MUSIC + "/music/birthday/BirthdaySong");
                locals.add(entity);
            }
        }
        return locals;
    }

    public static List<ThemeEntity> createHoliTheme() {
        List<ThemeEntity> locals = new ArrayList<>();
        for (ThemeEnum themeEnum : ThemeEnum.values()) {
            if (themeEnum.getType() == ThemeConstant.HOLI) {
                ThemeEntity entity = new ThemeEntity();
                entity.setResName(themeEnum.getName());
                entity.setThemeEnum(themeEnum);
                entity.setZipPath(DownloadPath.BASE_PATH + "/theme/birthday/birthday1.zip");
                entity.setMusicLocalPath(PathConstant.MUSIC + "/music/birthday/BirthdaySong");
                locals.add(entity);
            }
        }
        return locals;
    }

    public static List<ThemeEntity> createDivaliTheme() {
        List<ThemeEntity> locals = new ArrayList<>();
        for (ThemeEnum themeEnum : ThemeEnum.values()) {
            if (themeEnum.getType() == ThemeConstant.DIVALI) {
                ThemeEntity entity = new ThemeEntity();
                entity.setResName((themeEnum.getName()));
                entity.setThemeEnum(themeEnum);
                entity.setZipPath(DownloadPath.BASE_PATH + "/theme/birthday/birthday1.zip");
                entity.setMusicLocalPath(PathConstant.MUSIC + "/music/birthday/BirthdaySong");
                locals.add(entity);
            }
        }
        return locals;
    }


    /**
     * 如果主题的基础数据是空的时候，创建四个本地数据
     *
     * @return
     */
    public static List<ThemeEntity> createLocalTheme(@ThemeConstant.ThemeType int themeType) {
        if (themeType == ThemeConstant.COMMON || themeType == ThemeConstant.HOT) {
            return createCommonTheme();
        }
        return null;
    }

    /**
     * <item index="20" name="common21" path="/theme/common/common21.zip" size="760916" musicPath="/music/soft/TheBluestStar.mp3"/>
     * <item index="12" name="common13" path="/theme/common/commonEmpty.zip" size="796289" musicPath="/music/dynamic/campfire.mp3"/>
     * <item index="26" name="common27" path="/theme/common/common27.zip" size="461189" musicPath="/music/dynamic/Add Loudness.mp3"/>
     * <item index="27" name="common28" path="/theme/common/common28.zip" size="375420" musicPath="/music/dynamic/Indie Pop.mp3"/>
     *
     * @return
     */
    public static List<ThemeEntity> createCommonTheme() {
        List<ThemeEntity> locals = new ArrayList<>();
//        for (ThemeEnum themeEnum : ThemeEnum.values()) {
//            if (themeEnum.getType() == ThemeConstant.COMMON) {
//                SlideshowEntity entity = new SlideshowEntity();
//
//                entity.setThemeConstanType(ThemeConstant.HOT);
//                entity.setThemeEnum(themeEnum);
//                entity.setMusicDownPath("");
//                entity.setZipPath(DownloadPath.BASE_PATH + "/theme/common/" + themeEnum.getName() + ".zip");
//                switch (themeEnum) {
//                    case COMMON_THIRDTEEN:
//                        entity.setMusicLocalPath(PathUtil.MUSIC + "/music/dynamic/campfire");
//                        entity.setResName(MediaSdk.getInstance().getResouce().getString(R.string.common13));
//                        break;
//                    case COMMON_TWENTYONE:
//                        entity.setMusicLocalPath(PathUtil.MUSIC + "/music/soft/TheBluestStar");
//                        entity.setResName(MediaSdk.getInstance().getResouce().getString(R.string.common21));
//                        break;
//                    case COMMON_TWENTYSEVEN:
//                        entity.setMusicLocalPath(PathUtil.MUSIC + "/music/dynamic/Add Loudness");
//                        entity.setResName(MediaSdk.getInstance().getResouce().getString(R.string.common27));
//                        break;
//                    case COMMON_TWENTYEIGHT:
//                        entity.setMusicLocalPath(PathUtil.MUSIC + "/music/dynamic/Indie Pop");
//                        entity.setResName(MediaSdk.getInstance().getResouce().getString(R.string.common28));
//                        break;
//                    default:
//                        continue;
//                }
//                locals.add(entity);
//            }
//        }
        return locals;
    }

    /**
     * 获取本地主题的预览图
     *
     * @param themeEnum 主题枚举
     * @return 地址
     */
    public static String getLocalThemeCoverPath(ThemeEnum themeEnum) {
        if (checkLockTheme(themeEnum)) {
            return "file:///android_asset/cover/cover_" + themeEnum.getName() + ".webp";
        } else {
            throw new IllegalArgumentException("This is not local Theme!");
        }
    }


    private static List<String> themeSort = null;

    /**
     * 获取当前主题展示
     *
     * @return
     */
    public static List<String> getThemeSort() {
        if (themeSort == null || themeSort == ThemeConstant.defaultThemeSort) {
            synchronized (ThemeFactory.class) {
                if (themeSort == null || themeSort == ThemeConstant.defaultThemeSort) {
                    try {
                        Reader reader = new FileReader(DownloadPath.THEME_SORT_SAVE);
                        themeSort = new Gson().fromJson(reader, new TypeToken<List<String>>() {
                        }.getType());
                        themeSort = Collections.unmodifiableList(themeSort);
                    } catch (Exception e) {
                        themeSort = ThemeConstant.defaultThemeSort;
                        e.printStackTrace();
                    }
                }
            }
        }
        return themeSort;
    }

    /**
     * 主题锁
     */
//    public static ReentrantLock saveThemeLocker = new ReentrantLock(true);

    /**
     * 保存下载的主题
     *
     * @param entity
     */
    public static void saveDownloadTheme(ThemeEntity entity) {
//        saveThemeLocker.lock();
//        try {
//            List<String> themeNames = SharedPreferencesUtil.getDataList(ContactUtils.SP_KEY_DOWNLOAD_THEME, String.class);
//            if (themeNames.isEmpty()) {
//                List<SlideshowEntity> entities = ThemeFactory.createLocalTheme(ThemeConstant.COMMON);
//                for (SlideshowEntity e : entities) {
//                    themeNames.add(e.getName());
//                }
//            }
//            themeNames.add(0, entity.getName());
//            SharedPreferencesUtil.setDataList(ContactUtils.SP_KEY_DOWNLOAD_THEME, themeNames);
//        } finally {
//            saveThemeLocker.unlock();
//        }
    }
}
