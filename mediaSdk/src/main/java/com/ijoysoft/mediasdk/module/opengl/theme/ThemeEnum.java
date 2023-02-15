package com.ijoysoft.mediasdk.module.opengl.theme;


import com.ijoysoft.mediasdk.common.global.ConstantMediaSize;
import com.ijoysoft.mediasdk.module.entity.RatioType;
import com.ijoysoft.mediasdk.module.opengl.particle.GlobalParticles;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.BasePreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.EmptyPreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.IPretreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.attitude.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.baby.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.beat.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.birthday.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.celebration.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.christmas.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.christmas.Christmas6Pretreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.common.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.food.Food0Pretreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.daily.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.food.Food1PreTreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.food.Food2Pretreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.food.Food3Pretreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.food.Food4Pretreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.food.Food5Pretreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.food.Food6Pretreatment;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.friend.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.greeting.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holi.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.holiday.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.love.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.newyear.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.onam.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.rakshabandhan.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.sport.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.travel.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.valentine.*;
import com.ijoysoft.mediasdk.module.opengl.theme.pretreatment.wedding.*;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude1.Attitude1ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude2.Attitude2ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude3.Attitude3ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude4.Attitude4ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude5.Attitude5ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude6.Attitude6ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude7.Attitude7ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.attitude.attitude8.Attitude8ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby1.BabyOneThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby2.BabyTwoThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby3.BabyThreeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby4.BabyFourThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby5.BabyFiveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby6.BabySixThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.baby.baby7.BabySevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat1.Beat1ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat2.Beat2ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat3.Beat3ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat4.Beat4ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat5.Beat5ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat6.Beat6ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat7.Beat7ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.beat.beat8.Beat8ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday1.BDOneThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday10.BDTenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday11.BDElevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday12.BDTwelveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday13.BDThirteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday14.BDFourteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday15.BDFifteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday16.BDSixteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday17.BDSeventeenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday18.BDEighteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday19.BDNineteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday2.BDTwoThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday20.Birthday20ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday21.Birthday21ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday22.Birthday22ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday23.Birthday23ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday24.Birthday24ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday25.Birthday25ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday26.Birthday26ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday27.Birthday27ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday3.BDThreeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday4.BDFourThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday5.BDFiveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday6.BDSixThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday7.BDSevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday8.BDEightThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.birthday.birthday9.BDNineThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration1.CDOneThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration2.CDTwoThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration3.CDThreeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration4.CDFourThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration5.CDFiveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration6.CDSixThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration7.CDSevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration8.CDEightThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration9.CDNineThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas0.Christmas0ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas1.Christmas1ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas2.Christmas2ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas5.Christmas5ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas6.Christmas6ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas3.Christmas3ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common1.Common1ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common10.Common10ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11.Common11ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common12.Common12ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common13.Common13ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common14.Common14ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common15.Common15ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common16.Common16ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common17.Common17ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common18.Common18ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common19.Common19ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common2.Common2ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common21.Common21ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common22.Common22ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common23.Common23ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common24.Common24ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common25.Common25ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common27.Common27ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common20.Common20ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common28.Common28ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common29.Common29ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common3.Common3ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common30.Common30ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common31.Common31ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common32.Common32ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common33.Common33ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common4.Common4ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common5.Common5ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common6.Common6ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common7.Common7ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common8.Common8ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common9.Common9ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily2.Daily2ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily3.Daily3ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily4.Daily4ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily6.Daily6ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily7.Daily7ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily7.Daily8ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily9.Daily9ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.food.Food0ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily0.Daily0ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily1.Daily1ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.food.Food1ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.food.Food2ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.food.Food3ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.food.Food4ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.food.Food5ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.food.Food6ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend1.Friend1ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend2.Friend2ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend3.Friend3ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.friend.friend4.Friend4ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting1.GreetingOneThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting2.GreetingTwoThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting3.GreetingThreeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting4.GreetingFourThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting5.GreetingFiveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting6.Greeting6ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting7.Greeting7ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting8.Greeting8ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.greeting.greeting9.Greeting9ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi1.HoliOneThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi10.HoliTenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi11.HoliElevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi12.HoliTwelveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi2.HoliTwoThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi3.HoliThreeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi4.HoliFourThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi5.HoliFiveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi6.HoliSixThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi7.HoliSevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi8.HoliEightThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holi.holi9.HoliNineThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday27.HD27ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday1.HDOneThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday10.HDTenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday11.HDElevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday12.HDTwelveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday13.HDThirteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday14.HDFourteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday15.Holiday15ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday15.Holiday18ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday15.Holiday19ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday15.Holiday16ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday17.Holiday17ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday2.HDTwoThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday20.HD20ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday21.HD21ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday22.HD22ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday23.HD23ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday24.HD24ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday25.HD25ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday26.HD26ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday28.HD28ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday29.HD29ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday3.HDThreeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday30.HD30ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday31.HD31ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday32.HD32ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday33.HD33ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday4.HDFourThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday5.HDFiveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday6.HDSixThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday7.HDSevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday8.HDEightThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday9.HDNineThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love1.LoveOneThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love10.LoveTenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love11.LoveElevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love12.LoveTwelveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love13.LoveThirdThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love14.LoveFourteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love15.LoveFifteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love16.LoveSixteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love2.LoveTwoThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love3.LoveThreeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love4.LoveFourThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love5.LoveFiveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love6.LoveSixThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love7.LoveSevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love8.LoveEightThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.love.love9.LoveNineThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.newyear.newyear0.NewYear0ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.newyear.newyear1.NewYear1ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.newyear.newyear2.NewYear2ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.newyear.newyear3.NewYear3ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam1.Onam1ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam2.Onam2ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam3.Onam3ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam4.Onam4ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.onam.onam5.Onam5ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan1.Rakshabandhan1ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan2.Rakshabandhan2ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan3.Rakshabandhan3ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan4.Rakshabandhan4ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan5.Rakshabandhan5ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan6.Rakshabandhan6ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan7.Rakshabandhan7ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan8.Rakshabandhan8ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.rakshabandhan.rakshabandhan9.Rakshabandhan9ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.sport.sport0.Sport0ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.sport.sport1.Sport1ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.sport.sport2.Sport2ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel1.TravelOneThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel10.TravelTenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel11.TravelElevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel12.TravelTwelveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel13.TravelThirteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel14.Travel14ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel2.TravelTwoThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel3.TravelThreeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel4.TravelFourThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel5.TravelFiveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel6.TravelSixThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel7.TravelSevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel8.TravelEightThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.travel.travel9.TravelNineThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine1.VTOneThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine10.VTTenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine11.VTElevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine12.VTTwelveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine13.VTThirteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine14.VTFourteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine15.VTFifteenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine16.Valentine16ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine17.Valentine17ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine18.Valentine18ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine19.Valentine19ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine2.VTTwoThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine3.VTThreeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine4.VTFourThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine5.VTFiveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine6.VTSixThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine7.VTSevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine8.VTEightThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.valentine.valentine9.VTNineThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding1.WeddingOneThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding10.WeddingTenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding11.Wedding11ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding12.Wedding12ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding13.Wedding13ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding14.Wedding14ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding15.Wedding15ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding16.Wedding16ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding17.Wedding17ThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding2.WeddingTwoThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding3.WeddingThreeThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding4.WeddingFourThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding5.WeddingFiveThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding6.WeddingSixThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding7.WeddingSevenThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding8.WeddingEightThemeManager;
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.wedding.wedding9.WeddingNineThemeManager;
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每个主题预留五十个子项
 *
 * @implNote index的值和string（eg：birthday1）中的顺序值对应的，并以此为映射关系
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum ThemeEnum {
    NONE(-1, "", -1, ThemeNoneManager.class, BasePreTreatment.class),
    BIRTHDAY_ONE(0, "birthday1", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_ONE_DURATION, BDOneThemeManager.class, Birthday1PreTreatment.class),
    BIRTHDAY_TWO(1, "birthday2", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_TWO_DURATION, BDTwoThemeManager.class, BDTwoPreTreatment.class),
    BIRTHDAY_THREE(2, "birthday3", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_THREE_DURATION, BDThreeThemeManager.class, BDThreePreTreatment.class),
    BIRTHDAY_FOUR(3, "birthday4", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_ONE_DURATION, BDFourThemeManager.class, BDFourPreTreatment.class),
    BIRTHDAY_FIVE(4, "birthday5", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_TWO_DURATION, BDFiveThemeManager.class, BDFivePreTreatment.class),
    BIRTHDAY_SIX(5, "birthday6", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_THREE_DURATION, BDSixThemeManager.class, BDSixPreTreatment.class),
    BIRTHDAY_SEVEN(6, "birthday7", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_THREE_DURATION, BDSevenThemeManager.class, BDSevenPreTreatment.class),
    BIRTHDAY_EIGHT(7, "birthday8", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_THREE_DURATION, BDEightThemeManager.class, BDEightPreTreatment.class),
    BIRTHDAY_NINE(8, "birthday9", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_THREE_DURATION, BDNineThemeManager.class, BDNinePreTreatment.class),
    BIRTHDAY_TEN(9, "birthday10", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_THREE_DURATION, BDTenThemeManager.class, BDTenPreTreatment.class),
    BIRTHDAY_ELEVEN(10, "birthday11", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_THREE_DURATION, BDElevenThemeManager.class, BDElevenPreTreatment.class),
    BIRTHDAY_TWELVE(11, "birthday12", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_TWO_DURATION, BDTwelveThemeManager.class, BDTwelvePreTreatment.class),
    BIRTHDAY_THIRTEEN(12, "birthday13", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_ONE_DURATION, BDThirteenThemeManager.class, BDThirteenPreTreatment.class),
    BIRTHDAY_FOURTEEN(13, "birthday14", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_TWO_DURATION, BDFourteenThemeManager.class, BDFourteenPreTreatment.class),
    BIRTHDAY_FIFTEEN(14, "birthday15", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_THREE_DURATION, BDFifteenThemeManager.class, BDFifteenPreTreatment.class),
    BIRTHDAY_SIXTEEN(15, "birthday16", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_THREE_DURATION, BDSixteenThemeManager.class, BDSixteenPreTreatment.class),
    BIRTHDAY_SEVENTEEN(16, "birthday17", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_TWO_DURATION, BDSeventeenThemeManager.class, BDSeventeenPreTreatment.class),
    BIRTHDAY_EIGHTEEN(17, "birthday18", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_THREE_DURATION, BDEighteenThemeManager.class, BDEighteenPreTreatment.class),
    BIRTHDAY_NINETEEN(18, "birthday19", ThemeConstant.BIRTHDAY, ThemeConstant.THEME_ONE_DURATION, BDNineteenThemeManager.class, BDNineteenPreTreatment.class),
    BIRTHDAY_TWENTY(19, "birthday20", ThemeConstant.BIRTHDAY, Birthday20ThemeManager.class, Birthday20PreTreatment.class),
    BIRTHDAY_TWENTY_ONE(20, "birthday21", ThemeConstant.BIRTHDAY, Birthday21ThemeManager.class, Birthday21PreTreatment.class),
    BIRTHDAY_TWENTY_TWO(21, "birthday22", ThemeConstant.BIRTHDAY, Birthday22ThemeManager.class, Birthday22PreTreatment.class),
    BIRTHDAY_TWENTY_THREE(22, "birthday23", ThemeConstant.BIRTHDAY, Birthday23ThemeManager.class, Birthday23PreTreatment.class),
    BIRTHDAY_TWENTY_FOUR(23, "birthday24", ThemeConstant.BIRTHDAY, Birthday24ThemeManager.class, Birthday24PreTreatment.class),
    BIRTHDAY_TWENTY_FIVE(24, "birthday25", ThemeConstant.BIRTHDAY, Birthday25ThemeManager.class, Birthday25PreTreatment.class),
    BIRTHDAY_TWENTY_SIX(25, "birthday26", ThemeConstant.BIRTHDAY, Birthday26ThemeManager.class, Birthday26PreTreatment.class),
    BIRTHDAY_TWENTY_SEVEN(26, "birthday27", ThemeConstant.BIRTHDAY, Birthday27ThemeManager.class, Birthday27PreTreatment.class),


    VALENTINE_ONE(0, "valentine1", ThemeConstant.VALENTINE, ThemeConstant.THEME_TWO_DURATION, VTOneThemeManager.class, VTOnePreTreatment.class),
    VALENTINE_TWO(1, "valentine2", ThemeConstant.VALENTINE, ThemeConstant.THEME_TWO_DURATION, VTTwoThemeManager.class, VTTwoPreTreatment.class),
    VALENTINE_THREE(2, "valentine3", ThemeConstant.VALENTINE, ThemeConstant.THEME_THREE_DURATION, VTThreeThemeManager.class, VTThreePreTreatment.class),
    VALENTINE_FOUR(3, "valentine4", ThemeConstant.VALENTINE, ThemeConstant.THEME_THREE_DURATION, VTFourThemeManager.class, VTFourPreTreatment.class),
    VALENTINE_FIVE(4, "valentine5", ThemeConstant.VALENTINE, ThemeConstant.THEME_THREE_DURATION, VTFiveThemeManager.class, VTFivePreTreatment.class),
    VALENTINE_SIX(5, "valentine6", ThemeConstant.VALENTINE, ThemeConstant.THEME_TWO_DURATION, VTSixThemeManager.class, VTSixPreTreatment.class),
    VALENTINE_SEVEN(6, "valentine7", ThemeConstant.VALENTINE, ThemeConstant.THEME_ONE_DURATION, VTSevenThemeManager.class, VTSevenPreTreatment.class),
    VALENTINE_EIGHT(7, "valentine8", ThemeConstant.VALENTINE, ThemeConstant.THEME_TWO_DURATION, VTEightThemeManager.class, VTEightPreTreatment.class),
    VALENTINE_NINE(8, "valentine9", ThemeConstant.VALENTINE, ThemeConstant.THEME_TWO_DURATION, VTNineThemeManager.class, VTNinePreTreatment.class),
    VALENTINE_TEN(9, "valentine10", ThemeConstant.VALENTINE, ThemeConstant.THEME_TWO_DURATION, VTTenThemeManager.class, VTTenPreTreatment.class),
    VALENTINE_ELEVEN(10, "valentine11", ThemeConstant.VALENTINE, ThemeConstant.THEME_TWO_DURATION, VTElevenThemeManager.class, VTElevenPreTreatment.class),
    VALENTINE_TWELVE(11, "valentine12", ThemeConstant.VALENTINE, ThemeConstant.THEME_THREE_DURATION, VTTwelveThemeManager.class, VTTwelvePreTreatment.class),
    VALENTINE_THIRTEEN(12, "valentine13", ThemeConstant.VALENTINE, ThemeConstant.THEME_ONE_DURATION, VTThirteenThemeManager.class, VTThirteenPreTreatment.class),
    VALENTINE_FOURTEEN(13, "valentine14", ThemeConstant.VALENTINE, ThemeConstant.THEME_TWO_DURATION, VTFourteenThemeManager.class, VTFourteenPreTreatment.class),
    VALENTINE_FIFTEEN(14, "valentine15", ThemeConstant.VALENTINE, ThemeConstant.THEME_THREE_DURATION, VTFifteenThemeManager.class, VTFifteenPreTreatment.class),
    VALENTINE16(15, "valentine16", ThemeConstant.VALENTINE, Valentine16ThemeManager.class, Valentine16Pretreatment.class),
    VALENTINE17(16, "valentine17", ThemeConstant.VALENTINE, RatioType._9_16, null, GlobalParticles.PAG_PARTICLE6, Valentine17ThemeManager.class, Valentine17Pretreatment.class),
    VALENTINE18(17, "valentine18", ThemeConstant.VALENTINE, null, GlobalParticles.PAG_PARTICLE6, Valentine18ThemeManager.class, Valentine18Pretreatment.class),
    VALENTINE19(18, "valentine19", ThemeConstant.VALENTINE, Valentine19ThemeManager.class, Valentine19Pretreatment.class),

    WEDDING_ONE(0, "wedding1", ThemeConstant.WEDDING, ThemeConstant.THEME_THREE_DURATION, WeddingOneThemeManager.class, WeddingOnePreTreatment.class),
    WEDDING_TWO(1, "wedding2", ThemeConstant.WEDDING, ThemeConstant.THEME_TWO_DURATION, WeddingTwoThemeManager.class, WeddingTwoPreTreatment.class),
    WEDDING_THREE(2, "wedding3", ThemeConstant.WEDDING, ThemeConstant.THEME_TWO_DURATION, WeddingThreeThemeManager.class, WeddingThreePreTreatment.class),
    WEDDING_FOUR(3, "wedding4", ThemeConstant.WEDDING, ThemeConstant.THEME_ONE_DURATION, WeddingFourThemeManager.class, WeddingFourPreTreatment.class),
    WEDDING_FIVE(4, "wedding5", ThemeConstant.WEDDING, ThemeConstant.THEME_TWO_DURATION, WeddingFiveThemeManager.class, WeddingFivePreTreatment.class),
    WEDDING_SIX(5, "wedding6", ThemeConstant.WEDDING, ThemeConstant.THEME_TWO_DURATION, WeddingSixThemeManager.class, WeddingSixPreTreatment.class),
    WEDDING_SEVEN(6, "wedding7", ThemeConstant.WEDDING, ThemeConstant.THEME_TWO_DURATION, WeddingSevenThemeManager.class, WeddingSevenPreTreatment.class),
    WEDDING_EIGHT(7, "wedding8", ThemeConstant.WEDDING, ThemeConstant.THEME_TWO_DURATION, WeddingEightThemeManager.class, WeddingEightPreTreatment.class),
    WEDDING_NINE(8, "wedding9", ThemeConstant.WEDDING, ThemeConstant.THEME_ONE_DURATION, WeddingNineThemeManager.class, WeddingNinePreTreatment.class),
    WEDDING_TEN(9, "wedding10", ThemeConstant.WEDDING, ThemeConstant.THEME_TWO_DURATION, WeddingTenThemeManager.class, WeddingTenPreTreatment.class),
    WEDDING_ELEVEN(10, "wedding11", ThemeConstant.WEDDING, Wedding11ThemeManager.class, Wedding11PreTreatment.class),
    WEDDING_TWELVE(11, "wedding12", ThemeConstant.WEDDING, Wedding12ThemeManager.class, Wedding12PreTreatment.class),
    WEDDING_THIRTEEN(12, "wedding13", ThemeConstant.WEDDING, Wedding13ThemeManager.class, Wedding13PreTreatment.class),
    WEDDING_FOURTEEN(13, "wedding14", ThemeConstant.WEDDING, Wedding14ThemeManager.class, Wedding14PreTreatment.class),
    WEDDING_FIFTEEN(14, "wedding15", ThemeConstant.WEDDING, Wedding15ThemeManager.class, Wedding15PreTreatment.class),
    WEDDING_SIXTEEN(15, "wedding16", ThemeConstant.WEDDING, Wedding16ThemeManager.class, Wedding16PreTreatment.class),
    WEDDING_SEVENTEEN(16, "wedding17", ThemeConstant.WEDDING, Wedding17ThemeManager.class, Wedding17PreTreatment.class),

    BABY_ONE(0, "baby1", ThemeConstant.BABY, ThemeConstant.THEME_THREE_DURATION, BabyOneThemeManager.class, BabyOnePreTreatment.class),
    BABY_TWO(1, "baby2", ThemeConstant.BABY, ThemeConstant.THEME_TWO_DURATION, BabyTwoThemeManager.class, BabyTwoPreTreatment.class),
    BABY_THREE(2, "baby3", ThemeConstant.BABY, ThemeConstant.THEME_TWO_DURATION, BabyThreeThemeManager.class, BabyThreePreTreatment.class),
    BABY_FOUR(3, "baby4", ThemeConstant.BABY, ThemeConstant.THEME_TWO_DURATION, BabyFourThemeManager.class, BabyFourPreTreatment.class),
    BABY_FIVE(4, "baby5", ThemeConstant.BABY, ThemeConstant.THEME_TWO_DURATION, BabyFiveThemeManager.class, BabyFivePreTreatment.class),
    BABY_SIX(5, "baby6", ThemeConstant.BABY, ThemeConstant.THEME_TWO_DURATION, BabySixThemeManager.class, BabySixPreTreatment.class),
    BABY_SEVEN(6, "baby7", ThemeConstant.BABY, ThemeConstant.THEME_TWO_DURATION, BabySevenThemeManager.class, BabySevenPreTreatment.class),

    GREETING_ONE(0, "greeting1", ThemeConstant.GREETING, ThemeConstant.THEME_TWO_DURATION, GreetingOneThemeManager.class, GreetingOnePreTreatment.class),
    GREETING_TWO(1, "greeting2", ThemeConstant.GREETING, ThemeConstant.THEME_TWO_DURATION, GreetingTwoThemeManager.class, GreetingTwoPreTreatment.class),
    GREETING_THREE(2, "greeting3", ThemeConstant.GREETING, ThemeConstant.THEME_TWO_DURATION, GreetingThreeThemeManager.class, GreetingThreePreTreatment.class),
    GREETING_FOUR(3, "greeting4", ThemeConstant.GREETING, ThemeConstant.THEME_TWO_DURATION, GreetingFourThemeManager.class, GreetingFourPreTreatment.class),
    GREETING_FIVE(4, "greeting5", ThemeConstant.GREETING, ThemeConstant.THEME_THREE_DURATION, GreetingFiveThemeManager.class, GreetingFivePreTreatment.class),
    GREETING_SIX(5, "greeting6", ThemeConstant.GREETING, Greeting6ThemeManager.class, Greeting6PreTreatment.class),
    GREETING_SEVEN(6, "greeting7", ThemeConstant.GREETING, Greeting7ThemeManager.class, Greeting7PreTreatment.class),
    GREETING_EIGHT(7, "greeting8", ThemeConstant.GREETING, Greeting8ThemeManager.class, Greeting8PreTreatment.class),
    GREETING_NINE(8, "greeting9", ThemeConstant.GREETING, Greeting8ThemeManager.class, Greeting8PreTreatment.class),
    GREETING_TEN(9, "greeting10", ThemeConstant.GREETING, Greeting9ThemeManager.class, Greeting9PreTreatment.class),
    //感恩节
    HOLIDAY_TWENTYTHREE(22, "holiday23", ThemeConstant.GREETING, HD23ThemeManager.class, HD23PreTreatment.class),
    HOLIDAY_TWENTYFOUR(23, "holiday24", ThemeConstant.GREETING, HD24ThemeManager.class, HD24PreTreatment.class),
    HOLIDAY_TWENTYFIVE(24, "holiday25", ThemeConstant.GREETING, HD25ThemeManager.class, HD25PreTreatment.class),
    HOLIDAY_TWENTYSIX(25, "holiday26", ThemeConstant.GREETING, HD26ThemeManager.class, HD26Pretreatment.class),
    HOLIDAY_TWENTYSEVEN(26, "holiday27", ThemeConstant.GREETING, HD27ThemeManager.class, HD27PreTreatment.class),
    HOLIDAY_TWENTYEIGHT(27, "holiday28", ThemeConstant.GREETING, HD28ThemeManager.class, HD28PreTreatment.class),
    HOLIDAY_TWENTYNINE(28, "holiday29", ThemeConstant.GREETING, HD29ThemeManager.class, HD29PreTreatment.class),
    HOLIDAY_THIRTY(29, "holiday30", ThemeConstant.GREETING, HD30ThemeManager.class, HD30PreTreatment.class),
    HOLIDAY_THIRTYONE(30, "holiday31", ThemeConstant.GREETING, HD31ThemeManager.class, HD31PreTreatment.class),
    HOLIDAY_THIRTYOTWO(31, "holiday32", ThemeConstant.GREETING, HD32ThemeManager.class, HD32PreTreatment.class),
    HOLIDAY_THIRTYTHREE(32, "holiday33", ThemeConstant.GREETING, ThemeConstant.THEME_TWO_DURATION, HD33ThemeManager.class, HD33PreTreatment.class),

    HOLIDAY_ONE(0, "holiday1", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, HDOneThemeManager.class, HDOnePreTreatment.class),
    HOLIDAY_TWO(1, "holiday2", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, HDTwoThemeManager.class, HDTwoPreTreatment.class),
    HOLIDAY_THREE(2, "holiday3", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, HDThreeThemeManager.class, HDThreePreTreatment.class),
    HOLIDAY_FOUR(3, "holiday4", ThemeConstant.HOLIDAY, ThemeConstant.THEME_ONE_DURATION, HDFourThemeManager.class, HDFourPreTreatment.class),
    HOLIDAY_FIVE(4, "holiday5", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, HDFiveThemeManager.class, HDFivePreTreatment.class),
    HOLIDAY_SIX(5, "holiday6", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, HDSixThemeManager.class, HDSixPreTreatment.class),
    HOLIDAY_SEVEN(6, "holiday7", ThemeConstant.HOLIDAY, ThemeConstant.THEME_ONE_DURATION, HDSevenThemeManager.class, HDSevenPreTreatment.class),
    HOLIDAY_EIGHT(7, "holiday8", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, HDEightThemeManager.class, HDEightPreTreatment.class),
    HOLIDAY_NINE(8, "holiday9", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, HDNineThemeManager.class, HDNinePreTreatment.class),
    HOLIDAY_TEN(9, "holiday10", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, HDTenThemeManager.class, HDTenPreTreatment.class),
    HOLIDAY_ELEVEN(10, "holiday11", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, HDElevenThemeManager.class, HDElevenPreTreatment.class),
    HOLIDAY_TWELVE(11, "holiday12", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, HDTwelveThemeManager.class, HDTwelvePreTreatment.class),
    HOLIDAY_THIRTEEN(12, "holiday13", ThemeConstant.HOLIDAY, ThemeConstant.THEME_ONE_DURATION, HDThirteenThemeManager.class, HDThirteenPreTreatment.class),
    HOLIDAY_FOURTEEN(13, "holiday14", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, HDFourteenThemeManager.class, HDFourteenPreTreatment.class),
    //万圣节
    HOLIDAY_FIFTEEN(14, "holiday15", ThemeConstant.HOLIDAY, Holiday15ThemeManager.class, Holiday15PreTreatment.class),
    HOLIDAY_SIXTEEN(15, "holiday16", ThemeConstant.HOLIDAY, Holiday16ThemeManager.class, Holiday16PreTreatment.class),
    HOLIDAY_SEVENTEEN(16, "holiday17", ThemeConstant.HOLIDAY, ThemeConstant.THEME_ONE_DURATION, Holiday17ThemeManager.class, Holiday17PreTreatment.class),
    HOLIDAY_EIGHTEEN(17, "holiday18", ThemeConstant.HOLIDAY, Holiday18ThemeManager.class, Holiday18PreTreatment.class),
    HOLIDAY_NINETEEN(18, "holiday19", ThemeConstant.HOLIDAY, Holiday19ThemeManager.class, Holiday19PreTreatment.class),
    HOLIDAY_TWENTY(19, "holiday20", ThemeConstant.HOLIDAY, HD20ThemeManager.class, HD20PreTreatment.class),
    HOLIDAY_TWENTYONE(20, "holiday21", ThemeConstant.HOLIDAY, HD21ThemeManager.class, HD21PreTreatment.class),
    HOLIDAY_TWENTYTWO(21, "holiday22", ThemeConstant.HOLIDAY, HD22ThemeManager.class, HD22PreTreatment.class),

    CELEBRATION_ONE(0, "celebration1", ThemeConstant.CELEBRATION, ThemeConstant.THEME_TWO_DURATION, CDOneThemeManager.class, CDOnePreTreatment.class),
    CELEBRATION_TWO(1, "celebration2", ThemeConstant.CELEBRATION, ThemeConstant.THEME_TWO_DURATION, CDTwoThemeManager.class, CDTwoPreTreatment.class),
    CELEBRATION_THREE(2, "celebration3", ThemeConstant.CELEBRATION, ThemeConstant.THEME_ONE_DURATION, CDThreeThemeManager.class, CDThreePreTreatment.class),
    CELEBRATION_FOUR(3, "celebration4", ThemeConstant.CELEBRATION, ThemeConstant.THEME_TWO_DURATION, CDFourThemeManager.class, CDFourPreTreatment.class),
    CELEBRATION_FIVE(4, "celebration5", ThemeConstant.CELEBRATION, ThemeConstant.THEME_ONE_DURATION, CDFiveThemeManager.class, CDFivePreTreatment.class),
    CELEBRATION_SIX(5, "celebration6", ThemeConstant.CELEBRATION, ThemeConstant.THEME_ONE_DURATION, CDSixThemeManager.class, CDSixPreTreatment.class),
    CELEBRATION_SEVEN(6, "celebration7", ThemeConstant.CELEBRATION, ThemeConstant.THEME_ONE_DURATION, CDSevenThemeManager.class, CDSevenPreTreatment.class),
    CELEBRATION_EIGHT(7, "celebration8", ThemeConstant.CELEBRATION, ThemeConstant.THEME_TWO_DURATION, CDEightThemeManager.class, CDEightPreTreatment.class),
    CELEBRATION_NINE(8, "celebration9", ThemeConstant.CELEBRATION, ThemeConstant.THEME_TWO_DURATION, CDNineThemeManager.class, CDNinePreTreatment.class),

    LOVE_ONE(1, "love1", ThemeConstant.LOVE, ThemeConstant.THEME_TWO_DURATION, LoveOneThemeManager.class, LoveOnePreTreatment.class),
    LOVE_TWO(2, "love2", ThemeConstant.LOVE, ThemeConstant.THEME_THREE_DURATION, LoveTwoThemeManager.class, LoveTwoPreTreatment.class),
    LOVE_THREE(3, "love3", ThemeConstant.LOVE, ThemeConstant.THEME_THREE_DURATION, LoveThreeThemeManager.class, LoveThreePreTreatment.class),
    LOVE_FOUR(4, "love4", ThemeConstant.LOVE, ThemeConstant.THEME_TWO_DURATION, LoveFourThemeManager.class, LoveFourPreTreatment.class),
    LOVE_FIVE(5, "love5", ThemeConstant.LOVE, ThemeConstant.THEME_TWO_DURATION, LoveFiveThemeManager.class, LoveFivePreTreatment.class),
    LOVE_SIX(6, "love6", ThemeConstant.LOVE, ThemeConstant.THEME_TWO_DURATION, LoveSixThemeManager.class, LoveSixPreTreatment.class),
    LOVE_SEVEN(7, "love7", ThemeConstant.LOVE, ThemeConstant.THEME_TWO_DURATION, LoveSevenThemeManager.class, LoveSevenPreTreatment.class),
    LOVE_EIGHT(8, "love8", ThemeConstant.LOVE, ThemeConstant.THEME_TWO_DURATION, LoveEightThemeManager.class, LoveEightPreTreatment.class),
    LOVE_NINE(9, "love9", ThemeConstant.LOVE, LoveNineThemeManager.class, LoveNinePreTreatment.class),
    LOVE_TEN(10, "love10", ThemeConstant.LOVE, LoveTenThemeManager.class, LoveTenPreTreatment.class),
    LOVE_ELEVEN(11, "love11", ThemeConstant.LOVE, LoveElevenThemeManager.class, LoveElevenPreTreatment.class),
    LOVE_TWELVE(12, "love12", ThemeConstant.LOVE, LoveTwelveThemeManager.class, LoveTwelvePreTreatment.class),
    LOVE_THIRDTEEN(13, "love13", ThemeConstant.LOVE, LoveThirdThemeManager.class, LoveThirdPreTreatment.class),
    LOVE_FOURTEEN(14, "love14", ThemeConstant.LOVE, LoveFourteenThemeManager.class, LoveFourteenPreTreatment.class),
    LOVE_FIFTEEN(15, "love15", ThemeConstant.LOVE, LoveFifteenThemeManager.class, LoveFifteenPreTreatment.class),
    LOVE_SIXTEEN(16, "love16", ThemeConstant.LOVE, LoveSixteenThemeManager.class, LoveSixteenPreTreatment.class),

    TRAVEL_ONE(0, "travel1", ThemeConstant.TRAVEL, ThemeConstant.THEME_THREE_DURATION, TravelOneThemeManager.class, TravelOnePreTreatment.class),
    TRAVEL_TWO(1, "travel2", ThemeConstant.TRAVEL, ThemeConstant.THEME_ONE_DURATION, TravelTwoThemeManager.class, TravelTwoPreTreatment.class),
    TRAVEL_THREE(2, "travel3", ThemeConstant.TRAVEL, ThemeConstant.THEME_TWO_DURATION, TravelThreeThemeManager.class, TravelThreePreTreatment.class),
    TRAVEL_FOUR(3, "travel4", ThemeConstant.TRAVEL, ThemeConstant.THEME_TWO_DURATION, TravelFourThemeManager.class, TravelFourPreTreatment.class),
    TRAVEL_FIVE(4, "travel5", ThemeConstant.TRAVEL, ThemeConstant.THEME_TWO_DURATION, TravelFiveThemeManager.class, TravelFivePreTreatment.class),
    TRAVEL_SIX(5, "travel6", ThemeConstant.TRAVEL, ThemeConstant.THEME_THREE_DURATION, TravelSixThemeManager.class, TravelSixPreTreatment.class),
    TRAVEL_SEVEN(6, "travel7", ThemeConstant.TRAVEL, ThemeConstant.THEME_ONE_DURATION, TravelSevenThemeManager.class, TravelSevenPreTreatment.class),
    TRAVEL_EIGHT(7, "travel8", ThemeConstant.TRAVEL, ThemeConstant.THEME_THREE_DURATION, TravelEightThemeManager.class, TravelEightPreTreatment.class),
    TRAVEL_NINE(8, "travel9", ThemeConstant.TRAVEL, ThemeConstant.THEME_ONE_DURATION, TravelNineThemeManager.class, TravelNinePreTreatment.class),
    TRAVEL_TEN(9, "travel10", ThemeConstant.TRAVEL, ThemeConstant.THEME_TWO_DURATION, TravelTenThemeManager.class, TravelTenPreTreatment.class),
    TRAVEL_ELEVEN(10, "travel11", ThemeConstant.TRAVEL, ThemeConstant.THEME_TWO_DURATION, TravelElevenThemeManager.class, TravelElevenPreTreatment.class),
    TRAVEL_TWELVE(11, "travel12", ThemeConstant.TRAVEL, ThemeConstant.THEME_TWO_DURATION, TravelTwelveThemeManager.class, TravelTwelvePreTreatment.class),
    TRAVEL_THIRTEEN(12, "travel13", ThemeConstant.TRAVEL, ThemeConstant.THEME_TWO_DURATION, TravelThirteenThemeManager.class, TravelThirteenPreTreatment.class),
    TRAVEL_FOURTEEN(13, "travel14", ThemeConstant.TRAVEL, RatioType._3_4, Travel14ThemeManager.class, Travel14Pretreatment.class),

    /*洒红节日*/
    HOLI_ONE(0, "holi1", ThemeConstant.HOLI, ThemeConstant.THEME_TWO_DURATION, HoliOneThemeManager.class, HoliOnePreTreatment.class),
    HOLI_TWO(1, "holi2", ThemeConstant.HOLI, ThemeConstant.THEME_TWO_DURATION, HoliTwoThemeManager.class, HoliTwoPreTreatment.class),
    HOLI_THREE(2, "holi3", ThemeConstant.HOLI, ThemeConstant.THEME_TWO_DURATION, HoliThreeThemeManager.class, HoliThreePreTreatment.class),
    HOLI_FOUR(3, "holi4", ThemeConstant.HOLI, ThemeConstant.THEME_TWO_DURATION, HoliFourThemeManager.class, HoliFourPreTreatment.class),
    HOLI_FIVE(4, "holi5", ThemeConstant.HOLI, ThemeConstant.THEME_TWO_DURATION, HoliFiveThemeManager.class, HoliFivePreTreatment.class),
    HOLI_SIX(5, "holi6", ThemeConstant.HOLI, ThemeConstant.THEME_TWO_DURATION, HoliSixThemeManager.class, HoliSixPreTreatment.class),
    HOLI_SEVEN(6, "holi7", ThemeConstant.HOLI, HoliSevenThemeManager.class, HoliSevenPreTreatment.class),
    HOLI_EIGHT(7, "holi8", ThemeConstant.HOLI, HoliEightThemeManager.class, HoliEightPreTreatment.class),
    HOLI_NINE(8, "holi9", ThemeConstant.HOLI, HoliNineThemeManager.class, HoliNinePreTreatment.class),
    HOLI_TEN(9, "holi10", ThemeConstant.HOLI, HoliTenThemeManager.class, HoliTenPreTreatment.class),
    HOLI_ELEVEN(10, "holi11", ThemeConstant.HOLI, HoliElevenThemeManager.class, HoliElevenPreTreatment.class),
    HOLI_TWELVE(11, "holi12", ThemeConstant.HOLI, HoliTwelveThemeManager.class, HoliTwelvePreTreatment.class),

    /**
     * 兄妹节
     */
    RAKSHABANDHAN_ONE(0, "rakshabandhan1", ThemeConstant.RAKSHABANDHAN, Rakshabandhan1ThemeManager.class, Rakshabandhan1PreTreatment.class),
    RAKSHABANDHAN_TWO(1, "rakshabandhan2", ThemeConstant.RAKSHABANDHAN, Rakshabandhan2ThemeManager.class, Rakshabandhan2PreTreatment.class),
    RAKSHABANDHAN_THREE(2, "rakshabandhan3", ThemeConstant.RAKSHABANDHAN, Rakshabandhan3ThemeManager.class, Rakshabandhan3PreTreatment.class),
    RAKSHABANDHAN_FOUR(3, "rakshabandhan4", ThemeConstant.RAKSHABANDHAN, Rakshabandhan4ThemeManager.class, Rakshabandhan4PreTreatment.class),
    RAKSHABANDHAN_FIVE(4, "rakshabandhan5", ThemeConstant.RAKSHABANDHAN, Rakshabandhan5ThemeManager.class, Rakshabandhan5PreTreatment.class),
    RAKSHABANDHAN_SIX(5, "rakshabandhan6", ThemeConstant.RAKSHABANDHAN, Rakshabandhan6ThemeManager.class, Rakshabandhan6PreTreatment.class),
    RAKSHABANDHAN_SEVEN(6, "rakshabandhan7", ThemeConstant.RAKSHABANDHAN, Rakshabandhan7ThemeManager.class, Rakshabandhan7PreTreatment.class),
    RAKSHABANDHAN_EIGHT(7, "rakshabandhan8", ThemeConstant.RAKSHABANDHAN, Rakshabandhan8ThemeManager.class, Rakshabandhan8PreTreatment.class),
    RAKSHABANDHAN_NINE(8, "rakshabandhan9", ThemeConstant.RAKSHABANDHAN, Rakshabandhan9ThemeManager.class, Rakshabandhan9PreTreatment.class),

    /**
     * 欧南节
     */
    ONAM_ONE(0, "onam1", ThemeConstant.ONAM, Onam1ThemeManager.class, Onam1PreTreatment.class),
    ONAM_TWO(1, "onam2", ThemeConstant.ONAM, Onam2ThemeManager.class, Onam2PreTreatment.class),
    ONAM_THREE(2, "onam3", ThemeConstant.ONAM, Onam3ThemeManager.class, Onam3PreTreatment.class),
    ONAM_FOUR(3, "onam4", ThemeConstant.ONAM, Onam4ThemeManager.class, Onam4PreTreatment.class),
    ONAM_FIVE(4, "onam5", ThemeConstant.ONAM, Onam5ThemeManager.class, Onam5PreTreatment.class),
//    ONAM_SIX(5, "onam6", ThemeConstant.ONAM, Onam6ThemeManager.class, Onam6PreTreatment.class),
//    ONAM_SEVEN(6, "onam7", ThemeConstant.ONAM, Onam7ThemeManager.class, Onam7PreTreatment.class),
//    ONAM_EIGHT(7, "onam8", ThemeConstant.ONAM, Onam8ThemeManager.class, Onam8PreTreatment.class),
//    ONAM_NINE(8, "onam9", ThemeConstant.ONAM, Onam9ThemeManager.class, Onam9PreTreatment.class),
//    ONAM_TEN(9, "onam10", ThemeConstant.ONAM, Onam10ThemeManager.class, Onam10PreTreatment.class),
//    ONAM_ELEVEN(10, "onam11", ThemeConstant.ONAM, Onam11ThemeManager.class, Onam11PreTreatment.class),
//    ONAM_TWELVE(11, "onam12", ThemeConstant.ONAM, Onam12ThemeManager.class, Onam12PreTreatment.class),


    /**
     * beats
     */
    BEAT_ONE(0, "beat1", ThemeConstant.BEAT, Beat1ThemeManager.class, Beat1PreTreatment.class),
    BEAT_TWO(1, "beat2", ThemeConstant.BEAT, Beat2ThemeManager.class, Beat2PreTreatment.class),
    BEAT_THREE(2, "beat3", ThemeConstant.BEAT, Beat3ThemeManager.class, Beat3PreTreatment.class),
    BEAT_FOUR(3, "beat4", ThemeConstant.BEAT, Beat4ThemeManager.class, Beat4PreTreatment.class),
    BEAT_FIVE(4, "beat5", ThemeConstant.BEAT, Beat5ThemeManager.class, Beat5PreTreatment.class),
    BEAT_SIX(5, "beat6", ThemeConstant.BEAT, Beat6ThemeManager.class, Beat6PreTreatment.class),
    BEAT_SEVEN(6, "beat7", ThemeConstant.BEAT, Beat7ThemeManager.class, Beat7PreTreatment.class),
    BEAT_EIGHT(7, "beat8", ThemeConstant.BEAT, Beat8ThemeManager.class, Beat8PreTreatment.class),


    /**
     * 态度
     */
    ATTITUDE_ONE(0, "attitude1", ThemeConstant.ATTITUDE, Attitude1ThemeManager.class, Attitude1PreTreatment.class),
    ATTITUDE_TWO(1, "attitude2", ThemeConstant.ATTITUDE, Attitude2ThemeManager.class, Attitude2PreTreatment.class),
    ATTITUDE_THREE(2, "attitude3", ThemeConstant.ATTITUDE, Attitude3ThemeManager.class, Attitude3PreTreatment.class),
    ATTITUDE_FOUR(3, "attitude4", ThemeConstant.ATTITUDE, Attitude4ThemeManager.class, Attitude4PreTreatment.class),
    ATTITUDE_FIVE(4, "attitude5", ThemeConstant.ATTITUDE, Attitude5ThemeManager.class, Attitude5PreTreatment.class),
    ATTITUDE_SIX(5, "attitude6", ThemeConstant.ATTITUDE, Attitude6ThemeManager.class, Attitude6PreTreatment.class),
    ATTITUDE_SEVEN(6, "attitude7", ThemeConstant.ATTITUDE, Attitude7ThemeManager.class, Attitude7PreTreatment.class),
    ATTITUDE_EIGHT(7, "attitude8", ThemeConstant.ATTITUDE, Attitude8ThemeManager.class, Attitude8PreTreatment.class),


    /**
     * 朋友
     */
    FRIEND_ONE(0, "friend1", ThemeConstant.FRIEND, Friend1ThemeManager.class, Friend1PreTreatment.class),
    FRIEND_TWO(1, "friend2", ThemeConstant.FRIEND, Friend2ThemeManager.class, Friend2PreTreatment.class),
    FRIEND_THREE(2, "friend3", ThemeConstant.FRIEND, Friend3ThemeManager.class, Friend3PreTreatment.class),
    FRIEND_FOUR(3, "friend4", ThemeConstant.FRIEND, Friend4ThemeManager.class, Friend4PreTreatment.class),


    /**
     * 通用
     */
    COMMON_ONE(1, "common1", ThemeConstant.COMMON, Common1ThemeManager.class, Common1PreTreatment.class),
    COMMON_TWO(2, "common2", ThemeConstant.COMMON, Common2ThemeManager.class, Common2PreTreatment.class),
    COMMON_THREE(3, "common3", ThemeConstant.COMMON, Common3ThemeManager.class, Common3PreTreatment.class),
    COMMON_FOUR(4, "common4", ThemeConstant.COMMON, Common4ThemeManager.class, Common4PreTreatment.class),
    COMMON_FIVE(5, "common5", ThemeConstant.COMMON, Common5ThemeManager.class, Common5PreTreatment.class),
    COMMON_SIX(6, "common6", ThemeConstant.COMMON, Common6ThemeManager.class, Common6PreTreatment.class),
    COMMON_SEVEN(7, "common7", ThemeConstant.COMMON, Common7ThemeManager.class, Common7PreTreatment.class),
    COMMON_EIGHT(8, "common8", ThemeConstant.COMMON, Common8ThemeManager.class, Common8PreTreatment.class),
    COMMON_NINE(9, "common9", ThemeConstant.COMMON, Common9ThemeManager.class, Common9PreTreatment.class),
    COMMON_TEN(10, "common10", ThemeConstant.COMMON, Common10ThemeManager.class, Common10PreTreatment.class),
    COMMON_ELEVEN(11, "common11", ThemeConstant.COMMON, Common11ThemeManager.class, Common11PreTreatment.class),
    COMMON_TWELVE(12, "common12", ThemeConstant.COMMON, Common12ThemeManager.class, Common12PreTreatment.class),
    COMMON_THIRDTEEN(13, "common13", ThemeConstant.COMMON, Common13ThemeManager.class, Common13PreTreatment.class),
    COMMON_FOURTEEN(14, "common14", ThemeConstant.COMMON, Common14ThemeManager.class, Common14PreTreatment.class),
    COMMON_FIFTEEN(15, "common15", ThemeConstant.COMMON, Common15ThemeManager.class, Common15PreTreatment.class),
    COMMON_SIXTEEN(16, "common16", ThemeConstant.COMMON, Common16ThemeManager.class, Common16PreTreatment.class),
    COMMON_SEVENTEEN(17, "common17", ThemeConstant.COMMON, Common17ThemeManager.class, Common17PreTreatment.class),
    COMMON_EIGHTEEN(18, "common18", ThemeConstant.COMMON, Common18ThemeManager.class, Common18PreTreatment.class),
    COMMON_NINETEEN(19, "common19", ThemeConstant.COMMON, Common19ThemeManager.class, Common19PreTreatment.class),
    COMMON_TWENTY(20, "common20", ThemeConstant.COMMON, Common20ThemeManager.class, Common20PreTreatment.class),
    COMMON_TWENTYONE(21, "common21", ThemeConstant.COMMON, Common21ThemeManager.class, Common21PreTreatment.class),
    COMMON_TWENTYTWO(22, "common22", ThemeConstant.COMMON, Common22ThemeManager.class, Common22PreTreatment.class),
    COMMON_TWENTYTHREE(23, "common23", ThemeConstant.COMMON, Common23ThemeManager.class, Common23PreTreatment.class),
    COMMON_TWENTYFOUR(24, "common24", ThemeConstant.COMMON, Common24ThemeManager.class, Common24PreTreatment.class),
    COMMON_TWENTYFIVE(25, "common25", ThemeConstant.COMMON, Common25ThemeManager.class, Common25PreTreatment.class),
    //COMMON_TWENTY(26, "common26", ThemeConstant.COMMON, Common26ThemeManager.class, Common26PreTreatment.class),

    COMMON_TWENTYSEVEN(27, "common27", ThemeConstant.COMMON, Common27ThemeManager.class, Common27PreTreatment.class),
    COMMON_TWENTYEIGHT(28, "common28", ThemeConstant.COMMON, Common28ThemeManager.class, Common28PreTreatment.class),
    COMMON_TWENTYNINE(29, "common29", ThemeConstant.COMMON, Common29ThemeManager.class, Common29PreTreatment.class),
    COMMON_THIRDTY(30, "common30", ThemeConstant.COMMON, Common30ThemeManager.class, Common30PreTreatment.class),
    COMMON_THIRDTYONE(31, "common31", ThemeConstant.COMMON, Common31ThemeManager.class, Common31PreTreatment.class),
    COMMON_THIRDTYTWO(32, "common32", ThemeConstant.COMMON, Common32ThemeManager.class, Common32PreTreatment.class),
    COMMON_THIRDTYTHREE(33, "common33", ThemeConstant.COMMON, Common33ThemeManager.class, Common33PreTreatment.class),


    CHRISTMAS_0(0, "christmas0", ThemeConstant.HOLIDAY, ThemeConstant.THEME_ONE_DURATION, Christmas0ThemeManager.class, Christmas0Pretreatment.class),
    CHRISTMAS_1(1, "christmas1", ThemeConstant.HOLIDAY, Christmas1ThemeManager.class, Christmas1Pretreatment.class),
    CHRISTMAS_2(2, "christmas2", ThemeConstant.HOLIDAY, Christmas2ThemeManager.class, Christmas2Pretreatment.class),
    CHRISTMAS_3(3, "christmas3", ThemeConstant.HOLIDAY, 3600, Christmas3ThemeManager.class, BasePreTreatment.class),
    CHRISTMAS_4(4, "christmas4", ThemeConstant.HOLIDAY, BaseThemeManager.class, BasePreTreatment.class),
    CHRISTMAS_5(5, "christmas5", ThemeConstant.HOLIDAY, Christmas5ThemeManager.class, Christmas5Pretreatment.class),
    CHRISTMAS_6(6, "christmas6", ThemeConstant.HOLIDAY, ThemeConstant.THEME_ONE_DURATION, Christmas6ThemeManager.class, Christmas6Pretreatment.class),
    CHRISTMAS_7(7, "christmas7", ThemeConstant.HOLIDAY, BaseThemeManager.class, BasePreTreatment.class),
    CHRISTMAS_8(8, "christmas8", ThemeConstant.HOLIDAY, BaseThemeManager.class, BasePreTreatment.class),
    CHRISTMAS_9(9, "christmas9", ThemeConstant.HOLIDAY, BaseThemeManager.class, BasePreTreatment.class),
    CHRISTMAS_10(10, "christmas10", ThemeConstant.HOLIDAY, BaseThemeManager.class, BasePreTreatment.class),


    NEWYEAR_0(0, "newyear0", ThemeConstant.HOLIDAY, NewYear0ThemeManager.class, NewYear0Pretreatment.class),
    NEWYEAR_1(1, "newyear1", ThemeConstant.HOLIDAY, NewYear1ThemeManager.class, NewYear1Pretreatment.class),
    NEWYEAR_2(2, "newyear2", ThemeConstant.HOLIDAY, ThemeConstant.THEME_TWO_DURATION, NewYear2ThemeManager.class, NewYear2Pretreatment.class),
    NEWYEAR_3(3, "newyear3", ThemeConstant.HOLIDAY, NewYear3ThemeManager.class, NewYear3Pretreatment.class),

    /*日常*/
    DAILY_0(0, "daily0", ThemeConstant.DAILY, Daily0ThemeManager.class, Daily0Pretreatment.class),
    DAILY_1(1, "daily1", ThemeConstant.DAILY, ThemeConstant.THEME_TWO_DURATION, 0, Daily1ThemeManager.class, Daily1Pretreatment.class),
    DAILY_2(2, "daily2", ThemeConstant.DAILY, TransitionType.PAG_BOOM5, GlobalParticles.PAG_PARTICLE13, Daily2ThemeManager.class, Daily2Pretreatment.class),
    DAILY_3(3, "daily3", ThemeConstant.DAILY, Daily3ThemeManager.class, Daily3Pretreatment.class),
    DAILY_4(4, "daily4", ThemeConstant.DAILY, null, GlobalParticles.PAG_PARTICLE16, Daily4ThemeManager.class, EmptyPreTreatment.class),
    DAILY_5(5, "daily5", ThemeConstant.DAILY, null, GlobalParticles.PAG_PARTICLE13, Daily4ThemeManager.class, EmptyPreTreatment.class),
    DAILY_6(6, "daily6", ThemeConstant.DAILY, RatioType._9_16, null, GlobalParticles.PAG_PARTICLE5, Daily6ThemeManager.class, Daily6Pretreatment.class),
    DAILY_7(7, "daily7", ThemeConstant.DAILY, RatioType._9_16, null, GlobalParticles.PAG_PARTICLE5, Daily7ThemeManager.class, Daily7Pretreatment.class),
    DAILY_8(8, "daily8", ThemeConstant.DAILY, RatioType._9_16, null, GlobalParticles.PAG_PARTICLE21, Daily8ThemeManager.class, Daily8Pretreatment.class),
    DAILY_9(9, "daily9", ThemeConstant.DAILY, null, GlobalParticles.PAG_PARTICLE7, Daily9ThemeManager.class, Daily9Pretreatment.class),


    /*美食*/
    FOOD_0(0, "food0", ThemeConstant.FOOD, Food0ThemeManager.class, Food0Pretreatment.class),
    FOOD_1(1, "food1", ThemeConstant.FOOD, Food1ThemeManager.class, Food1PreTreatment.class),
    FOOD_2(2, "food2", ThemeConstant.FOOD, Food2ThemeManager.class, Food2Pretreatment.class),
    FOOD_3(3, "food3", ThemeConstant.FOOD, null, GlobalParticles.PAG_PARTICLE9, Food3ThemeManager.class, Food3Pretreatment.class),
    FOOD_4(4, "food4", ThemeConstant.FOOD, Food4ThemeManager.class, Food4Pretreatment.class),
    FOOD_5(5, "food5", ThemeConstant.FOOD, TransitionType.PAG_BOOM1, GlobalParticles.PAG_PARTICLE9, Food5ThemeManager.class, Food5Pretreatment.class),
    FOOD_6(6, "food6", ThemeConstant.FOOD, null,GlobalParticles.PAG_PARTICLE10, Food6ThemeManager.class, Food6Pretreatment.class),

    SPORT_0(0, "sport0", ThemeConstant.SPORT, ThemeConstant.THEME_TWO_DURATION, Sport0ThemeManager.class, Sport0Pretreatment.class),

    SPORT_1(1, "sport1", ThemeConstant.SPORT, ThemeConstant.THEME_TWO_DURATION, null, GlobalParticles.PAG_PARTICLE7, Sport1ThemeManager.class, Sport1Pretreatment.class),


    SPORT_2(2, "sport2", ThemeConstant.SPORT, ThemeConstant.THEME_TWO_DURATION, null, GlobalParticles.PAG_PARTICLE7, Sport2ThemeManager.class, Sport2Pretreatment.class),

    //枚举结束分号：
    ;


    int index;
    //    String alias;
    String name;
    //    int resId;
    @ThemeConstant.ThemeType
    int type;

    //主题的时间类型(默认一个进场悬停出长时间)
    int themeTimeType;

    String className;

    Class<? extends ThemeIRender> themeManagerClass;

    Class<? extends IPretreatment> themePreTreatmentClass;

    //固定显示比例
    RatioType mRatioType;

    /**
     * 主题用到的转场类型，如果是线上的那么下载要同步下载
     */
    TransitionType mTransitionType;
    /**
     * 主题用到的粒子类型，如果用到
     */
    GlobalParticles mParticleType;


    int endOffset;

    /**
     * 默认主题类型
     *
     * @param index
     * @param name
     * @param type
     * @param themeManagerClass
     * @param themePreTreatmentClass
     */
    ThemeEnum(int index, String name, int type, Class<? extends ThemeIRender> themeManagerClass, Class<? extends IPretreatment> themePreTreatmentClass) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.themeManagerClass = themeManagerClass;
        if (themePreTreatmentClass == null) {
            this.themePreTreatmentClass = EmptyPreTreatment.class;
        } else {
            this.themePreTreatmentClass = themePreTreatmentClass;
        }
        this.themeTimeType = ThemeConstant.THEME_ONE_DURATION;
        if (BaseTimeThemeManager.class.isAssignableFrom(themeManagerClass)) {
            endOffset = 0;
        } else {
            endOffset = ConstantMediaSize.END_OFFSET;
        }
    }

    /**
     * 针对固定比例主题
     *
     * @param index
     * @param name
     * @param type
     * @param ratioType
     * @param themeManagerClass
     * @param themePreTreatmentClass
     */
    ThemeEnum(int index, String name, int type, RatioType ratioType, Class<? extends ThemeIRender> themeManagerClass, Class<? extends IPretreatment> themePreTreatmentClass) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.mRatioType = ratioType;
        this.themeManagerClass = themeManagerClass;
        if (themePreTreatmentClass == null) {
            this.themePreTreatmentClass = EmptyPreTreatment.class;
        } else {
            this.themePreTreatmentClass = themePreTreatmentClass;
        }
        if (BaseTimeThemeManager.class.isAssignableFrom(themeManagerClass)) {
            endOffset = 0;
        } else {
            endOffset = ConstantMediaSize.END_OFFSET;
        }
    }

    /**
     * 针对固定比例主题
     *
     * @param index
     * @param name
     * @param type
     * @param ratioType
     * @param themeManagerClass
     * @param themePreTreatmentClass
     */
    ThemeEnum(int index, String name, int type, RatioType ratioType, TransitionType transitionType, GlobalParticles particleType
            , Class<? extends ThemeIRender> themeManagerClass, Class<? extends IPretreatment> themePreTreatmentClass) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.mRatioType = ratioType;
        this.themeManagerClass = themeManagerClass;
        this.mTransitionType = transitionType;
        this.mParticleType = particleType;
        if (themePreTreatmentClass == null) {
            this.themePreTreatmentClass = EmptyPreTreatment.class;
        } else {
            this.themePreTreatmentClass = themePreTreatmentClass;
        }
        if (BaseTimeThemeManager.class.isAssignableFrom(themeManagerClass)) {
            endOffset = 0;
        } else {
            endOffset = ConstantMediaSize.END_OFFSET;
        }
    }

    /**
     * 针对进场时间不一样主题
     *
     * @param index
     * @param name
     * @param type
     * @param themeTimeType
     * @param themeManagerClass
     * @param themePreTreatmentClass
     */
    ThemeEnum(int index, String name, int type, int themeTimeType, Class<? extends ThemeIRender> themeManagerClass, Class<? extends IPretreatment> themePreTreatmentClass) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.themeManagerClass = themeManagerClass;
        if (themePreTreatmentClass == null) {
            this.themePreTreatmentClass = EmptyPreTreatment.class;
        } else {
            this.themePreTreatmentClass = themePreTreatmentClass;
        }
        this.themeTimeType = themeTimeType;
        if (BaseTimeThemeManager.class.isAssignableFrom(themeManagerClass)) {
            endOffset = 0;
        } else {
            endOffset = ConstantMediaSize.END_OFFSET;
        }
    }

    /**
     * 针对进场时间不一样主题
     *
     * @param index
     * @param name
     * @param type
     * @param themeTimeType
     * @param themeManagerClass
     * @param themePreTreatmentClass
     */
    ThemeEnum(int index, String name, int type, int themeTimeType, TransitionType transitionType, GlobalParticles particleType, Class<? extends ThemeIRender> themeManagerClass, Class<? extends IPretreatment> themePreTreatmentClass) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.themeManagerClass = themeManagerClass;
        this.mTransitionType = transitionType;
        this.mParticleType = particleType;
        if (themePreTreatmentClass == null) {
            this.themePreTreatmentClass = EmptyPreTreatment.class;
        } else {
            this.themePreTreatmentClass = themePreTreatmentClass;
        }
        this.themeTimeType = themeTimeType;
        if (BaseTimeThemeManager.class.isAssignableFrom(themeManagerClass)) {
            endOffset = 0;
        } else {
            endOffset = ConstantMediaSize.END_OFFSET;
        }
    }


    ThemeEnum(int index, String name, int type, TransitionType transitionType, GlobalParticles particleType,
              Class<? extends ThemeIRender> themeManagerClass, Class<? extends IPretreatment> themePreTreatmentClass) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.themeManagerClass = themeManagerClass;
        this.mTransitionType = transitionType;
        this.mParticleType = particleType;
        if (themePreTreatmentClass == null) {
            this.themePreTreatmentClass = EmptyPreTreatment.class;
        } else {
            this.themePreTreatmentClass = themePreTreatmentClass;
        }
        this.themeTimeType = ThemeConstant.THEME_ONE_DURATION;
        if (BaseTimeThemeManager.class.isAssignableFrom(themeManagerClass)) {
            endOffset = 0;
        } else {
            endOffset = ConstantMediaSize.END_OFFSET;
        }
    }

    /**
     * 针对进场时间不一样主题
     *
     * @param index
     * @param name
     * @param type
     * @param themeTimeType
     * @param themeManagerClass
     * @param themePreTreatmentClass
     */
    ThemeEnum(int index, String name, int type, int themeTimeType, int endOffset, Class<? extends ThemeIRender> themeManagerClass, Class<? extends IPretreatment> themePreTreatmentClass) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.themeManagerClass = themeManagerClass;
        if (themePreTreatmentClass == null) {
            this.themePreTreatmentClass = EmptyPreTreatment.class;
        } else {
            this.themePreTreatmentClass = themePreTreatmentClass;
        }
        this.themeTimeType = themeTimeType;
        this.endOffset = endOffset;
    }

    public static ThemeEnum getThemeType(int index) {
        for (ThemeEnum themeEnum : ThemeEnum.values()) {
            if (index == themeEnum.index) {
                return themeEnum;
            }
        }
        return null;
    }


    public static List<ThemeEnum> getThemeTypes(int Themetype) {
        List<ThemeEnum> list = new ArrayList<>();
        for (ThemeEnum themeEnum : ThemeEnum.values()) {
            if (Themetype == themeEnum.type) {
                list.add(themeEnum);
            }
        }
        return list;
    }

    public static Map<String, ThemeEnum> nameMap;

    public static ThemeEnum findThemNum(String alias) {
        if (nameMap == null) {
            nameMap = new HashMap<>();
            for (ThemeEnum themeEnum : ThemeEnum.values()) {
                nameMap.put(themeEnum.name, themeEnum);
            }
        }
        ThemeEnum result = nameMap.get(alias);
        return result;
    }

    @ThemeConstant.ThemeType
    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public int getIndex() {
        return index;
    }

    public Class<? extends ThemeIRender> getThemeManagerClass() {
        return themeManagerClass;
    }

    public void setThemeManagerClass(Class<? extends ThemeIRender> themeManagerClass) {
        this.themeManagerClass = themeManagerClass;
    }

    public Class<? extends IPretreatment> getThemePreTreatmentClass() {
        return themePreTreatmentClass;
    }

    public void setThemePreTreatmentClass(Class<? extends IPretreatment> themePreTreatmentClass) {
        this.themePreTreatmentClass = themePreTreatmentClass;
    }

    public int getThemeTimeType() {
        return themeTimeType;
    }

    public RatioType getRatioType() {
        return mRatioType;
    }

    public TransitionType getTransitionType() {
        return mTransitionType;
    }

    public void setTransitionType(TransitionType transitionType) {
        mTransitionType = transitionType;
    }

    public GlobalParticles getParticleType() {
        return mParticleType;
    }

    public void setParticleType(GlobalParticles particleType) {
        mParticleType = particleType;
    }

    public int getEndOffset() {
        return endOffset;
    }
}
