package com.qiusuo.videoeditor.common.bean;


import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeConstant;
import com.qiusuo.videoeditor.common.data.ThemeRepository;

import java.util.ArrayList;
import java.util.List;

public class ThemeGroupEntity {
    //分类增加修改点
    private List<ThemeDownEntity> birthday = new ArrayList<>();
    private List<ThemeDownEntity> valentine = new ArrayList<>();
    private List<ThemeDownEntity> wedding = new ArrayList<>();
    private List<ThemeDownEntity> baby = new ArrayList<>();
    private List<ThemeDownEntity> holiday = new ArrayList<>();
    private List<ThemeDownEntity> greeting = new ArrayList<>();
    private List<ThemeDownEntity> celebration = new ArrayList<>();
    private List<ThemeDownEntity> love = new ArrayList<>();
    private List<ThemeDownEntity> travel = new ArrayList<>();
    private List<ThemeDownEntity> common = new ArrayList<>();
    private List<ThemeDownEntity> beat = new ArrayList<>();
    //洒红节
    private List<ThemeDownEntity> holi;
    //热门系列
    private List<ThemeDownEntity> hots = new ArrayList<>();

    private List<ThemeDownEntity> allCacheList;

    /**
     * 分类增加修改点
     */
    private int[] startIndex = new int[14];

    //分类增加修改点


    /**
     * 兄妹节
     */
    private List<ThemeDownEntity> rakshaBandHan;

    /**
     * 欧南节
     */
    private List<ThemeDownEntity> onam;

    /**
     * 态度
     */
    private List<ThemeDownEntity> attitude;

    /**
     * friend
     */
    private List<ThemeDownEntity> friend;

    /**
     * christmas
     */
    private List<ThemeDownEntity> christmas = new ArrayList<>();
    ;


    /**
     * new year
     */
    private List<ThemeDownEntity> newYear = new ArrayList<>();
    ;
    /**
     * food
     */
    private List<ThemeDownEntity> food = new ArrayList<>();

    /**
     * daily
     */
    private List<ThemeDownEntity> daily = new ArrayList<>();

    /**
     * sport
     */
    private List<ThemeDownEntity> sport = new ArrayList<>();
    ;

    //分类增加修改点


    public List<ThemeDownEntity> getChristmas() {
        return christmas;
    }

    public void setChristmas(List<ThemeDownEntity> christmas) {
        this.christmas = christmas;
    }

    public List<ThemeDownEntity> getNewYear() {
        return newYear;
    }

    public void setNewYear(List<ThemeDownEntity> newYear) {
        this.newYear = newYear;
    }

    public List<ThemeDownEntity> getBirthday() {
        return birthday;
    }

    public void setBirthday(List<ThemeDownEntity> birthday) {
        this.birthday = birthday;
    }

    public List<ThemeDownEntity> getValentine() {
        return valentine;
    }

    public void setValentine(List<ThemeDownEntity> valentine) {
        this.valentine = valentine;
    }

    public List<ThemeDownEntity> getWedding() {
        return wedding;
    }

    public void setWedding(List<ThemeDownEntity> wedding) {
        this.wedding = wedding;
    }

    public List<ThemeDownEntity> getBaby() {
        return baby;
    }

    public void setBaby(List<ThemeDownEntity> baby) {
        this.baby = baby;
    }

    public List<ThemeDownEntity> getHoliday() {
        return holiday;
    }

    public void setHoliday(List<ThemeDownEntity> holiday) {
        this.holiday = holiday;
    }

    public List<ThemeDownEntity> getGreeting() {
        return greeting;
    }

    public void setGreeting(List<ThemeDownEntity> greeting) {
        this.greeting = greeting;
    }

    public List<ThemeDownEntity> getCelebration() {
        return celebration;
    }

    public void setCelebration(List<ThemeDownEntity> celebration) {
        this.celebration = celebration;
    }

    public List<ThemeDownEntity> getLove() {
        return love;
    }

    public void setLove(List<ThemeDownEntity> love) {
        this.love = love;
    }

    public List<ThemeDownEntity> getTravel() {
        return travel;
    }

    public void setTravel(List<ThemeDownEntity> travel) {
        this.travel = travel;
    }

    public List<ThemeDownEntity> getHots() {
        return hots;
    }

    public void setHots(List<ThemeDownEntity> hots) {
        this.hots = hots;
    }

    public List<ThemeDownEntity> getBeat() {
        return beat;
    }

    public void setBeat(List<ThemeDownEntity> beat) {
        this.beat = beat;
    }

    public List<ThemeDownEntity> getHoli() {
        return holi;
    }

    public void setHoli(List<ThemeDownEntity> holi) {
        this.holi = holi;
    }

    public List<ThemeDownEntity> getAttitude() {
        return attitude;
    }

    public List<ThemeDownEntity> getFriend() {
        return friend;
    }

    public void setFriend(List<ThemeDownEntity> friend) {
        this.friend = friend;
    }

    public List<ThemeDownEntity> getSport() {
        return sport;
    }

    public void setSport(List<ThemeDownEntity> sport) {
        this.sport = sport;
    }

    /**
     * 分类增加修改点
     *
     * @param themeType
     * @return
     */
    public List<ThemeDownEntity> getThemeList(@ThemeConstant.ThemeType int themeType) {
        switch (themeType) {
            case ThemeConstant.BIRTHDAY:
                return birthday;
            case ThemeConstant.VALENTINE:
                return valentine;
            case ThemeConstant.WEDDING:
                return wedding;
            case ThemeConstant.HOLIDAY:
                return holiday;
            case ThemeConstant.GREETING:
                return greeting;
            case ThemeConstant.CELEBRATION:
                return celebration;
            case ThemeConstant.LOVE:
                return love;
            case ThemeConstant.TRAVEL:
                return travel;
            case ThemeConstant.BABY:
                return baby;
            case ThemeConstant.HOT:
                return hots;
            case ThemeConstant.RAKSHABANDHAN:
                return rakshaBandHan;
            case ThemeConstant.ONAM:
                return onam;
            case ThemeConstant.ATTITUDE:
                return attitude;
            case ThemeConstant.BEAT:
                return beat;
            case ThemeConstant.HOLI:
                return holi;
            case ThemeConstant.FRIEND:
                return friend;
            case ThemeConstant.COMMON:
                return common;
            case ThemeConstant.CHRISTMAS:
                return christmas;
            case ThemeConstant.NEWYEAR:
                return newYear;
            case ThemeConstant.DAILY:
                return daily;
            case ThemeConstant.FOOD:
                return food;
            case ThemeConstant.SPORT:
                return sport;
        }
        return birthday;
    }

    /**
     * 获取全部主题
     * 分类增加修改点
     *
     * @return
     */
    public List<ThemeDownEntity> getAllList() {
        if (allCacheList != null && !allCacheList.isEmpty()) {
            return allCacheList;
        }
        allCacheList = new ArrayList<>();
        int i = 0;
        if (ObjectUtils.isEmpty(common)) {
            startIndex[i++] = 0;
            while (i < startIndex.length) {
                startIndex[i++] = 4;
            }
            return allCacheList;
        }
        List<String> themeSort = ThemeRepository.getThemeSort();
        startIndex = new int[themeSort.size() - 1];
        for (int j = 1; j < themeSort.size(); j++) {
            startIndex[i++] = allCacheList.size();
            allCacheList.addAll(getThemeList(ThemeConstant.getThemeTypeByName(themeSort.get(j))));
        }
        return allCacheList;
    }


    /**
     * 获取已下载主题
     * 分类增加修改点
     *
     * @return
     */
    public List<ThemeDownEntity> getDownLocalList() {
        List<ThemeDownEntity> allList = new ArrayList<>();
        allList.addAll(birthday);
        allList.addAll(valentine);
        allList.addAll(wedding);
        allList.addAll(baby);
        allList.addAll(holiday);
        allList.addAll(greeting);
        allList.addAll(celebration);
        allList.addAll(love);
        allList.addAll(travel);
        allList.addAll(attitude);
        allList.addAll(holi);
        allList.addAll(beat);
        allList.addAll(friend);
        allList.addAll(common);
        allList.addAll(christmas);
        allList.addAll(newYear);
        allList.addAll(food);
        allList.addAll(daily);
        allList.addAll(sport);
        List<ThemeDownEntity> result = new ArrayList<>();
//        for (ThemeDownEntity downEntity : allList) {
//            if (DownloadHelper.getSingleState(DownloadPath.BASE_PATH + downEntity.getPath()) == Downloader.STATE_LOADED) {
//                result.add(downEntity);
//            }
//        }
        return result;
    }

    public List<ThemeDownEntity> getRakshaBandHan() {
        return rakshaBandHan;
    }

    public void setRakshaBandHan(List<ThemeDownEntity> rakshaBandHan) {
        this.rakshaBandHan = rakshaBandHan;
    }

    public List<ThemeDownEntity> getOnam() {
        return onam;
    }

    public void setOnam(List<ThemeDownEntity> onam) {
        this.onam = onam;
    }

    public void setAttitude(List<ThemeDownEntity> attitude) {
        this.attitude = attitude;
    }

    public List<ThemeDownEntity> getCommon() {
        return common;
    }

    public void setCommon(List<ThemeDownEntity> common) {
        this.common = common;
    }

    public int[] getStartIndex() {
        return startIndex;
    }

    public List<ThemeDownEntity> getFood() {
        return food;
    }

    public void setFood(List<ThemeDownEntity> food) {
        this.food = food;
    }

    public List<ThemeDownEntity> getDaily() {
        return daily;
    }

    public void setDaily(List<ThemeDownEntity> daily) {
        this.daily = daily;
    }

    /**
     * editor页面的主题tab
     * @return
     */
    public int[] getThemeTabs() {
        if (startIndex[startIndex.length - 1] == 0) {
            getAllList();
        }
        int[] temp = new int[startIndex.length];
        for (int i = 0; i < startIndex.length; i++) {
            temp[i] = startIndex[i] + hots.size();
        }
        return temp;
    }


    /**
     * 版本号
     */
    private String requestVersion = null;

    public String getRequestVersion() {
        return requestVersion;
    }

    public void setRequestVersion(String requestVersion) {
        this.requestVersion = requestVersion;
    }

    /**
     * 空实体
     */
    public static ThemeGroupEntity EMPTY_THEME_GROUP_ENTITY = new ThemeGroupEntity();
}
