package com.qiusuo.videoeditor.common.bean;

import com.ijoysoft.mediasdk.common.utils.ObjectUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//分类增加修改点
public class ThemeResGroupEntity {

    private List<ThemeResourceEntity> birthday;
    private List<ThemeResourceEntity> valentine;
    private List<ThemeResourceEntity> wedding;
    private List<ThemeResourceEntity> baby;
    private List<ThemeResourceEntity> holiday;
    private List<ThemeResourceEntity> greeting;
    private List<ThemeResourceEntity> celebration;
    private List<ThemeResourceEntity> love;
    private List<ThemeResourceEntity> travel;
    private List<ThemeResourceEntity> common;
    private List<ThemeResourceEntity> newyear;
    private List<ThemeResourceEntity> food;
    private List<ThemeResourceEntity> daily;
    private List<ThemeResourceEntity> sport;

    private List<ThemeResourceEntity> allCacheList;


    //分类增加修改点

    public List<ThemeResourceEntity> getBirthday() {
        return birthday;
    }

    public void setBirthday(List<ThemeResourceEntity> birthday) {
        this.birthday = birthday;
    }

    public List<ThemeResourceEntity> getValentine() {
        return valentine;
    }

    public void setValentine(List<ThemeResourceEntity> valentine) {
        this.valentine = valentine;
    }

    public List<ThemeResourceEntity> getWedding() {
        return wedding;
    }

    public void setWedding(List<ThemeResourceEntity> wedding) {
        this.wedding = wedding;
    }

    public List<ThemeResourceEntity> getBaby() {
        return baby;
    }

    public void setBaby(List<ThemeResourceEntity> baby) {
        this.baby = baby;
    }

    public List<ThemeResourceEntity> getHoliday() {
        return holiday;
    }

    public void setHoliday(List<ThemeResourceEntity> holiday) {
        this.holiday = holiday;
    }

    public List<ThemeResourceEntity> getGreeting() {
        return greeting;
    }

    public void setGreeting(List<ThemeResourceEntity> greeting) {
        this.greeting = greeting;
    }

    public List<ThemeResourceEntity> getCelebration() {
        return celebration;
    }

    public void setCelebration(List<ThemeResourceEntity> celebration) {
        this.celebration = celebration;
    }

    public List<ThemeResourceEntity> getCommon() {
        return common;
    }

    public void setCommon(List<ThemeResourceEntity> common) {
        this.common = common;
    }

    public List<ThemeResourceEntity> getLove() {
        return love;
    }

    public void setLove(List<ThemeResourceEntity> love) {
        this.love = love;
    }

    public List<ThemeResourceEntity> getTravel() {
        return travel;
    }

    public void setTravel(List<ThemeResourceEntity> travel) {
        this.travel = travel;
    }

    public List<ThemeResourceEntity> getNewyear() {
        return newyear;
    }

    public void setNewyear(List<ThemeResourceEntity> newyear) {
        this.newyear = newyear;
    }

    public List<ThemeResourceEntity> getSport() {
        return sport;
    }

    public void setSport(List<ThemeResourceEntity> sport) {
        this.sport = sport;
    }

    /**
     * 分类增加修改点
     */
    public List<ThemeResourceEntity> getAllResource() {
        if (!ObjectUtils.isEmpty(allCacheList)) {
            return allCacheList;
        }

        allCacheList = new CopyOnWriteArrayList<>();
        if (!ObjectUtils.isEmpty(common)) {
            allCacheList.addAll(common);
        }
        if (!ObjectUtils.isEmpty(newyear)) {
            allCacheList.addAll(newyear);
        }
        if (!ObjectUtils.isEmpty(birthday)) {
            allCacheList.addAll(birthday);
        }
        if (!ObjectUtils.isEmpty(valentine)) {
            allCacheList.addAll(valentine);
        }
        if (!ObjectUtils.isEmpty(wedding)) {
            allCacheList.addAll(wedding);
        }
        if (!ObjectUtils.isEmpty(baby)) {
            allCacheList.addAll(baby);
        }
        if (!ObjectUtils.isEmpty(greeting)) {
            allCacheList.addAll(greeting);
        }
        if (!ObjectUtils.isEmpty(holiday)) {
            allCacheList.addAll(holiday);
        }

        if (!ObjectUtils.isEmpty(celebration)) {
            allCacheList.addAll(celebration);
        }

        if (!ObjectUtils.isEmpty(love)) {
            allCacheList.addAll(love);
        }

        if (!ObjectUtils.isEmpty(travel)) {
            allCacheList.addAll(travel);
        }
        if (!ObjectUtils.isEmpty(food)) {
            allCacheList.addAll(food);
        }
        if (!ObjectUtils.isEmpty(daily)) {
            allCacheList.addAll(daily);
        }
        if (!ObjectUtils.isEmpty(sport)) {
            allCacheList.addAll(sport);
        }
        return allCacheList;
    }

    /**
     * TODO index 判断先去掉
     *
     * @param index
     * @param templateName
     * @return
     */
    public ThemeResourceEntity getIndexResourEntity(int index, String templateName) {
        ThemeResourceEntity themeResourceEntity = new ThemeResourceEntity(templateName);
        int i = getAllResource().indexOf(themeResourceEntity);
        if (i >= 0) {
            return getAllResource().get(i);
        }
        return null;
    }

    public List<ThemeResourceEntity> getFood() {
        return food;
    }

    public void setFood(List<ThemeResourceEntity> food) {
        this.food = food;
    }

    public List<ThemeResourceEntity> getDaily() {
        return daily;
    }

    public void setDaily(List<ThemeResourceEntity> daily) {
        this.daily = daily;
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
}
