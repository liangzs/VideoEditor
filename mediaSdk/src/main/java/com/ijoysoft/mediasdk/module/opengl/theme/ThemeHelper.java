package com.ijoysoft.mediasdk.module.opengl.theme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.common.global.MediaSdk;
import com.ijoysoft.mediasdk.common.utils.BitmapUtil;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo;
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter;
import com.ijoysoft.mediasdk.module.opengl.particle.GlobalParticles;
import com.ijoysoft.mediasdk.module.opengl.particle.IParticleRender;
import com.ijoysoft.mediasdk.module.opengl.particle.MyMovingSystem;
import com.ijoysoft.mediasdk.module.opengl.particle.PAGNoBgParticle;
import com.ijoysoft.mediasdk.module.opengl.particle.PAGOneBgParticle;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleDrawerManager;
import com.ijoysoft.mediasdk.module.opengl.particle.ParticleType;
import com.ijoysoft.mediasdk.module.opengl.particle.MyMovingDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.action.TimeThemeRenderContainer;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BaseTimePreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.IPretreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby4.particle.BabyFourDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby7.BabySevenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby2.BabyTwoDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat1.Beat1ParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat2.Beat2ParticleColor;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat3.Beat3ParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat4.Beat4ParticleColor;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday18.BDEightteenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday11.BDElevenDrawerTwo;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday4.BDFourDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday19.BDNineteenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1.BDOneDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday7.particle.BDSevenDrawerOne;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday17.BDSeventeenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday17.BDSeventeenParticleDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday6.BDSixDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.particle.BDSixteenDrawerOne;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday10.BDTenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday12.BDTwelveDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday12.BDTwelveParticleDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday2.particleone.BDTwoDrawerOne;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday2.particletwo.BDTwoDrawerTwo;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration8.CDEightDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration9.CDNineDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration2.CDTwoDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11.Common11Paticle;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11.Common11Paticle2;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common21.Common21ParticleColor;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common24.Common24ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common31.Common31ParticleColor;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common32.Common32ParticleColor;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common9.Common9ParticleColor;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5.GreetFiveDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5.particletwo.GreetFiveParticleDrawerTwo;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting3.GreetThreeDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi1.HoliOneParticleColor;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi12.HoliTwelveParticleSystem;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday8.HDEightDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday8.HDEightDrawerTwo;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday11.HDElevenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday4.HDFourDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday14.HDFourteenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday9.HDNineDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday1.HDOneDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday6.HDSixDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday13.HDThirteenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday12.HDTwelveDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday2.HDTwoDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love8.LoveEightDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love4.LoveFourDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love7.LoveSevenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love2.LoveTwoDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel8.TravelEightDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel4.TravelFourDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel7.TravelSevenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel6.TravelSixDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel13.TravelThirteenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine13.VTThirteenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine11.particle.VTElevenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine5.particle.VTFiveDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine4.VTFourDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine14.VTFourteenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine1.particle.VTOneDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine7.VTSevenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine6.particle.VTSixDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine10.particle.VTTenDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine3.VTThreeDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding8.WedEightDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1.WeddingParticleDrawer;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding7.WedSevenDrawer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.SUFFIX;
import static com.ijoysoft.mediasdk.common.global.ConstantMediaSize.themeType;

public class ThemeHelper {


    /**
     * Action类后缀
     */
    private static final String ACTION_CLASS_SUFFIX = "Action";

    /**
     * 主题管理器类后缀
     */
    private static final String MANAGER_CLASS_SUFFIX = "ThemeManager";
    /**
     * 主题基础包，用于反射创建实例
     */
    public static final String THEME_BASE_PACKAGE = "com.ijoysoft.mediasdk.module.opengl.theme.themecontent.";


    /**
     * 预处理类 包位置
     */
    public static final String PRE_TREATMENT_BASE_PACKAGE = "com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.";


    /**
     * 预处理类后缀
     */
    private static final String PRE_CLASS_SUFFIX = "PreTreatment";


    /**
     * 获取主题管理类
     * TODO: To optimize
     *
     * @return
     */
    public static ThemeIRender createThemeManager() {
        try {
            return themeType.getThemeManagerClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 粒子管理
     *
     * @return
     */
    public static ParticleDrawerManager createParticles() {
        return createParticles(themeType);
    }


    public static ParticleDrawerManager createParticles(ThemeEnum themeType) {
        ParticleDrawerManager particlesDrawer = new ParticleDrawerManager();
//        particlesDrawer.addParticle(new ParticleBuilder(ParticleType.EXPAND, Arrays.asList(
//                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_light),
//                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_light),
//                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_light),
//                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_light)
//        )).setParticleCount(25)
//                .setPointSize(3, 20).setSelfRotate(true).setScale(true).build());
        switch (themeType) {
            case WEDDING_ONE:
                particlesDrawer.addParticle(new WeddingParticleDrawer());
                break;
            case WEDDING_SEVEN:
                particlesDrawer.addParticle(new WedSevenDrawer());
                break;
            case BIRTHDAY_ONE:
                particlesDrawer.addParticle(new BDOneDrawer());
                break;
            case BIRTHDAY_TWO:
                particlesDrawer.addParticle(new BDTwoDrawerOne());
                particlesDrawer.addParticle(new BDTwoDrawerTwo());
                break;
            case BIRTHDAY_THREE:
                break;
            case BIRTHDAY_FOUR:
                particlesDrawer.addParticle(new BDFourDrawer());
                break;
            case BIRTHDAY_FIVE:
                break;
            case BIRTHDAY_SIX:
                particlesDrawer.addParticle(new BDSixDrawer());
                break;
            case BIRTHDAY_SEVEN:
                particlesDrawer.addParticle(new BDSevenDrawerOne());
                break;
            case BIRTHDAY_EIGHT:
                break;
            case BIRTHDAY_NINE:
                break;
            case BIRTHDAY_TEN:
                particlesDrawer.addParticle(new BDTenDrawer());
                break;
            case BIRTHDAY_ELEVEN:
                particlesDrawer.addParticle(new BDElevenDrawerTwo());
                break;
            case BIRTHDAY_TWELVE:
                particlesDrawer.addParticle(new BDTwelveParticleDrawer());
                particlesDrawer.addParticle(new BDTwelveDrawer());
                break;
            case BIRTHDAY_THIRTEEN:
                break;
            case BIRTHDAY_FOURTEEN:
                break;
            case BIRTHDAY_FIFTEEN:
                break;
            case BIRTHDAY_SIXTEEN:
                particlesDrawer.addParticle(new BDSixteenDrawerOne());
                break;
            case BIRTHDAY_SEVENTEEN:
                particlesDrawer.addParticle(new BDSeventeenDrawer());
                particlesDrawer.addParticle(new BDSeventeenParticleDrawer());
                break;
            case BIRTHDAY_EIGHTEEN:
                particlesDrawer.addParticle(new BDEightteenDrawer());
                break;
            case BIRTHDAY_NINETEEN:
                particlesDrawer.addParticle(new BDNineteenDrawer());
                break;
            case VALENTINE_ONE:
                particlesDrawer.addParticle(new VTOneDrawer());
                break;
            case VALENTINE_TWO:
                break;
            case VALENTINE_THREE:
                particlesDrawer.addParticle(new VTThreeDrawer());
                break;
            case VALENTINE_FOUR:
                particlesDrawer.addParticle(new VTFourDrawer());
                break;
            case VALENTINE_FIVE:
                particlesDrawer.addParticle(new VTFiveDrawer());
                break;
            case VALENTINE_SIX:
                particlesDrawer.addParticle(new VTSixDrawer());
                break;
            case VALENTINE_SEVEN:
                particlesDrawer.addParticle(new VTSevenDrawer());
                break;
            case VALENTINE_EIGHT:
                break;
            case VALENTINE_NINE:
                break;
            case VALENTINE_TEN:
                particlesDrawer.addParticle(new VTTenDrawer());
                break;
            case VALENTINE_ELEVEN:
                particlesDrawer.addParticle(new VTElevenDrawer());
                break;
            case VALENTINE_THIRTEEN:
                particlesDrawer.addParticle(new VTThirteenDrawer());
                break;
            case VALENTINE_FOURTEEN:
                particlesDrawer.addParticle(new VTFourteenDrawer());
                break;
            case VALENTINE17:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, GlobalParticles.PAG_PARTICLE6.getSavePath()));
                break;
            case VALENTINE18:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, GlobalParticles.PAG_PARTICLE6.getSavePath()));
                break;
            case BABY_TWO:
                particlesDrawer.addParticle(new BabyTwoDrawer());
                break;
            case BABY_FOUR:
                particlesDrawer.addParticle(new BabyFourDrawer());
                break;
            case BABY_SEVEN:
                particlesDrawer.addParticle(new BabySevenDrawer());
                break;
            case HOLIDAY_ONE:
                particlesDrawer.addParticle(new HDOneDrawer());
                break;
            case HOLIDAY_TWO:
                particlesDrawer.addParticle(new HDTwoDrawer());
                break;
            case HOLIDAY_FOUR:
                particlesDrawer.addParticle(new HDFourDrawer());
                break;
            case HOLIDAY_SIX:
                particlesDrawer.addParticle(new HDSixDrawer());
                break;
            case HOLIDAY_EIGHT:
                particlesDrawer.addParticle(new HDEightDrawer());
                particlesDrawer.addParticle(new HDEightDrawerTwo());
                break;
            case HOLIDAY_NINE:
                particlesDrawer.addParticle(new HDNineDrawer());
                break;
            case HOLIDAY_ELEVEN:
                particlesDrawer.addParticle(new HDElevenDrawer());
                break;
            case HOLIDAY_TWELVE:
                particlesDrawer.addParticle(new HDTwelveDrawer());
                break;
            case HOLIDAY_THIRTEEN:
                particlesDrawer.addParticle(new HDThirteenDrawer());
                break;
            case HOLIDAY_FOURTEEN:
                particlesDrawer.addParticle(new HDFourteenDrawer());
                break;
            case HOLIDAY_FIFTEEN:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday15_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday15_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday15_particle2" + SUFFIX))
                        )
                        .setParticleCount(12)
                        .setParticleSizeRatio(0.02f)
                        .build()
                );
                break;
            case HOLIDAY_SIXTEEN:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(Collections.singletonList(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday16_particle1" + SUFFIX)))
                        .setParticleCount(12)
                        .setParticleSizeRatio(0.02f)
                        .build()
                );
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday16_particle2" + SUFFIX)
                                        )
                                )
                                .setParticleCount(16)
                                .setShortLife(0.5f)
                                .setParticleSizeDelta(2f)
                                .build()
                );
                break;
            case HOLIDAY_SEVENTEEN:
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.FALLING, Arrays.asList(
                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday17_particle1" + SUFFIX)))
                        .setParticleCount(5)
                        .setPointSize(3, 40)
                        .setLifeTime(4, 1)
                        .setSelfRotate(true)
                        .build());
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, Arrays.asList(
                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday17_particle2")))
                        .setParticleCount(6)
                        .setPointSize(3, 20)
                        .setScale(true)
                        .setSelfRotate(true)
                        .setLoadAllParticleInFirstFrame(true)
                        .build());
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday17_particle3" + SUFFIX)))
                        .setParticleCount(5)
                        .setParticleSizeRatio(0.02f)
                        .setSizeFloatingStart(0.3f)
                        .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                        .build()
                );
                break;
            case HOLIDAY_EIGHTEEN:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                                .setListBitmaps(Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday18_particle1"),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday18_particle2"),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday18_particle3"),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday18_particle4"),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday18_particle5"))
                                )
                                .setParticleCount(16)
                                .setShortLife(0.5f)
                                .setParticleSizeDelta(2f)
                                .build()
                );

                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, Arrays.asList(
                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday18_particle6")
                        , BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday18_particle7"))).setParticleCount(12)
                        .setPointSize(3, 20)
                        .setScale(true)
                        .setSelfRotate(true)
                        .setColor(true, new Common31ParticleColor())
                        .setLoadAllParticleInFirstFrame(true)
                        .build());
                break;
            case HOLIDAY_NINETEEN:
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.FALLING, Arrays.asList(
                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday19_particle1")))
                        .setParticleCount(30)
                        .setPointSize(3, 30)
                        .setLifeTime(4, 1)
                        .setSelfRotate(true)
                        .build());
                break;
            case CELEBRATION_TWO:
                particlesDrawer.addParticle(new CDTwoDrawer());
                break;
            case CELEBRATION_EIGHT:
                particlesDrawer.addParticle(new CDEightDrawer());
                break;
            case CELEBRATION_NINE:
                particlesDrawer.addParticle(new CDNineDrawer());
                break;
            case GREETING_THREE:
                particlesDrawer.addParticle(new GreetThreeDrawer());
                break;
            case GREETING_FIVE:
                GreetFiveDrawer cdTenDrawer = new GreetFiveDrawer();
                cdTenDrawer.setDelayStart(ConstantMediaSize.END_OFFSET);
                particlesDrawer.addParticle(cdTenDrawer);
                particlesDrawer.addParticle(new GreetFiveParticleDrawerTwo());
                break;
            case LOVE_TWO:
                particlesDrawer.addParticle(new LoveTwoDrawer());
                break;
            case LOVE_FOUR:
                particlesDrawer.addParticle(new LoveFourDrawer());
                break;
            case LOVE_SEVEN:
                particlesDrawer.addParticle(new LoveSevenDrawer());
                break;
            case LOVE_EIGHT:
                particlesDrawer.addParticle(new LoveEightDrawer());
                break;
            case LOVE_NINE:
                List list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_nine_particle2"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(15)
                        .setPointSize(10, 15).setLifeTime(5, 3).setScale(true).setSelfRotate(true).build());
                break;
            case LOVE_THIRDTEEN:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_third_particle1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_third_particle2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_third_particle3"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(10)
                        .setPointSize(10, 15).setLifeTime(5, 3).setScale(true).setSelfRotate(true).build());
                break;
            case LOVE_FIFTEEN:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/love_fifteen_particle"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(10)
                        .setPointSize(10, 15).setLifeTime(5, 3).setScale(true).setSelfRotate(false).build());
                break;
            case WEDDING_EIGHT:
                particlesDrawer.addParticle(new WedEightDrawer());
                break;
            case TRAVEL_FOUR:
                particlesDrawer.addParticle(new TravelFourDrawer());
                break;
            case TRAVEL_THIRTEEN:
                particlesDrawer.addParticle(new TravelThirteenDrawer());
                break;
            case TRAVEL_SIX:
                particlesDrawer.addParticle(new TravelSixDrawer());
                break;
            case TRAVEL_SEVEN:
                particlesDrawer.addParticle(new TravelSevenDrawer());
                break;
            case TRAVEL_EIGHT:
                particlesDrawer.addParticle(new TravelEightDrawer());
                break;

            case TRAVEL_FOURTEEN:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, ConstantMediaSize.themePath + "/travel14_particle"));
                break;
            /*holi*/
            case HOLI_ONE:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_one_particle1"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(20).setColor(true, new HoliOneParticleColor())
                        .setLifeTime(5, 2)
                        .setPointSize(3, 20).setScale(true).build());
                break;
            case HOLI_TWO:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_two_particle1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_two_particle2"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.EXPAND, list).setParticleCount(25)
                        .setPointSize(3, 20).setSelfRotate(true).setScale(true).build());
                break;

            case HOLI_ELEVEN:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_eleven_particle1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_eleven_particle2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_eleven_particle3"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_eleven_particle4"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(20)
                        .setPointSize(2, 20).setScale(true).setSelfRotate(true).build());
                break;
            case HOLI_TWELVE:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_twelve_particle1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holi_twelve_particle2"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(10).setColor(true)
                        .setPointSize(3, 10).setScale(true).setSelfRotate(true).setCustomSystom(new HoliTwelveParticleSystem()).build());
                break;
            /*beat*/
            case BEAT_ONE:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_one_particle1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_one_particle2"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(20).setColor(true).setLifeTime(5, 2)
                        .setPointSize(3, 10).setScale(true).setiAddParticle(new Beat1ParticleSystem()).build());
                break;
            case BEAT_TWO:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_two_particle1"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.FALLING, list).setParticleCount(20).setColor(true, new Beat2ParticleColor()).setLifeTime(4, 3)
                        .setPointSize(2, 20).build());
                break;
            case BEAT_THREE:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_three_particle1"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.FIX_COORD, list).setParticleCount(6).setColor(true)
                        .setScale(true).setCustomSystom(new Beat3ParticleSystem()).build());
                break;
            case BEAT_FOUR:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_four_particle1"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(15).
                        setColor(true, new Beat4ParticleColor()).setLifeTime(4, 3).setScale(true)
                        .setPointSize(7, 30).build());
                break;
            case BEAT_FIVE:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_five_particle1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_five_particle2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_five_particle3"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.FLYING, list).setParticleCount(6)
                        .setScale(true).setPointSize(6, 20).build());
                break;
            case BEAT_SIX:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_six_particle1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_six_particle2"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(15).setLifeTime(4, 3).setScale(true)
                        .setPointSize(3, 20).build());
                break;
            case BEAT_SEVEN:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_seven_particle1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_seven_particle2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_seven_particle3"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(15).setLifeTime(4, 3).setScale(true)
                        .setPointSize(3, 20).build());
                break;
            case BEAT_EIGHT:
                list = new ArrayList<>();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_eight_particle1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_eight_particle2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/beat_eight_particle3"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(15).setLifeTime(4, 3).setScale(true)
                        .setPointSize(3, 20).build());
                break;

            case RAKSHABANDHAN_TWO:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                                .setListBitmaps(Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rakshabandhan2_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rakshabandhan2_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rakshabandhan2_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rakshabandhan2_particle4" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rakshabandhan2_particle5" + SUFFIX)
                                ))
                                .setParticleCount(32)
                                .setParticleSizeRatio(0.3f)
                                .build()
                );
                break;

            case RAKSHABANDHAN_SIX:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Collections.singletonList(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rakshabandhan6_particle" + SUFFIX))
                        )
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.03f)
                        .build()
                );
                break;

            case RAKSHABANDHAN_SEVEN:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rakshabandhan7_particle" + SUFFIX)
                                        )
                                )
                                .setParticleCount(3)
                                .setParticleSizeDelta(0.5f)
                                .build()
                );
                break;
            case RAKSHABANDHAN_EIGHT:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rakshabandhan8_particle" + SUFFIX)
                                        )
                                )
                                .setParticleCount(12)
                                .setParticleSizeRatio(0.5f)
                                .build()
                );
                break;
            case RAKSHABANDHAN_NINE:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/rakshabandhan9_particle" + SUFFIX)
                                        )
                                )
                                .setParticleCount(32)
                                .setParticleSizeRatio(0.5f)
                                .build()
                );
                break;


            case ONAM_ONE:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/onam1_particle1" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/onam1_particle2" + SUFFIX)
                                        )
                                )
                                .setParticleCount(16)
                                .setParticleSizeRatio(0.002f)
                                .build()
                );
                break;

            case ONAM_TWO:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/onam2_particle" + SUFFIX)
                                        )
                                )
                                .setParticleCount(24)
                                .setParticleSizeRatio(0.03f)
                                //随机最大透明度
                                .setRandomAlpha(true)
                                .build()
                );
                break;
            case ATTITUDE_ONE:

                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude1_light" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude1_pink_particle" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude1_purple_particle" + SUFFIX)
                                        )
                                )
                                .setParticleCount(8)
                                .setParticleSizeRatio(0.05f)
                                .build()
                );
                MyMovingDrawer.ParticleUpdaterWithScreenRatio attitude1 = new MyMovingDrawer.ParticleUpdaterWithScreenRatio() {
                    float yOffset = 0.01f;


                    @Override
                    public void onRatioChanged(RatioType ratioType) {
                        yOffset = ratioType.getValue() * 0.01f;
                    }

                    @Override
                    public void update(float[] particle, int offset, int length) {
                        //向左下移动
                        particle[offset] -= 0.01f;
                        particle[offset + 1] += yOffset;
                    }
                };
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.MOVING)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude1_meteor" + SUFFIX)
                                        )
                                )
                                .setParticleCount(16)
                                .setParticleSizeRatio(0.03f)
                                .setPositionUpdater(attitude1)
                                .build()
                );
                break;

            case ATTITUDE_TWO:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_particle1" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_particle2" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_particle3" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_particle4" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_particle5" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_particle6" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_particle7" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude2_particle8" + SUFFIX)
                                        )
                                )
                                .setParticleCount(16)
                                .setParticleSizeRatio(0.02f)
                                .build()
                );
                break;
            case ATTITUDE_THREE:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude3_blue_particel" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude3_yellow_particle3" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude3_pink_particle2" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude3_cyan_particle2" + SUFFIX)
                                        )
                                )
                                .setParticleCount(8)
                                .setParticleSizeRatio(0.03f)
                                .setSizeFreeze(true)
                                .build()
                );
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude3_red_particle" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude3_pinkl_particle" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude3_yellow_particle" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude3_cyan_particle" + SUFFIX)
                                        )
                                )
                                .setParticleCount(8)
                                .setParticleSizeRatio(0.03f)
                                .setSizeFreeze(true)
                                .build()
                );
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude3_red_shine" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude3_orange_shine" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude3_cyan_shine" + SUFFIX)
                                        )
                                )
                                .setParticleCount(6)
                                .setParticleSizeRatio(0.03f)
                                .setSizeFreeze(true)
                                .build()
                );
                break;
            case ATTITUDE_FIVE:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude5_gray_gradient" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude5_pink_gradient" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude5_purple_gradient" + SUFFIX)
                                )
                        )
                        .setParticleCount(12)
                        .setParticleSizeRatio(0.16f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude5_green_particle" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude5_red_particle" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude5_yellow_particle" + SUFFIX)
                                )
                        )
                        .setParticleCount(8)
                        .setParticleSizeRatio(0.06f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude5_shine" + SUFFIX)
                                )
                        )
                        .setParticleCount(8)
                        .setSizeFreeze(true)
                        .setParticleSizeDelta(3f)
                        .build()
                );
                break;
            case ATTITUDE_SIX:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude6_gray_heart" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude6_shine_heart" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude6_red_heart" + SUFFIX)
                                )
                        )
                        .setParticleCount(8)
                        .setParticleSizeRatio(0.03f)
                        .setPositionUpdater((particle, offset, length) ->
                                particle[offset + 1] -= 0.01f
                        )
                        .build()
                );
                break;
            case ATTITUDE_SEVEN:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude7_light2" + SUFFIX)
                                )
                        )
                        .setParticleCount(12)
                        .setSizeFreeze(true)
                        .setParticleSizeRatio(0.16f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude7_light1" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.1f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude7_green_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude7_green_particle2" + SUFFIX)
                                )
                        )
                        .setParticleCount(8)
                        .setSizeFloatingStart(0.1f)
                        .setParticleSizeRatio(0.03f)
                        .build()
                );
                break;
            case ATTITUDE_EIGHT:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude8_white_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude8_white_particle2" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.08f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude8_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude8_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude8_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude8_particle4" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude8_particle5" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude8_particle6" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/attitude8_particle7" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.008f)
                        .build()
                );
                break;
            case BIRTHDAY_TWENTY:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/brithday20_green_particle" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/brithday20_blue_particle" + SUFFIX)
                                )
                        )
                        .setParticleCount(8)
                        .setSizeFloatingStart(0.3f)
                        .setParticleSizeRatio(0.03f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/brithday20_white_particle" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setSizeFloatingStart(0f)
                        .setParticleSizeRatio(0.03f)
                        .build()
                );
                break;
            case BIRTHDAY_TWENTY_ONE:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday21_star" + SUFFIX)
                                )
                        )
                        .setParticleCount(24)
                        .setSizeFloatingStart(0.3f)
                        .setParticleSizeRatio(0.03f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday21_green_particle" + SUFFIX)
                                )
                        )
                        .setParticleCount(3)
                        .setSizeFloatingStart(0f)
                        .setParticleSizeRatio(0.03f)
                        .build()
                );
                break;

            case BIRTHDAY_TWENTY_TWO:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday22_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday22_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday22_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday22_particle4" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday22_particle5" + SUFFIX)
                                )
                        )
                        .setParticleCount(24)
                        .setParticleSizeRatio(0.03f)
                        .setPositionUpdater((particle, offset, length) ->
                                particle[offset + 1] += 0.01f
                        )
                        .setStartPositionGenerator(new MyMovingSystem.ParticleStartPositionGenerator() {
                            final Random random = new Random();

                            @Override
                            public void setPosition(float[] particle, int offset) {
                                particle[offset] = random.nextFloat() * 2f - 1f;
                                particle[offset + 1] = -1f;
                                particle[offset + 2] = 0f;
                            }
                        })
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday22_heart" + SUFFIX)
                                )
                        )
                        .setParticleCount(3)
                        .setParticleSizeRatio(0.06f)
                        .build()
                );
                break;
            case BIRTHDAY_TWENTY_THREE:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday23_yellow_particle" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setSizeFloatingStart(0.3f)
                        .setParticleSizeRatio(0.06f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday23_pink_particle" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday23_white_particle" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setSizeFloatingStart(0.1f)
                        .setParticleSizeRatio(0.03f)
                        .build()
                );
                break;
            case BIRTHDAY_TWENTY_FOUR:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday24_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday24_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday24_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday24_particle4" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday24_particle5" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday24_particle6" + SUFFIX)
                                )
                        )
                        .setParticleCount(24)
                        .setParticleSizeRatio(0.015f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                        .build()
                );
                break;
            case BIRTHDAY_TWENTY_FIVE:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday25_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday25_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday25_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday25_particle4" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday25_particle5" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday25_particle6" + SUFFIX)
                                )
                        )
                        .setParticleCount(64)
                        .setParticleSizeRatio(0.03f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                        .build()
                );
                break;
            case BIRTHDAY_TWENTY_SIX:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday26_white_heart" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday26_blur_heart" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday26_heart" + SUFFIX)
                                )
                        )
                        .setParticleCount(12)
                        .setParticleSizeRatio(0.06f)
                        .build()
                );
                break;
            case BIRTHDAY_TWENTY_SEVEN:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday27_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday27_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday27_particle3" + SUFFIX)
                                )
                        )
                        .setParticleCount(32)
                        .setParticleSizeRatio(0.015f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                        .build()
                );
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.MOVING)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday27_particle4" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday27_particle5" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday27_particle6" + SUFFIX)
                                        )
                                )
                                .setParticleCount(4)
                                .setParticleSizeRatio(0.06f)
                                .setSizeFreeze(true)
                                .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                                .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                                .build()
                );
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday27_particle7" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday27_particle8" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/birthday27_yello_particle" + SUFFIX)
                                        )
                                )
                                .setParticleCount(32)
                                .setParticleSizeRatio(0.012f)
                                .build()
                );
                break;

            case GREETING_SIX:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greet6_particle" + SUFFIX)
                                )
                        )
                        .setParticleCount(12)
                        .setParticleSizeRatio(0.06f)
                        .build()
                );
                break;
            case GREETING_SEVEN:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting7_heart" + SUFFIX)
                                )
                        )
                        .setParticleCount(6)
                        .setParticleSizeRatio(0.015f)
                        .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting_circle" + SUFFIX)
                                )
                        )
                        .setParticleCount(8)
                        .setParticleSizeRatio(0.06f)
                        .build()
                );
                break;
            case GREETING_EIGHT:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting8_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting8_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting8_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting8_particle4" + SUFFIX)
                                )
                        )
                        .setParticleCount(32)
                        .setSizeFloatingStart(0.1f)
                        .setParticleSizeRatio(0.015f)
                        .build()
                );
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.MOVING)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting8_pink_heart1" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting8_pink_heart2" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting8_white_heart1" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting8_white_heart2" + SUFFIX)
                                        )
                                )
                                .setParticleCount(16)
                                .setParticleSizeRatio(0.03f)
                                .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                                .build()
                );
                break;
            case GREETING_NINE:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting9_particle_white" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/greeting9_particle_yellow" + SUFFIX)
                                )
                        )
                        .setParticleCount(32)
                        .setParticleSizeDelta(5f)
                        .build()
                );
                break;
            case FRIEND_ONE:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend1_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend1_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend1_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend1_particle4" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend1_particle5" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend1_particle6" + SUFFIX)
                                )
                        )
                        .setParticleCount(24)
                        .setParticleSizeRatio(0.06f)
                        .build()
                );
                break;
            case FRIEND_TWO:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend2_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend2_alpha_particle" + SUFFIX)
                                )
                        )
                        .setParticleCount(6)
                        .setParticleSizeRatio(0.2f)
                        .build()
                );
                MyMovingDrawer.ParticleUpdaterWithScreenRatio friend2Updater = new MyMovingDrawer.ParticleUpdaterWithScreenRatio() {
                    float yOffset = 0.03f;


                    @Override
                    public void onRatioChanged(RatioType ratioType) {
                        yOffset = ratioType.getValue() * 0.03f;
                    }

                    @Override
                    public void update(float[] particle, int offset, int length) {
                        //向左下移动
                        particle[offset] += 0.03f;
                        particle[offset + 1] += yOffset;
                    }
                };
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.MOVING)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend2_flash_star" + SUFFIX)
                                        )
                                )
                                .setParticleCount(16)
                                .setParticleSizeRatio(0.03f)
                                .setPositionUpdater(friend2Updater)
                                .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend2_light" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend2_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend2_particle2" + SUFFIX)
                                )
                        )
                        .setParticleCount(32)
                        .setSizeFloatingStart(0.1f)
                        .setParticleSizeRatio(0.015f)
                        .build()
                );
                break;
            case FRIEND_THREE:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend3_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend3_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend3_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend3_particle4" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend3_particle5" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend3_particle6" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend3_particle7" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend3_particle8" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.1f)
                        .build()
                );
                break;
            case FRIEND_FOUR:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend4_shine1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend4_shine2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/friend4_shine3" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.1f)
                        .build()
                );
                break;
            case WEDDING_ELEVEN:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding11_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding11_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding11_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding11_particle4" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding11_particle5" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding11_particle6" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.1f)
                        .build()
                );
                break;
            case WEDDING_TWELVE:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding12_heart1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding12_heart2" + SUFFIX)
                                )
                        )
                        .setParticleCount(12)
                        .setParticleSizeRatio(0.06f)
                        .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding12_shine1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding12_shine2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding12_shine3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding12_shine4" + SUFFIX)
                                )
                        )
                        .setParticleCount(4)
                        .setParticleSizeDelta(4f)
                        .build()
                );
                break;

            case COMMON_ONE:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_light),
                                                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_pink_particle),
                                                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_purple_particle)
                                        )
                                )
                                .setParticleCount(8)
                                .setParticleSizeRatio(0.05f)
                                .build()
                );
                MyMovingDrawer.ParticleUpdaterWithScreenRatio common1 = new MyMovingDrawer.ParticleUpdaterWithScreenRatio() {
                    float yOffset = 0.01f;

                    @Override
                    public void onRatioChanged(RatioType ratioType) {
                        yOffset = ratioType.getValue() * 0.01f;
                    }

                    @Override
                    public void update(float[] particle, int offset, int length) {
                        //向左下移动
                        particle[offset] -= 0.01f;
                        particle[offset + 1] += yOffset;
                    }
                };
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.MOVING)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_meteor)
                                        )
                                )
                                .setParticleCount(16)
                                .setParticleSizeRatio(0.03f)
                                .setPositionUpdater(common1)
                                .build()
                );
                break;
            case COMMON_TWO:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common2_light2)
                                )
                        )
                        .setParticleCount(12)
                        .setSizeFreeze(true)
                        .setParticleSizeRatio(0.16f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common2_light1)
                                )
                        )
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.1f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common2_green_particle1),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common2_green_particle2)
                                )
                        )
                        .setParticleCount(8)
                        .setParticleSizeRatio(0.06f)
                        .build()
                );
                break;
            case COMMON_THREE:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common3_particle1),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common3_particle2),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common3_particle3),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common3_particle4),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common3_particle5),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common3_particle6)
                                )
                        )
                        .setParticleCount(24)
                        .setParticleSizeRatio(0.06f)
                        .build()
                );
                break;
            case COMMON_FOUR:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common4_particle3),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common4_alpha_particle)
                                )
                        )
                        .setParticleCount(6)
                        .setParticleSizeRatio(0.2f)
                        .build()
                );
                MyMovingDrawer.ParticleUpdaterWithScreenRatio common4Updater = new MyMovingDrawer.ParticleUpdaterWithScreenRatio() {
                    float yOffset = 0.03f;


                    @Override
                    public void onRatioChanged(RatioType ratioType) {
                        yOffset = ratioType.getValue() * 0.03f;
                    }

                    @Override
                    public void update(float[] particle, int offset, int length) {
                        //向左下移动
                        particle[offset] -= 0.03f;
                        particle[offset + 1] += yOffset;
                    }
                };
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.MOVING)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common4_flash_star)
                                        )
                                )
                                .setParticleCount(16)
                                .setParticleSizeRatio(0.03f)
                                .setPositionUpdater(common4Updater)
                                .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common4_light),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common4_particle1),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common4_particle2)
                                )
                        )
                        .setParticleCount(32)
                        .setSizeFloatingStart(0.1f)
                        .setParticleSizeRatio(0.015f)
                        .build()
                );
                break;
            case COMMON_SIX:

                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common6_star1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common6_star2" + SUFFIX)
                                )
                        )
                        .setLoadAllParticleInFirstFrame(true)
                        .setParticleCount(32)
                        .setParticleSizeDelta(5f)
                        .setShortLife(0.5f)
                        .build()
                );
                break;
            case HOLIDAY_TWENTYSIX:

                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday26_star1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/holiday26_star2" + SUFFIX)
                                )
                        )
                        .setLoadAllParticleInFirstFrame(true)
                        .setParticleCount(32)
                        .setParticleSizeDelta(5f)
                        .setShortLife(0.5f)
                        .build()
                );
                break;
            case COMMON_ELEVEN:
                Common11Paticle common11Paticle = new Common11Paticle();
                list = new ArrayList();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle4"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.FALLING, list).setParticleCount(24)
                        .setPointSize(3, 30)
                        .setLoadAllParticleInFirstFrame(true)
                        .setIDirectionAction(common11Paticle)
                        .setILocationAction(common11Paticle)
                        .setIAccelerate(common11Paticle)
                        .build());

                //闪耀持续两个周期
                list = new ArrayList();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common11_particle5"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.SCALE_LOOP, list).setParticleCount(10)
                        .setPointSize(2, 10)
                        .setPointScalePeriod(1.5f)
                        .build());
                break;
            case COMMON_THIRDTEEN:
            case COMMON_FOURTEEN:
            case DAILY_3:
                list = new ArrayList();
                list.add(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common_particle_heart));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(10)
                        .setPointSize(4, 20)
                        .setMinPointSize(2)
                        .setAlpha(true)
                        .build());
                list = new ArrayList();
                list.add(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common_particle_star));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.SCALE_LOOP, list).setParticleCount(8)
                        .setPointSize(2, 10)
                        .build());
                break;
            case COMMON_FIFTEEN:
            case HOLIDAY_TWENTYSEVEN:
                list = new ArrayList();
                list.add(BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common_white_particle));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(40)
                        .setPointSize(4, 20)
                        .setAlpha(true)
                        .build());
                break;
            case COMMON_TWENTYNINE:
                common11Paticle = new Common11Paticle();
                list = new ArrayList();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common29_particle2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common29_particle3"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.FALLING, list).setParticleCount(24)
                        .setPointSize(3, 30)
                        .setLoadAllParticleInFirstFrame(true)
                        .setIDirectionAction(common11Paticle)
                        .setILocationAction(common11Paticle)
                        .setIAccelerate(common11Paticle)
                        .build());
                break;
            case COMMON_THIRDTYONE:
                list = new ArrayList();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common31_particle1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common31_particle2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common31_particle3"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common31_particle4"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common31_particle5"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.FALLING, list)
                        .setParticleCount(30)
                        .setPointSize(3, 30)
                        .setLifeTime(4, 1)
                        .setSelfRotate(true)
                        .build());

                list = new ArrayList();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common31_particle6"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(12)
                        .setPointSize(3, 20)
                        .setScale(true)
                        .setSelfRotate(true)
                        .setColor(true, new Common31ParticleColor())
                        .setLoadAllParticleInFirstFrame(true)
                        .build());
                break;
            case COMMON_NINE:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common9_particle1")
                                )
                        )
                        .setParticleCount(8)
                        .setParticleSizeRatio(0.01f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .setLoadAllParticleInFirstFrame(true)
                        .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                        .build()
                );

                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common9_particle2")
                                )
                        )
                        .setParticleCount(64)
                        .setParticleSizeRatio(0.01f)
                        .setRandomAlphaStart(0f)
                        .setSizeFloatingStart(0.3f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .setLoadAllParticleInFirstFrame(true)
                        .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                        .setColor(true, new Common9ParticleColor())
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common9_particle3")
                                )
                        )
                        .setParticleCount(4)
                        .setParticleSizeRatio(0.01f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .setLoadAllParticleInFirstFrame(true)
                        .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                        .build()
                );
                break;

            case COMMON_TEN:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_light),
                                                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_pink_particle),
                                                BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_purple_particle)
                                        )
                                )
                                .setParticleCount(8)
                                .setParticleSizeRatio(0.05f)
                                .build()
                );
                break;
            case COMMON_SIXTEEN:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common16_particle0" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common16_particle1" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common16_particle2" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common16_particle3" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common16_particle4" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common16_particle5" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common16_particle6" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common16_particle7" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common16_particle8" + SUFFIX)
                                        )
                                )
                                .setParticleCount(24)
                                .setParticleSizeRatio(0.05f)
                                .setParticleSizeDelta(3f)
                                .build()
                );
                break;
            case COMMON_SEVENTEEN:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common17_star" + SUFFIX)
                                        )
                                )
                                .setParticleCount(16)
                                .setShortLife(0.5f)
                                .setParticleSizeDelta(2f)
                                .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common17_red_heart" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common17_yellow_heart" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common17_purple_heart" + SUFFIX)
                                )
                        )
                        .setParticleCount(8)
                        .setParticleSizeRatio(0.02f)
                        .setSizeFloatingStart(0.3f)
                        .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                        .build()
                );
                break;

            case COMMON_EIGHTEEN:
            case VALENTINE16:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common18_heart1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common18_heart2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common18_heart3" + SUFFIX)
                                )
                        )
                        .setParticleCount(8)
                        .setSizeFloatingStart(0.1f)
                        .setParticleSizeRatio(0.045f)
                        .build()
                );

                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common18_star1" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common18_star2" + SUFFIX)
                                        )
                                )
                                .setParticleCount(16)
                                .setShortLife(0.5f)
                                .setParticleSizeDelta(16f)
                                .build()
                );
                break;
            case COMMON_NINETEEN:
                Bitmap star1 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common19_particle1" + SUFFIX);
                Bitmap star2 = BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common19_particle2" + SUFFIX);

                MyMovingDrawer.ParticleUpdaterWithScreenRatio common19Updater1 = new MyMovingDrawer.ParticleUpdaterWithScreenRatio() {
                    float yOffset = 0.01f;


                    @Override
                    public void onRatioChanged(RatioType ratioType) {
                        yOffset = ratioType.getValue() * 0.01f * 0.70422535211267f;
                    }

                    @Override
                    public void update(float[] particle, int offset, int length) {
                        //向左下移动
                        particle[offset] -= 0.01f;
                        particle[offset + 1] += yOffset;
                    }
                };

                MyMovingDrawer.ParticleUpdaterWithScreenRatio common19Updater2 = new MyMovingDrawer.ParticleUpdaterWithScreenRatio() {
                    float yOffset = 0.01f;


                    @Override
                    public void onRatioChanged(RatioType ratioType) {
                        yOffset = ratioType.getValue() * 0.01f * 0.6956521739130434f;
                    }

                    @Override
                    public void update(float[] particle, int offset, int length) {
                        //向左下移动
                        particle[offset] -= 0.01f;
                        particle[offset + 1] += yOffset;
                    }
                };
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.MOVING)
                                .setListBitmaps(Collections.singletonList(star1))
                                .setParticleCount(6)
                                .setParticleSizeRatio(0.04f)
                                .setSizeFloatingStart(0.4f)
                                .setPositionUpdater(common19Updater1)
                                .build()
                );
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.MOVING)
                                .setListBitmaps(Collections.singletonList(star2))
                                .setParticleCount(6)
                                .setParticleSizeRatio(0.04f)
                                .setSizeFloatingStart(0.4f)
                                .setPositionUpdater(common19Updater2)
                                .build()
                );

                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common19_particle3" + SUFFIX)
                                        )
                                )
                                .setParticleCount(16)
                                .setShortLife(0.5f)
                                .setParticleSizeDelta(2f)
                                .build()
                );
                break;
            case COMMON_TWENTYONE:

                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common21_white_shine),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common21_red_shine),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common21_orange_shine)
                                )
                        )
                        .setParticleCount(3)
                        .setParticleSizeDelta(6f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common21_particle)
                                )
                        )
                        .setParticleCount(32)
                        .setSizeFloatingStart(0.3f)
                        .setParticleSizeRatio(0.02f)
                        .setColor(true, new Common21ParticleColor())
                        .setRandomAlphaStart(0f)
                        .setLoadAllParticleInFirstFrame(true)
                        .setRebound(true)
                        .build()
                );


                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common21_particle1),
                                        BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common21_particle2)
                                )
                        )
                        .setParticleCount(32)
                        .setParticleSizeRatio(0.02f)
                        .setSizeFloatingStart(0.3f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .setLoadAllParticleInFirstFrame(true)
                        .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                        .build()
                );
                break;
            case COMMON_TWENTYTHREE:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common23_heart1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common23_heart2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common23_heart3" + SUFFIX)
                                )
                        )
                        .setParticleCount(8)
                        .setSizeFloatingStart(0.1f)
                        .setParticleSizeRatio(0.045f)
                        .build()
                );

                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common23_star1" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common23_star1" + SUFFIX)
                                        )
                                )
                                .setParticleCount(16)
                                .setShortLife(0.5f)
                                .setParticleSizeDelta(16f)
                                .build()
                );
                break;

            case COMMON_TWENTYFOUR:

                Common24ThemeManager.ParticlePositionSystem positionSystem = new Common24ThemeManager.ParticlePositionSystem();

                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.MOVING)
                                .setListBitmaps(Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common24_cloud" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common24_cloud2" + SUFFIX)
                                ))
                                .setParticleCount(6)
                                .setParticleSizeRatio(0.02f)
                                .setSizeFloatingStart(0.4f)
                                .setPositionUpdater(positionSystem)
                                .setLoadAllParticleInFirstFrame(true)
                                .setStartPositionGenerator(positionSystem)
                                .setLoadFirstFrameByGenerator(true)
                                .build()
                );
                break;
            case COMMON_TWENTYFIVE:

                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common25_blue_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common25_blue_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common25_purple_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/common25_purple_particle2" + SUFFIX)
                                )
                        )
                        .setParticleCount(24)
                        .setSizeFloatingStart(0.3f)
                        .setParticleSizeRatio(0.025f)
                        .setLoadAllParticleInFirstFrame(true)
                        .setDisableFade(true)
                        .build()
                );
                break;


            case WEDDING_THIRTEEN:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding13_particle_alpha" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding13_particle_purple" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding13_particle_yellow" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.1f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding13_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding13_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding13_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding13_particle4" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setSizeFloatingStart(0.1f)
                        .setParticleSizeRatio(0.045f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding13_petal1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding13_petal2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding13_petal3" + SUFFIX)
                                )
                        )
                        .setParticleCount(8)
                        .setParticleSizeRatio(0.02f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .build()
                );
                break;
            case WEDDING_FOURTEEN:

                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding14_white_light" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setSizeFloatingStart(0.1f)
                        .setParticleSizeRatio(0.045f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding14_shine" + SUFFIX)
                                )
                        )
                        .setParticleCount(4)
                        .setParticleSizeDelta(4f)
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding14_particle1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding14_particle2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding14_particle3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding14_particle4" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding14_particle5" + SUFFIX)
                                )
                        )
                        .setParticleCount(12)
                        .setSizeFloatingStart(0.1f)
                        .setParticleSizeRatio(0.05f)
                        .build()
                );
            case WEDDING_FIFTEEN:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding15_heart2" + SUFFIX)
                                )
                        )
                        .setParticleCount(6)
                        .setParticleSizeRatio(0.03f)
                        .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                        .build()
                );

                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding15_heart1" + SUFFIX)
                                )
                        )
                        .setParticleCount(6)
                        .setParticleSizeRatio(0.03f)
                        .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                        .build()
                );
                break;
            case WEDDING_SIXTEEN:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding16_heart1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding16_heart2" + SUFFIX)
                                )
                        )
                        .setParticleCount(12)
                        .setParticleSizeRatio(0.03f)
                        .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding16_snow1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding16_snow2" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding16_snow3" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding16_snow4" + SUFFIX)
                                )
                        )
                        .setParticleCount(32)
                        .setParticleSizeRatio(0.015f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .build()
                );
                break;
            case WEDDING_SEVENTEEN:
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                                Collections.singletonList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding17_flower2" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.015f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .build()
                );
                particlesDrawer.addParticle(new ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(
                                Arrays.asList(
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding17_alpha1" + SUFFIX),
                                        BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/wedding17_alpha2" + SUFFIX)
                                )
                        )
                        .setParticleCount(16)
                        .setSizeFloatingStart(0.3f)
                        .setParticleSizeRatio(0.05f)
                        .build()
                );
                break;
            case HOLIDAY_THIRTYOTWO:
                particlesDrawer.addParticle(new CDTwoDrawer() {
                    @Override
                    public Bitmap loadBitmap() {
                        return BitmapFactory.decodeResource(com.ijoysoft.mediasdk.common.global.MediaSdk.getInstance().getResouce(),
                                R.mipmap.system21_particle
                        );
                    }
                });
                break;

            case CHRISTMAS_0:
                particlesDrawer.addParticle(new PAGNoBgParticle(true, ConstantMediaSize.themePath + "/christmas0_particle.pag"));
                break;
            case CHRISTMAS_1:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, ConstantMediaSize.themePath + "/christmas1.pag"));
                break;
            case CHRISTMAS_2:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, ConstantMediaSize.themePath + "/christmas2.pag"));
                break;
            case CHRISTMAS_3:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, ConstantMediaSize.themePath + "/christmas3_pag"));
                break;
            case CHRISTMAS_6:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, ConstantMediaSize.themePath + "/christmas6_pag1"));
                break;
            case NEWYEAR_0:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, ConstantMediaSize.themePath + "/newyaer0_fireworks.pag"));
                break;
            case NEWYEAR_1:
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                                .setListBitmaps(
                                        Collections.singletonList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear1_firework" + SUFFIX))
                                )
                                .setParticleCount(8)
                                .setShortLife(0.5f)
                                .setParticleSizeDelta(20f)
                                .build()
                );
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.RANDOM)
                                .setListBitmaps(
                                        Arrays.asList(
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear1_particle1" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear1_particle2" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear1_particle3" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear1_particle4" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear1_particle5" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear1_particle6" + SUFFIX),
                                                BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear1_particle7" + SUFFIX)
                                        )
                                )
                                .setParticleCount(16)
                                .setParticleSizeRatio(0.02f)
                                .build()
                );
                break;
            case NEWYEAR_2:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, ConstantMediaSize.themePath + "/newyear2.pag"));
                break;
            case NEWYEAR_3:
                list = new ArrayList();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear3_particle1"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear3_particle2"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear3_particle3"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear3_particle4"));
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear3_particle5"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.FALLING, list)
                        .setParticleCount(30)
                        .setPointSize(3, 30)
                        .setLifeTime(4, 1)
                        .setSelfRotate(true)
                        .build());

                list = new ArrayList();
                list.add(BitmapUtil.decriptImage(ConstantMediaSize.themePath + "/newyear3_particle6"));
                particlesDrawer.addParticle(new ParticleBuilder(ParticleType.HOVER, list).setParticleCount(12)
                        .setPointSize(3, 20)
                        .setScale(true)
                        .setSelfRotate(true)
                        .setColor(true, new Common31ParticleColor())
                        .setLoadAllParticleInFirstFrame(true)
                        .build());
                break;

            case DAILY_0:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, ConstantMediaSize.themePath + "/pag"));
                break;
            case FOOD_0:
                particlesDrawer.addParticle(new PAGNoBgParticle(true, ConstantMediaSize.themePath + "/food0_particle"));
                break;
            case FOOD_4:
                List<Bitmap> bitmaps = GlobalParticles.concurrentLoad(R.mipmap.system10_heart1, R.mipmap.system10_star1, R.mipmap.system10_star2);
                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.HOVER)
                                .setListBitmaps(new ArrayList(bitmaps.subList(0, 1)))
                                .setParticleCount(8)
                                .setPointSize(3, 20)
                                .setSelfRotate(true)
                                .build()
                );

                particlesDrawer.addParticle(
                        new ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                                .setListBitmaps(new ArrayList(bitmaps.subList(1, 3)))
                                .setParticleCount(16)
                                .setShortLife(0.5f)
                                .setParticleSizeDelta(16f)
                                .build()
                );
                break;

            case DAILY_4:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, GlobalParticles.PAG_PARTICLE16.getSavePath()));
                break;
            case DAILY_5:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, GlobalParticles.PAG_PARTICLE13.getSavePath()));
                break;
            case DAILY_6:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, GlobalParticles.PAG_PARTICLE5.getSavePath()));
                break;
            case DAILY_7:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, GlobalParticles.PAG_PARTICLE5.getSavePath()));
                break;
            case DAILY_8:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, GlobalParticles.PAG_PARTICLE21.getSavePath()));
                break;
            case DAILY_9:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, GlobalParticles.PAG_PARTICLE7.getSavePath()));
                break;

            case SPORT_0:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, ConstantMediaSize.themePath + "/effect.pag"));
                break;
            case SPORT_1:
                particlesDrawer.addParticle(new PAGOneBgParticle(true, GlobalParticles.PAG_PARTICLE7.getSavePath()));
                break;


            default:
                break;
        }
        return particlesDrawer;
    }

    /**
     * 文字显示类
     * 暂时统一做法,文字放入片段展示，取消主题文字控制和编辑扩展
     *
     * @return
     */
    public static ThemeTextDoodleManager createTextDoodleManager() {
        ThemeTextDoodleManager themeTextDoodleManager = null;
        switch (ConstantMediaSize.themeType) {
            case WEDDING_ONE:
//                themeTextDoodleManager = new ThemeTextDoodleManager();
                break;
        }
        return themeTextDoodleManager;
    }


    /**
     * 通过反射创建preTreatment
     *
     * @param theme 具体主题
     * @return BasePreTreatment
     */
    public static IPretreatment createPreTreatment(ThemeEnum theme) {
        try {
            return theme.getThemePreTreatmentClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @return
     */
    public static boolean isChangeThemeReset(ThemeEnum themeEnum) {
        if (themeEnum == ThemeEnum.BIRTHDAY_NINETEEN ||
                themeEnum == ThemeEnum.WEDDING_FOUR ||
                themeEnum == ThemeEnum.TRAVEL_NINE) {
            return true;
        }
        return false;
    }

    public static boolean isOpenglSupport() {
        ThemeIRender themeIRender = createThemeManager();
        return !themeIRender.checkSupportTransition();
    }

    public static boolean isTimeTheme(ThemeEnum themeEnum) {
        return BaseTimePreTreatment.class.isAssignableFrom(themeEnum.getThemePreTreatmentClass());
    }
}
