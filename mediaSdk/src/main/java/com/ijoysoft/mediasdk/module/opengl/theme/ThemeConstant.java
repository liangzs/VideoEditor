package com.ijoysoft.mediasdk.module.opengl.theme;

import androidx.annotation.IntDef;
import androidx.annotation.StringRes;

import com.ijoysoft.mediasdk.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

/**
 * 主题常量
 */
public class ThemeConstant {


    public static final String MY_PACKAGE = "My";

    public static final int BIRTHDAY = 0;

    public static final String BIRTHDAY_PACKAGE = "birthday";

    public static final int VALENTINE = 1;

    public static final String VALENTINE_PACKAGE = "valentine";

    public static final int WEDDING = 2;

    public static final String WEDDING_PACKAGE = "wedding";


    public static final int BABY = 3;

    public static final String BABY_PACKAGE = "baby";


    public static final int HOLIDAY = 4;

    public static final String HOLIDAY_PACKAGE = "holiday";


    public static final int GREETING = 5;

    public static final String GREETING_PACKAGE = "greeting";


    public static final int CELEBRATION = 6;

    public static final String CELEBRATION_PACKAGE = "celebration";


    public static final int LOVE = 7;

    public static final String LOVE_PACKAGE = "love";


    public static final int TRAVEL = 8;

    public static final String TRAVEL_PACKAGE = "travel";


    public static final String HOT_PACKAGE = "hot";
    public static final int HOT = 9;

    /**
     * 洒红节
     */
    public static final int HOLI = 10;
    /**
     * common
     */
    public static final int COMMON = 99;

    public static final String HOLI_PACKAGE = "holi";
    public static final String COMMON_PACKAGE = "common";


    /**
     * 独立日
     */
    public static final int INDEPENDENCE = 11;

    public static final String INDEPENDENCE_PACKAGE = "independence";

    /**
     * 湿婆节
     */
    public static final int SHIVRATRI = 12;

    public static final String SHIVRATRI_PACKAGE = "shivratri";

    /**
     * 兄妹节
     */
    public static final int RAKSHABANDHAN = 13;

    public static final String RAKSHABANDHAN_PACKAGE = "rakshabandhan";

    /**
     * 欧南节
     */
    public static final int ONAM = 14;

    public static final String ONAM_PACKAGE = "onam";

    /**
     * 排灯节
     */
    public static final int DIVALI = 15;

    public static final String DIVALI_PACKAGE = "divali";

    /**
     * 态度
     */
    public static final int ATTITUDE = 16;

    public static final String ATTITUDE_PACKAGE = "attitude";

    /**
     * beats
     */
    public static final int BEAT = 17;

    public static final String BEAT_PACKAGE = "beat";

    /**
     * friend
     */
    public static final int FRIEND = 18;

    public static final String FRIEND_PACKAGE = "friend";

    /**
     * Christmas
     */
    public static final int CHRISTMAS = 19;

    public static final String CHRISTMAS_PACKAGE = "christmas";

    /**
     * New Year
     */
    public static final int NEWYEAR = 20;

    public static final String NEWYEAR_PACKAGE = "newyear";
    /**
     * 日常
     */
    public static final int DAILY = 21;

    public static final String DAILY_PACKAGE = "daily";
    /**
     * 美食
     */
    public static final int FOOD = 22;

    public static final String FOOD_PACKAGE = "food";

    /**
     * 运动
     */
    public static final int SPORT = 23;

    public static final String SPORT_PACKAGE = "sport";

    public static final List<String> defaultThemeSort = Arrays.asList(HOT_PACKAGE, COMMON_PACKAGE, DAILY_PACKAGE, FOOD_PACKAGE, SPORT_PACKAGE, BIRTHDAY_PACKAGE, VALENTINE_PACKAGE, WEDDING_PACKAGE, BABY_PACKAGE
            , GREETING_PACKAGE, HOLIDAY_PACKAGE, CELEBRATION_PACKAGE, LOVE_PACKAGE, TRAVEL_PACKAGE);

    /**
     * 分类增加修改点
     *
     * @param name
     * @return
     */
    public static @ThemeType
    int getThemeTypeByName(String name) {
        switch (name) {
            case BIRTHDAY_PACKAGE:
                return BIRTHDAY;
            case VALENTINE_PACKAGE:
                return VALENTINE;
            case WEDDING_PACKAGE:
                return WEDDING;
            case BABY_PACKAGE:
                return BABY;
            case HOLIDAY_PACKAGE:
                return HOLIDAY;
            case GREETING_PACKAGE:
                return GREETING;
            case CELEBRATION_PACKAGE:
                return CELEBRATION;
            case LOVE_PACKAGE:
                return LOVE;
            case TRAVEL_PACKAGE:
                return TRAVEL;
            case HOT_PACKAGE:
                return HOT;
            case HOLI_PACKAGE:
                return HOLI;
            case INDEPENDENCE_PACKAGE:
                return INDEPENDENCE;
            case SHIVRATRI_PACKAGE:
                return SHIVRATRI;
            case RAKSHABANDHAN_PACKAGE:
                return RAKSHABANDHAN;
            case ONAM_PACKAGE:
                return ONAM;
            case DIVALI_PACKAGE:
                return DIVALI;
            case ATTITUDE_PACKAGE:
                return ATTITUDE;
            case BEAT_PACKAGE:
                return BEAT;
            case FRIEND_PACKAGE:
                return FRIEND;
            case COMMON_PACKAGE:
                return COMMON;
            case CHRISTMAS_PACKAGE:
                return CHRISTMAS;
            case NEWYEAR_PACKAGE:
                return NEWYEAR;
            case DAILY_PACKAGE:
                return DAILY;
            case FOOD_PACKAGE:
                return FOOD;
            case SPORT_PACKAGE:
                return SPORT;
        }
        return HOT;
    }

    /**
     * 多语言标题
     * 分类增加修改点
     *
     * @param name
     * @return
     */
    @StringRes
    public static int getThemeTitle(String name) {
        int res;
        switch (name) {
            case "My":
                res = R.string.my;
                break;
            case BIRTHDAY_PACKAGE:
                res = R.string.birthday;
                break;
            case VALENTINE_PACKAGE:
                res = R.string.valentine;
                break;
            case WEDDING_PACKAGE:
                res = R.string.wedding;
                break;
            case BABY_PACKAGE:
                res = R.string.baby;
                break;
            case HOLIDAY_PACKAGE:
                res = R.string.holiday;
                break;
            case GREETING_PACKAGE:
                res = R.string.greeting;
                break;
            case CELEBRATION_PACKAGE:
                res = R.string.celebration;
                break;
            case LOVE_PACKAGE:
                res = R.string.love;
                break;
            case TRAVEL_PACKAGE:
                res = R.string.traveling;
                break;
            case COMMON_PACKAGE:
                res = R.string.common;
                break;
            case HOT_PACKAGE:
                res = R.string.hot;
                break;
            case CHRISTMAS_PACKAGE:
                res = R.string.christmas;
                break;
            case NEWYEAR_PACKAGE:
                res = R.string.new_year;
                break;
            case FOOD_PACKAGE:
                res = R.string.food;
                break;
            case DAILY_PACKAGE:
                res = R.string.daily;
                break;
            case SPORT_PACKAGE:
                res = R.string.sport;
                break;
            case HOLI_PACKAGE:
            case INDEPENDENCE_PACKAGE:
            case SHIVRATRI_PACKAGE:
            case RAKSHABANDHAN_PACKAGE:
            case ONAM_PACKAGE:
            case DIVALI_PACKAGE:
            case ATTITUDE_PACKAGE:
            case BEAT_PACKAGE:
            case FRIEND_PACKAGE:

            default:
                res = R.string.hot;
                break;
        }
        return res;
    }

    //分类增加修改点:

    /***
     * 后续的主题列表放配置文件转线上
     *                 R.string.common,
     *                 R.string.birthday,
     *                 R.string.valentine,
     *                 R.string.wedding,
     *                 R.string.baby,
     *                 R.string.greeting,
     *                 R.string.holiday,
     *                 R.string.celebration,
     *                 R.string.love,
     *                 R.string.traveling,
     *         };
     *         分类增加修改点
     */
//    public static int[] getThemeGroupArray() {
//        return new int[]{
//                R.string.common,
//                R.string.christmas,
//                R.string.new_year,
//                R.string.birthday,
//                R.string.valentine,
//                R.string.wedding,
//                R.string.baby,
//                R.string.greeting,
//                R.string.holiday,
//                R.string.celebration,
//                R.string.love,
//                R.string.traveling
//        };
//    }

    /**
     * 分类增加修改点
     *
     * @return
     */
    public static int[] getThemeConstantArray() {
        return new int[]{
                COMMON,
                DAILY,
                FOOD,
                SPORT,
                BIRTHDAY,
                VALENTINE,
                WEDDING,
                BABY,
                GREETING,
                HOLIDAY,
                CELEBRATION,
                LOVE,
                TRAVEL,
        };
    }


    /**
     * 分类增加修改点
     */
    @IntDef({BIRTHDAY, VALENTINE, WEDDING, BABY, HOLIDAY, GREETING, CELEBRATION, LOVE, TRAVEL, HOT,
            HOLI, INDEPENDENCE, SHIVRATRI, RAKSHABANDHAN, ONAM, DIVALI, ATTITUDE, BEAT, FRIEND, COMMON, CHRISTMAS,
            NEWYEAR, DAILY, FOOD, SPORT})
    @Retention(RetentionPolicy.SOURCE)

    public @interface ThemeType {
    }


    /**
     * 分类增加修改点
     *
     * @param theme
     * @return
     */
    public static String getPackageName(int theme) {
        switch (theme) {
            case BIRTHDAY:
                return BIRTHDAY_PACKAGE;
            case VALENTINE:
                return VALENTINE_PACKAGE;
            case WEDDING:
                return WEDDING_PACKAGE;
            case BABY:
                return BABY_PACKAGE;
            case HOLIDAY:
                return HOLIDAY_PACKAGE;
            case GREETING:
                return GREETING_PACKAGE;
            case CELEBRATION:
                return CELEBRATION_PACKAGE;
            case LOVE:
                return LOVE_PACKAGE;
            case TRAVEL:
                return TRAVEL_PACKAGE;
            case HOLI:
                return HOLI_PACKAGE;
            case INDEPENDENCE:
                return INDEPENDENCE_PACKAGE;
            case SHIVRATRI:
                return SHIVRATRI_PACKAGE;
            case RAKSHABANDHAN:
                return RAKSHABANDHAN_PACKAGE;
            case ONAM:
                return ONAM_PACKAGE;
            case DIVALI:
                return DIVALI_PACKAGE;
            case ATTITUDE:
                return ATTITUDE_PACKAGE;
            case BEAT:
                return BEAT_PACKAGE;
            case FRIEND:
                return FRIEND_PACKAGE;
            case COMMON:
                return COMMON_PACKAGE;
            case CHRISTMAS:
                return CHRISTMAS_PACKAGE;
            case NEWYEAR:
                return NEWYEAR_PACKAGE;
            case DAILY:
                return DAILY_PACKAGE;
            case FOOD:
                return FOOD_PACKAGE;
            case SPORT:
                return SPORT_PACKAGE;
            default:
               return "";
        }
    }


    /**
     * 主题时间类型
     */

    public static final int THEME_ONE_DURATION = 4200;
    //主题2、gl转场等

    public static final int THEME_TWO_DURATION = 4900;
    //波浪主题，主题2等

    public static final int THEME_THREE_DURATION = 5400;

    @IntDef({THEME_ONE_DURATION, THEME_TWO_DURATION, THEME_THREE_DURATION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ThemeTimeType {
    }


}
