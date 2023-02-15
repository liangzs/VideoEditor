package com.ijoysoft.mediasdk.module.opengl.particle

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import com.ijoysoft.mediasdk.R
import com.ijoysoft.mediasdk.common.global.MediaSdk
import com.ijoysoft.mediasdk.module.entity.RatioType
import com.ijoysoft.mediasdk.module.opengl.particle.MyMovingDrawer.ParticleUpdaterWithScreenRatio
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeEnum
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeHelper
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11.Common11Paticle
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common11.Common11Paticle2
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common21.Common21ParticleColor
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common31.Common31ParticleColor
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common33.Common33Paticle
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.common.common9.Common9ParticleColor
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType
import kotlinx.coroutines.*
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * 全局粒子系统
 *
 * @date 2022/2/15  13:41
 * @author hayring
 * @param index 顺序
 * @param creator 生成drawer的lambda
 * @param previewResource 预览图在资源库中的id
 */
@Parcelize
enum class GlobalParticles() : Parcelable {


    NONE(0, "", { ParticleDrawerManager() }),
    SYSTEM_1(1,
        "file:///android_asset/particle_previews/common1_particle_preview.webp",
        {
            com.ijoysoft.mediasdk.module.opengl.particle.ParticleDrawerManager().also {
                it.addParticle(
                    com.ijoysoft.mediasdk.module.opengl.particle.ParticleBuilder().setParticleType(com.ijoysoft.mediasdk.module.opengl.particle.ParticleType.RANDOM)
                        .setListBitmaps(
                            java.util.Arrays.asList<android.graphics.Bitmap?>(
                                android.graphics.BitmapFactory.decodeResource(com.ijoysoft.mediasdk.common.global.MediaSdk.getInstance().getResouce(), com.ijoysoft.mediasdk.R.mipmap.common1_light),
                                android.graphics.BitmapFactory.decodeResource(com.ijoysoft.mediasdk.common.global.MediaSdk.getInstance().getResouce(), com.ijoysoft.mediasdk.R.mipmap.common1_pink_particle),
                                android.graphics.BitmapFactory.decodeResource(com.ijoysoft.mediasdk.common.global.MediaSdk.getInstance().getResouce(), com.ijoysoft.mediasdk.R.mipmap.common1_purple_particle)
                            )
                        )
                        .setParticleCount(8)
                        .setParticleSizeRatio(0.05f)
                        .build()
                )
                val common1: ParticleUpdaterWithScreenRatio = object : ParticleUpdaterWithScreenRatio {
                    var yOffset: kotlin.Float = 0.01f
                    override fun onRatioChanged(ratioType: com.ijoysoft.mediasdk.module.entity.RatioType?) {
                        yOffset = ratioType!!.getValue() * 0.01f
                    }

                    override fun update(particle: kotlin.FloatArray?, offset: kotlin.Int, length: kotlin.Int) {
                        //向左下移动
                        particle!![offset] -= 0.01f
                        particle!![offset + 1] += yOffset
                    }
                }
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(listOf<Bitmap?>(
                            BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common1_meteor)
                        ))
                        .setParticleCount(16)
                        .setFlipTexture { false }
                        .setParticleSizeRatio(0.03f)
                        .setPositionUpdater(common1)
                        .build()
                )
            }
        }),
    SYSTEM_2(2, "file:///android_asset/particle_previews/common2_particle_preview.webp",
        { ThemeHelper.createParticles(ThemeEnum.COMMON_TWO) }),
    SYSTEM_3(3, "file:///android_asset/particle_previews/common3_particle_preview.webp",
        { ThemeHelper.createParticles(ThemeEnum.COMMON_THREE) }),
    SYSTEM_4(4, "file:///android_asset/particle_previews/common4_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                val bitmaps = concurrentLoad(R.mipmap.common4_particle3, R.mipmap.common4_alpha_particle)

                it.addParticle(ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                    .setListBitmaps(
                        bitmaps.subList(0, 2)
                    )
                    .setParticleCount(6)
                    .setParticleSizeRatio(0.2f)
                    .build()
                )
                val common4Updater: ParticleUpdaterWithScreenRatio = object : ParticleUpdaterWithScreenRatio {
                    var yOffset: kotlin.Float = 0.03f
                    override fun onRatioChanged(ratioType: RatioType?) {
                        yOffset = ratioType!!.getValue() * 0.03f
                    }

                    override fun update(particle: FloatArray, offset: Int, length: Int) {
                        //向左下移动
                        particle[offset] -= 0.03f
                        particle[offset + 1] += yOffset
                    }
                }
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(listOf<Bitmap?>(
                            BitmapFactory.decodeResource(MediaSdk.getInstance().getResouce(), R.mipmap.common4_flash_star)
                        ))
                        .setParticleCount(16)
                        .setParticleSizeRatio(0.03f)
                        .setPositionUpdater(common4Updater)
                        .setFlipTexture({
                            false
                        })
                        .build()
                )
                it.addParticle(ParticleBuilder().setParticleType(ParticleType.RANDOM)
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
                )
            }
        }),

    /**
     * @see ThemeEnum.COMMON_SIX
     */
    SYSTEM_5(5, "file:///android_asset/particle_previews/common6_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                val bitmaps = concurrentLoad(R.mipmap.system5_star1, R.mipmap.system5_star2)
                it.addParticle(
                    ParticleBuilder()
                        .setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(bitmaps)
                        .setLoadAllParticleInFirstFrame(true)
                        .setParticleCount(32)
                        .setParticleSizeDelta(5f)
                        .setShortLife(0.5f)
                        .build()
                )
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.COMMON_NINE
     */
    SYSTEM_6(6, "file:///android_asset/particle_previews/common9_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                            listOf(
                                BitmapFactory.decodeResource(MediaSdk.getInstance().resouce, R.mipmap.system6_particle1)
                            )
                        )
                        .setParticleCount(8)
                        .setParticleSizeRatio(0.01f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .setLoadAllParticleInFirstFrame(true)
                        .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                        .build()
                )

                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                            listOf(
                                BitmapFactory.decodeResource(MediaSdk.getInstance().resouce, R.mipmap.system6_particle2)
                            )
                        )
                        .setParticleCount(64)
                        .setParticleSizeRatio(0.01f)
                        .setRandomAlphaStart(0f)
                        .setSizeFloatingStart(0.3f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .setLoadAllParticleInFirstFrame(true)
                        .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                        .setColor(true, Common9ParticleColor())
                        .build()
                )
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(
                            listOf(
                                BitmapFactory.decodeResource(MediaSdk.getInstance().resouce, R.mipmap.system6_particle3)
                            )
                        )
                        .setParticleCount(4)
                        .setParticleSizeRatio(0.01f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .setLoadAllParticleInFirstFrame(true)
                        .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                        .build()
                )
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.COMMON_ELEVEN
     */
    SYSTEM_7(7, "file:///android_asset/particle_previews/common11_particle_preview.webp",
        {
            ParticleDrawerManager().also {

                val common11Paticle2 = Common11Paticle2()
                val bitmaps = concurrentLoad(
                    com.ijoysoft.mediasdk.R.mipmap.system7_particle8, com.ijoysoft.mediasdk.R.mipmap.system7_particle9, com.ijoysoft.mediasdk.R.mipmap.system7_particle10, com.ijoysoft.mediasdk.R.mipmap.system7_particle11,
                    com.ijoysoft.mediasdk.R.mipmap.system7_particle12, com.ijoysoft.mediasdk.R.mipmap.system7_particle13)
                it.addParticle(
                    ParticleBuilder(
                        ParticleType.SCALE_LOOP, bitmaps)
                        .setParticleCount(7)
                        .setPointSize(1, 4)
                        .setLifeTime(kotlin.Float.Companion.MAX_VALUE, 0f)
                        .setPointScalePeriod(1.5f)
                        .setPointScaleNodispear(true)
                        .setPointScaleNoRandom(true)
                        .setLoadAllParticleInFirstFrame(true)
                        .setILocationAction(common11Paticle2)
                        .setITexture(common11Paticle2)
                        .build()
                )
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.COMMON_FIFTEEN
     */
    SYSTEM_8(8, "file:///android_asset/particle_previews/common15_particle_preview.webp",
        { ThemeHelper.createParticles(ThemeEnum.COMMON_FIFTEEN).also { it.onSurfaceCreated() } }),

    /**
     * @see ThemeEnum.COMMON_SEVENTEEN
     */
    SYSTEM_9(9, "file:///android_asset/particle_previews/common17_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                val bitmaps = concurrentLoad(R.mipmap.system9_star, R.mipmap.system9_red_heart, R.mipmap.system9_yellow_heart, R.mipmap.system9_purple_heart)
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(ArrayList(bitmaps.subList(0, 1)))
                        .setParticleCount(16)
                        .setShortLife(0.5f)
                        .setParticleSizeDelta(2f)
                        .build()
                )
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(ArrayList(bitmaps.subList(1, 4)))
                        .setParticleCount(8)
                        .setParticleSizeRatio(0.02f)
                        .setSizeFloatingStart(0.3f)
                        .setFlipTexture { false }
                        .setPositionUpdater(ParticleBuilder.createBubblingUpdater())
                        .build()
                )
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.COMMON_EIGHTEEN
     */
    SYSTEM_10(10, "file:///android_asset/particle_previews/common18_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                val bitmaps = concurrentLoad(R.mipmap.system10_heart1, R.mipmap.system10_star1, R.mipmap.system10_star2)
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(ArrayList(bitmaps.subList(0, 1)))
                        .setParticleCount(8)
                        .setSizeFloatingStart(0.1f)
                        .setParticleSizeRatio(0.045f)
                        .setSelfRotate(true)
                        .build()
                )

                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(ArrayList(bitmaps.subList(1, 3)))
                        .setParticleCount(16)
                        .setShortLife(0.5f)
                        .setParticleSizeDelta(16f)
                        .build()
                )
                it.onSurfaceCreated()

            }
        }),

    /**
     * @see ThemeEnum.COMMON_NINETEEN
     */
    SYSTEM_11(11, "file:///android_asset/particle_previews/common19_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                val bitmaps = concurrentLoad(R.mipmap.system11_particle1, R.mipmap.system11_particle2, R.mipmap.system11_particle3)

                val common19Updater1: ParticleUpdaterWithScreenRatio =
                    object : ParticleUpdaterWithScreenRatio {
                        var yOffset = 0.01f


                        override fun onRatioChanged(ratioType: RatioType) {
                            yOffset = ratioType.getValue() * 0.01f * 0.70422535211267f
                        }

                        override fun update(particle: FloatArray, offset: Int, length: Int) {
                            //向左下移动
                            particle[offset] -= 0.01f
                            particle[offset + 1] += yOffset
                        }
                    }

                val common19Updater2: ParticleUpdaterWithScreenRatio =
                    object : ParticleUpdaterWithScreenRatio {
                        var yOffset = 0.01f
                        override fun onRatioChanged(ratioType: RatioType) {
                            yOffset = ratioType.getValue() * 0.01f * 0.6956521739130434f
                        }

                        override fun update(particle: FloatArray, offset: Int, length: Int) {
                            //向左下移动
                            particle[offset] -= 0.01f
                            particle[offset + 1] += yOffset
                        }
                    }
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(listOf(bitmaps[0]))
                        .setParticleCount(6)
                        .setParticleSizeRatio(0.04f)
                        .setSizeFloatingStart(0.4f)
                        .setFlipTexture { false }
                        .setPositionUpdater(common19Updater1)
                        .build()
                )
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(listOf(bitmaps[1]))
                        .setParticleCount(6)
                        .setParticleSizeRatio(0.04f)
                        .setSizeFloatingStart(0.4f)
                        .setFlipTexture { false }
                        .setPositionUpdater(common19Updater2)
                        .build()
                )

                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(listOf(bitmaps[2]))
                        .setParticleCount(16)
                        .setShortLife(0.5f)
                        .setParticleSizeDelta(2f)
                        .build()
                )
                it.onSurfaceCreated()

            }
        }),


    /**
     * @see ThemeEnum.COMMON_TWENTY
     */
    SYSTEM_12(12, "file:///android_asset/particle_previews/common20_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                val bitmaps = concurrentLoad(R.mipmap.system12_particle1, R.mipmap.system12_particle2, R.mipmap.system12_particle3, R.mipmap.system12_particle4)
                it.addParticle(ParticleBuilder(
                    ParticleType.CENTER_EXPAND, bitmaps).setParticleCount(16)
                    .setPointSize(3, 40)
                    .setLoadAllParticleInFirstFrame(true)
                    .setSelfRotate(true).setScale(false).build()
                )
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.COMMON_TWENTYONE
     */
    SYSTEM_13(13, "file:///android_asset/particle_previews/common21_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                val bitmaps = concurrentLoad(R.mipmap.system13_white_shine, R.mipmap.system13_red_shine, R.mipmap.system13_orange_shine, R.mipmap.system13_particle,
                    R.mipmap.system13_particle1, R.mipmap.system13_particle2)
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.SCALE_FADE)
                        .setListBitmaps(
                            Arrays.asList(bitmaps[0], bitmaps[1], bitmaps[2])
                        )
                        .setParticleCount(3)
                        .setParticleSizeDelta(6f)
                        .build()
                )
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.RANDOM)
                        .setListBitmaps(listOf(bitmaps[3]))
                        .setParticleCount(32)
                        .setSizeFloatingStart(0.3f)
                        .setParticleSizeRatio(0.02f)
                        .setColor(true, Common21ParticleColor())
                        .setRandomAlphaStart(0f)
                        .setLoadAllParticleInFirstFrame(true)
                        .setRebound(true)
                        .build()
                )
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.MOVING)
                        .setListBitmaps(listOf(bitmaps[4]))
                        .setParticleCount(32)
                        .setParticleSizeRatio(0.02f)
                        .setSizeFloatingStart(0.3f)
                        .setPositionUpdater(ParticleBuilder.createFallingUpdater())
                        .setLoadAllParticleInFirstFrame(true)
                        .setStartPositionGenerator(ParticleBuilder.createTopParticleGenerator())
                        .build()
                )
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.COMMON_TWENTYFIVE
     */
    SYSTEM_14(14, "file:///android_asset/particle_previews/common25_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                it.addParticle(
                    ParticleBuilder().setParticleType(ParticleType.RANDOM_FADE)
                        .setListBitmaps(concurrentLoad(
                            R.mipmap.system14_blue_particle1, R.mipmap.system14_blue_particle2, R.mipmap.system14_purple_particle1,
                            R.mipmap.system14_purple_particle2
                        )
                        )
                        .setParticleCount(24)
                        .setSizeFloatingStart(0.3f)
                        .setParticleSizeRatio(0.025f)
                        .setLoadAllParticleInFirstFrame(true)
                        .setDisableFade(true)
                        .build()
                )
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.COMMON_TWENTYNINE
     */
    SYSTEM_15(15, "file:///android_asset/particle_previews/common29_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                val common11Paticle = Common11Paticle()
                it.addParticle(
                    ParticleBuilder(ParticleType.FALLING,
                        concurrentLoad(R.mipmap.system15_particle2, R.mipmap.system15_particle3)
                    ).setParticleCount(24)
                        .setPointSize(3, 30)
                        .setLoadAllParticleInFirstFrame(true)
                        .setIDirectionAction(common11Paticle)
                        .setILocationAction(common11Paticle)
                        .setIAccelerate(common11Paticle)
                        .build()
                )
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.COMMON_THIRDTYONE
     */
    SYSTEM_16(16, "file:///android_asset/particle_previews/common31_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                val bitmaps = concurrentLoad(R.mipmap.system16_particle1, R.mipmap.system16_particle2, R.mipmap.system16_particle3, R.mipmap.system16_particle4,
                    R.mipmap.system16_particle5, R.mipmap.system16_particle6)
                it.addParticle(
                    ParticleBuilder(ParticleType.FALLING, ArrayList(bitmaps.subList(0, 5)))
                        .setParticleCount(30)
                        .setPointSize(3, 30)
                        .setLifeTime(4f, 1f)
                        .setSelfRotate(true)
                        .build()
                )

                it.addParticle(
                    ParticleBuilder(ParticleType.HOVER, listOf(bitmaps[5])).setParticleCount(12)
                        .setPointSize(3, 20)
                        .setScale(true)
                        .setSelfRotate(true)
                        .setColor(true, Common31ParticleColor())
                        .setLoadAllParticleInFirstFrame(true)
                        .build()
                )
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.COMMON_THIRDTYTHREE
     */
    SYSTEM_17(17, "file:///android_asset/particle_previews/common33_particle_preview.webp",
        {
            ParticleDrawerManager().also {

                it.addParticle(ParticleBuilder(ParticleType.SCALE_LOOP,
                    concurrentLoad(R.mipmap.system17_particle1, R.mipmap.system17_particle2, R.mipmap.system17_particle3)
                ).setParticleCount(40)
                    .setPointSize(3, 20)
                    .setMinPointSize(2)
                    .setLoadAllParticleInFirstFrame(true)
                    .setILocationAction(Common33Paticle())
                    .build()
                )
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.HOLIDAY_ONE
     */
    SYSTEM_18(18, "file:///android_asset/particle_previews/holiday1_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                it.addParticle(object : com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday1.HDOneDrawer() {
                    override fun loadBitmap(): android.graphics.Bitmap {
                        return BitmapFactory.decodeResource(com.ijoysoft.mediasdk.common.global.MediaSdk.getInstance().resouce,
                            R.mipmap.system18_particle
                        )
                    }
                })
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.HOLIDAY_SIX
     */
    SYSTEM_19(19, "file:///android_asset/particle_previews/holiday6_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                it.addParticle(object : com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday6.HDSixDrawer() {
                    override fun loadBitmaps(): List<android.graphics.Bitmap> {
                        return concurrentLoad(
                            R.mipmap.system19_particle1, R.mipmap.system19_particle2, R.mipmap.system19_particle3
                        )
                    }
                })
                it.onSurfaceCreated()
            }
        }),

    /**
     * @see ThemeEnum.HOLIDAY_NINE
     */
    SYSTEM_20(20, "file:///android_asset/particle_previews/holiday9_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                it.addParticle(object : com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday9.HDNineDrawer() {
                    override fun loadBitmaps(): kotlin.collections.List<android.graphics.Bitmap> {
                        return concurrentLoad(
                            com.ijoysoft.mediasdk.R.mipmap.system20_particle1,
                            com.ijoysoft.mediasdk.R.mipmap.system20_particle2,
                            com.ijoysoft.mediasdk.R.mipmap.system20_particle3
                        )
                    }
                })
                it.onSurfaceCreated()

            }
        }),

    /**
     * @see ThemeEnum.CELEBRATION_TWO
     */
    CELEBRATION_TWO(21, "file:///android_asset/particle_previews/celebration2_particle_preview.webp",
        {
            ParticleDrawerManager().also {
                it.addParticle(object : com.ijoysoft.mediasdk.module.opengl.theme.themecontent.celebration.celebration2.CDTwoDrawer() {
                    override fun loadBitmap(): android.graphics.Bitmap {
                        return BitmapFactory.decodeResource(com.ijoysoft.mediasdk.common.global.MediaSdk.getInstance().resouce,
                            R.mipmap.system21_particle
                        )
                    }
                })
                it.onSurfaceCreated()
            }
        }),

    //PAG 粒子
    PAG_PARTICLE1(22, true, "particle/1.pag", "particle_resource/1", PAGOneBgParticle::class.java),
    PAG_PARTICLE2(PAG_PARTICLE1.index + 1, true, "particle/2.pag", "particle_resource/2", PAGOneBgParticle::class.java),
    PAG_PARTICLE3(PAG_PARTICLE2.index + 1, true, "particle/3.pag", "particle_resource/3", PAGOneBgParticle::class.java),
    PAG_PARTICLE4(PAG_PARTICLE3.index + 1, true, "particle/4.pag", "particle_resource/4", PAGOneBgParticle::class.java),
    PAG_PARTICLE5(PAG_PARTICLE4.index + 1, true, "particle/5.pag", "particle_resource/5", PAGOneBgParticle::class.java),
    PAG_PARTICLE6(PAG_PARTICLE5.index + 1, true, "particle/6.pag", "particle_resource/6", PAGOneBgParticle::class.java),
    PAG_PARTICLE7(PAG_PARTICLE6.index + 1, true, "particle/7.pag", "particle_resource/7", PAGOneBgParticle::class.java),
    PAG_PARTICLE8(PAG_PARTICLE7.index + 1, true, "particle/8.pag", "particle_resource/8", PAGOneBgParticle::class.java),
    PAG_PARTICLE9(PAG_PARTICLE8.index + 1, true, "particle/9.pag", "particle_resource/9", PAGOneBgParticle::class.java),
    PAG_PARTICLE10(PAG_PARTICLE9.index + 1, true, "particle/10.pag", "particle_resource/10", PAGOneBgParticle::class.java),
    PAG_PARTICLE11(PAG_PARTICLE10.index + 1, true, "particle/11.pag", "particle_resource/11", PAGOneBgParticle::class.java),
    PAG_PARTICLE12(PAG_PARTICLE11.index + 1, true, "particle/12.pag", "particle_resource/12", PAGOneBgParticle::class.java),
    PAG_PARTICLE13(PAG_PARTICLE12.index + 1, true, "particle/13.pag", "particle_resource/13", PAGOneBgParticle::class.java),
    PAG_PARTICLE14(PAG_PARTICLE13.index + 1, true, "particle/14.pag", "particle_resource/14", PAGOneBgParticle::class.java),
    PAG_PARTICLE15(PAG_PARTICLE14.index + 1, true, "particle/15.pag", "particle_resource/15", PAGOneBgParticle::class.java),
    PAG_PARTICLE16(PAG_PARTICLE15.index + 1, true, "particle/16.pag", "particle_resource/16", PAGOneBgParticle::class.java),
    PAG_PARTICLE17(PAG_PARTICLE16.index + 1, true, "particle/17.pag", "particle_resource/17", PAGNoBgParticle::class.java),

    //放一个在本地，就你了
    PAG_PARTICLE18(PAG_PARTICLE17.index + 1, "file:///android_asset/particle_previews/pag_particle18.webp", "pag_particle/particle18.pag", PAGNoBgParticle::class.java),
    PAG_PARTICLE19(PAG_PARTICLE18.index + 1, true, "particle/19.pag", "particle_resource/19", PAGOneBgParticle::class.java),
    PAG_PARTICLE20(PAG_PARTICLE19.index + 1, true, "particle/20.pag", "particle_resource/20", PAGOneBgParticle::class.java),
    PAG_PARTICLE21(PAG_PARTICLE20.index + 1, true, "particle/21.pag", "particle_resource/21", PAGNoBgParticle::class.java),
    PAG_PARTICLE22(PAG_PARTICLE21.index + 1, true, "particle/22.pag", "particle_resource/22", PAGOneBgParticle::class.java),
    PAG_PARTICLE23(PAG_PARTICLE22.index + 1, true, "particle/23.pag", "particle_resource/23", PAGNoBgParticle::class.java),
    PAG_PARTICLE24(PAG_PARTICLE23.index + 1, true, "particle/24.pag", "particle_resource/24", PAGNoBgParticle::class.java),
    PAG_PARTICLE25(PAG_PARTICLE24.index + 1, true, "particle/25.pag", "particle_resource/25", PAGNoBgParticle::class.java),
    PAG_PARTICLE26(PAG_PARTICLE25.index + 1, true, "particle/26.pag", "particle_resource/26", PAGOneBgParticle::class.java),

    //枚举结束分号
    ;

    companion object {

        /**
         * 多线程加载多个bitmap
         * TODO: 协程泄露
         */
        @JvmStatic
        fun concurrentLoad(vararg idList: Int): List<Bitmap> {
            if (idList.size < 5) {
                //数量小，串行加载
                val result = ArrayList<Bitmap>(idList.size)
                idList.forEach {
                    result.add(BitmapFactory.decodeResource(MediaSdk.getInstance().resouce, it))
                }
                return result
            } else {
                //数量大，使用并发加载
                var result: ArrayList<Bitmap>
                //使用当前线程对其他线程进行等待
                runBlocking {
                    //延迟任务
                    val deferredList = ArrayList<Deferred<Bitmap>>(idList.size)
                    //对每个资源启动并行加载任务
                    idList.forEach {
                        deferredList.add(CoroutineScope(Dispatchers.IO).async {
                            BitmapFactory.decodeResource(MediaSdk.getInstance().resouce, it)
                        })
                    }
                    //阻塞并等待所有任务完成，将其添加进list
                    result = ArrayList<Bitmap>(deferredList.awaitAll())
                }
                return result
            }
        }


    }

    var index: Int = 0;
    var previewPath: String? = null
    lateinit var creator: () -> ParticleDrawerManager;
    var offsetPath: String? = null
    var transitionClass: Class<out PAGNoBgParticle>? = null
    var downPath: String? = null
    var isOnline = false;
    var savePath: String? = null
    var onlineIconOffset: String = "";
    var onlineIconPath: String = "";

    constructor(index: Int, previewPath: String, creator: () -> ParticleDrawerManager) : this() {
        this.index = index;
        this.previewPath = previewPath
        this.creator = creator;
    }

    constructor(index: Int, isOnline: Boolean, offsetPath: String, onlineIconOffset: String, clazz: Class<out PAGNoBgParticle?>?) : this() {
        this.index = index;
        this.previewPath = previewPath
        this.isOnline = isOnline
        this.offsetPath = offsetPath
        this.downPath = offsetPath
        this.transitionClass = clazz
        this.onlineIconOffset = onlineIconOffset;
        creator = { ParticleDrawerManager() };
    }

    constructor(index: Int, previewPath: String, downPath: String, clazz: Class<out PAGNoBgParticle>) : this() {
        this.index = index;
        this.previewPath = previewPath
        this.offsetPath = downPath
        this.savePath = downPath
        this.transitionClass = clazz
        creator = { ParticleDrawerManager() };
    }


    open fun setRequestPath(path: String): GlobalParticles? {
        this.downPath = path
        return this
    }

}