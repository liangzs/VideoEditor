package com.ijoysoft.videoeditor.theme.manager

import androidx.annotation.IntDef
import com.ijoysoft.mediasdk.common.utils.FileUtils
import com.ijoysoft.mediasdk.module.opengl.transition.*
import com.ijoysoft.videoeditor.R
import com.ijoysoft.videoeditor.entity.FilterEntity
import com.ijoysoft.videoeditor.model.download.DownloadHelper
import com.ijoysoft.videoeditor.model.download.DownloadPath
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiArray.Companion.CLASSIC
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiArray.Companion.GLITCH
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiArray.Companion.GRAPH
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiArray.Companion.GRID
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiArray.Companion.INTEREST
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiArray.Companion.MG
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiArray.Companion.PARTICLE
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiArray.Companion.ROCK
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiArray.Companion.SLIDE
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiGroup.Companion.GROUP_BASE
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiGroup.Companion.GROUP_GLITCH
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiGroup.Companion.GROUP_INTEREST
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiGroup.Companion.GROUP_MG
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiGroup.Companion.GROUP_NONE
import com.ijoysoft.videoeditor.theme.manager.TransitionRepository.TransiGroup.Companion.GROUP_PARTICLE
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.random.Random

object TransitionRepository {

    val transiClassis: List<TransitionType> by lazy {
        listOf(
            TransitionType.FADE_IN,
            TransitionType.BLUR,
            TransitionType.FLASH_WHITE,
            TransitionType.FLASH_BLACK,
            TransitionType.SHADE_DISSOLVE,
            TransitionType.CIRCLE_ROTATE,
            TransitionType.READ_BOOK,
            TransitionType.TRIANGLE,
            TransitionType.SHADE_REVEAL,
            TransitionType.SHADE_REVEAL_RE,
            TransitionType.BLUR,
            TransitionType.WIPE_LEFT,
            TransitionType.SHADE_TWO_LINE_CLOCK,
        )
    }


    val transiSlide: List<TransitionType> by lazy {
        listOf(
            TransitionType.MOVE_LEFT,
            TransitionType.MOVE_RIGHT,
            TransitionType.MOVE_TOP,
            TransitionType.MOVE_BOTTOM,
            TransitionType.MOVE_PUSH_L,
            TransitionType.MOVE_PUSH_R,
            TransitionType.MOVE_PUSH_T,
            TransitionType.MOVE_PUSH_B,
        )
    }

    val transiGrid: List<TransitionType> by lazy {
        listOf(
            TransitionType.SQUARE_TWINKLE,
            TransitionType.SHADE_PORTRAIT,
            TransitionType.SHADE_LANDSCAPE,
            TransitionType.GRID_SHOP,
            TransitionType.SHADE_BAR_CROSS,
            TransitionType.SHADE_BAR_CROSS_Y,
            TransitionType.SHADE_FOUR_CORNER,
            TransitionType.SHADE_FOUR_CORNER_RE,
            TransitionType.SHADE_MULTI_SQUARE,
            TransitionType.SHADE_MULTI_SQUARE_RE,
            TransitionType.SHADE_MULTI_TRIANGLE,
            TransitionType.SHADE_MULTI_DIAMOND,
            TransitionType.SHADE_MULTI_DIAMOND_RE,
            TransitionType.SHADE_RANDOMBAR,
        )
    }

    val transiGraph: List<TransitionType> by lazy {
        listOf(
            TransitionType.CIRCLE_ZOOM,
            TransitionType.OVAL_ZOOM,
            TransitionType.HEART_SHAPE,
            TransitionType.WHOLE_ZOOM,
            TransitionType.SHADE_PLUS,
            TransitionType.SHADE_PLUS_RE,
            TransitionType.SHADE_DIAMOND,
            TransitionType.SHADE_DIAMOND_RE,
            TransitionType.SHADE_SQUARE,
            TransitionType.SHADE_SQUARE_RE,
            TransitionType.SHADE_SPLIT,
            TransitionType.SHADE_SPLIT_RE,
        )
    }
    val transiRock: List<TransitionType> by lazy {
        listOf(
            TransitionType.HAHA_MIRROR,
            TransitionType.SHAKE,
            TransitionType.SQUARE_3D,
            TransitionType.WAVE,
            TransitionType.THUMB_MOVE,
            TransitionType.ROTATE_AXIS_Y,
            TransitionType.SHADE_MOVE_SHUTTER,
        )
    }

    /**
     * 请求地址
     */
    fun dealRequestPath(transitionType: TransitionType): String {
        return DownloadPath.BASE_PATH + "/" + transitionType.downOffet
    }

    /**
     * 下载存储地址
     */
    fun dealLocalPath(transitionType: TransitionType): String {
        return DownloadHelper.getDownloadPath(DownloadPath.BASE_PATH + "/" + transitionType.downOffet)
    }


    val transiInterst: List<TransitionType> by lazy {
        listOf(
            TransitionType.PAG_INTEREST_CAMERA1,
            TransitionType.PAG_INTEREST_CAMERA2,
            /*LEAF*/
            TransitionType.PAG_INTEREST_PAPER1,
            TransitionType.PAG_INTEREST_PAPER2.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_PAPER2))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_PAPER2)),
            TransitionType.PAG_INTEREST_PAPER3.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_PAPER3))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_PAPER3)),
            TransitionType.PAG_INTEREST_PAPER4.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_PAPER4))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_PAPER4)),
            TransitionType.PAG_INTEREST_FACE1.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_FACE1))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_FACE1)),
            /*LEAF*/
            TransitionType.PAG_INTEREST_LEAF1.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_LEAF1))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_LEAF1)),
            TransitionType.PAG_INTEREST_LEAF2.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_LEAF2))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_LEAF2)),
            TransitionType.PAG_INTEREST_LEAF3.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_LEAF3))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_LEAF3)),
            TransitionType.PAG_INTEREST_LEAF4.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_LEAF4))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_LEAF4)),
            TransitionType.PAG_INTEREST_LEAF5.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_LEAF5))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_LEAF5)),
            /*PEN*/
            TransitionType.PAG_PEN1.setRequestPath(dealRequestPath(TransitionType.PAG_PEN1))
                .setLocalPath(dealLocalPath(TransitionType.PAG_PEN1)),
            TransitionType.PAG_PEN2.setRequestPath(dealRequestPath(TransitionType.PAG_PEN2))
                .setLocalPath(dealLocalPath(TransitionType.PAG_PEN2)),
            TransitionType.PAG_PEN3.setRequestPath(dealRequestPath(TransitionType.PAG_PEN3))
                .setLocalPath(dealLocalPath(TransitionType.PAG_PEN3)),
            TransitionType.PAG_PEN4.setRequestPath(dealRequestPath(TransitionType.PAG_PEN4))
                .setLocalPath(dealLocalPath(TransitionType.PAG_PEN4)),
            TransitionType.PAG_PEN5.setRequestPath(dealRequestPath(TransitionType.PAG_PEN5))
                .setLocalPath(dealLocalPath(TransitionType.PAG_PEN5)),
            TransitionType.PAG_PEN6.setRequestPath(dealRequestPath(TransitionType.PAG_PEN6))
                .setLocalPath(dealLocalPath(TransitionType.PAG_PEN6)),
            TransitionType.PAG_PEN7.setRequestPath(dealRequestPath(TransitionType.PAG_PEN7))
                .setLocalPath(dealLocalPath(TransitionType.PAG_PEN7)),
            TransitionType.PAG_PEN8.setRequestPath(dealRequestPath(TransitionType.PAG_PEN8))
                .setLocalPath(dealLocalPath(TransitionType.PAG_PEN8)),
        )
    }

    val transiMg: List<TransitionType> by lazy {
        listOf(
            TransitionType.PAG_CARTON1,
            TransitionType.PAG_CARTON2,
            TransitionType.PAG_CARTON3,
            TransitionType.PAG_CARTON4,
            TransitionType.PAG_CARTON5,
            TransitionType.PAG_CARTON6,
            TransitionType.PAG_CARTON7,
            TransitionType.PAG_CARTON8,
            TransitionType.PAG_CARTON9,
            TransitionType.PAG_CARTON10,
            TransitionType.PAG_CARTON11,
            TransitionType.PAG_FLOW1.setRequestPath(dealRequestPath(TransitionType.PAG_FLOW1))
                .setLocalPath(dealLocalPath(TransitionType.PAG_FLOW1)),
            TransitionType.PAG_FLOW2.setRequestPath(dealRequestPath(TransitionType.PAG_FLOW2))
                .setLocalPath(dealLocalPath(TransitionType.PAG_FLOW2)),
            TransitionType.PAG_MG1.setRequestPath(dealRequestPath(TransitionType.PAG_MG1))
                .setLocalPath(dealLocalPath(TransitionType.PAG_MG1)),
            TransitionType.PAG_MG2.setRequestPath(dealRequestPath(TransitionType.PAG_MG2))
                .setLocalPath(dealLocalPath(TransitionType.PAG_MG2)),
            TransitionType.PAG_MG3.setRequestPath(dealRequestPath(TransitionType.PAG_MG3))
                .setLocalPath(dealLocalPath(TransitionType.PAG_MG3)),
            TransitionType.PAG_MG4.setRequestPath(dealRequestPath(TransitionType.PAG_MG4))
                .setLocalPath(dealLocalPath(TransitionType.PAG_MG4)),
            TransitionType.PAG_MG5.setRequestPath(dealRequestPath(TransitionType.PAG_MG5))
                .setLocalPath(dealLocalPath(TransitionType.PAG_MG5)),
            TransitionType.PAG_MG6.setRequestPath(dealRequestPath(TransitionType.PAG_MG6))
                .setLocalPath(dealLocalPath(TransitionType.PAG_MG6)),
            TransitionType.PAG_GRAPH1.setRequestPath(dealRequestPath(TransitionType.PAG_GRAPH1))
                .setLocalPath(dealLocalPath(TransitionType.PAG_GRAPH1)),
            TransitionType.PAG_GRAPH2.setRequestPath(dealRequestPath(TransitionType.PAG_GRAPH2))
                .setLocalPath(dealLocalPath(TransitionType.PAG_GRAPH2)),
            TransitionType.PAG_GRAPH3.setRequestPath(dealRequestPath(TransitionType.PAG_GRAPH3))
                .setLocalPath(dealLocalPath(TransitionType.PAG_GRAPH3)),
            TransitionType.PAG_GRAPH4.setRequestPath(dealRequestPath(TransitionType.PAG_GRAPH4))
                .setLocalPath(dealLocalPath(TransitionType.PAG_GRAPH4)),
            TransitionType.PAG_GRAPH5.setRequestPath(dealRequestPath(TransitionType.PAG_GRAPH5))
                .setLocalPath(dealLocalPath(TransitionType.PAG_GRAPH5)),

            )
    }

    /*PAG particle*/
    val transiParticle: List<TransitionType> by lazy {
        listOf(
            TransitionType.PAG_BOOM1.setRequestPath(dealRequestPath(TransitionType.PAG_BOOM1))
                .setLocalPath(dealLocalPath(TransitionType.PAG_BOOM1)),
            TransitionType.PAG_BOOM2.setRequestPath(dealRequestPath(TransitionType.PAG_BOOM2))
                .setLocalPath(dealLocalPath(TransitionType.PAG_BOOM2)),
            TransitionType.PAG_BOOM3.setRequestPath(dealRequestPath(TransitionType.PAG_BOOM3))
                .setLocalPath(dealLocalPath(TransitionType.PAG_BOOM3)),
            TransitionType.PAG_BOOM4.setRequestPath(dealRequestPath(TransitionType.PAG_BOOM4))
                .setLocalPath(dealLocalPath(TransitionType.PAG_BOOM4)),
            TransitionType.PAG_BOOM5.setRequestPath(dealRequestPath(TransitionType.PAG_BOOM5))
                .setLocalPath(dealLocalPath(TransitionType.PAG_BOOM5)),
            TransitionType.PAG_COLOR_HEART.setRequestPath(dealRequestPath(TransitionType.PAG_COLOR_HEART))
                .setLocalPath(dealLocalPath(TransitionType.PAG_COLOR_HEART)),
            TransitionType.PAG_PARTICLE1.setRequestPath(dealRequestPath(TransitionType.PAG_PARTICLE1))
                .setLocalPath(dealLocalPath(TransitionType.PAG_PARTICLE1)),
            /*HALO*/
            TransitionType.PAG_HALO1.setRequestPath(dealRequestPath(TransitionType.PAG_HALO1))
                .setLocalPath(dealLocalPath(TransitionType.PAG_HALO1)),
            TransitionType.PAG_HALO2.setRequestPath(dealRequestPath(TransitionType.PAG_HALO2))
                .setLocalPath(dealLocalPath(TransitionType.PAG_HALO2)),
            TransitionType.PAG_HALO3,
            TransitionType.PAG_HALO4.setRequestPath(dealRequestPath(TransitionType.PAG_HALO4))
                .setLocalPath(dealLocalPath(TransitionType.PAG_HALO4)),
            TransitionType.PAG_HALO5.setRequestPath(dealRequestPath(TransitionType.PAG_HALO5))
                .setLocalPath(dealLocalPath(TransitionType.PAG_HALO5)),
            TransitionType.PAG_HALO6,
            TransitionType.PAG_HALO7.setRequestPath(dealRequestPath(TransitionType.PAG_HALO7))
                .setLocalPath(dealLocalPath(TransitionType.PAG_HALO7)),
            TransitionType.PAG_HALO8.setRequestPath(dealRequestPath(TransitionType.PAG_HALO8))
                .setLocalPath(dealLocalPath(TransitionType.PAG_HALO8)),
            TransitionType.PAG_HALO9,
        )
    }

    /*pag glitch*/
    val transiGlitch: List<TransitionType> by lazy {
        listOf(
            /*GLITCH*/
            TransitionType.PAG_INTEREST_GLITCH1.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_GLITCH1))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_GLITCH1)),
            TransitionType.PAG_INTEREST_GLITCH2.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_GLITCH2))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_GLITCH2)),
            TransitionType.PAG_INTEREST_GLITCH3,
            TransitionType.PAG_INTEREST_GLITCH4.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_GLITCH4))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_GLITCH4)),
            TransitionType.PAG_INTEREST_GLITCH5.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_GLITCH5))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_GLITCH5)),
            TransitionType.PAG_INTEREST_GLITCH6.setRequestPath(dealRequestPath(TransitionType.PAG_INTEREST_GLITCH6))
                .setLocalPath(dealLocalPath(TransitionType.PAG_INTEREST_GLITCH6)),
            TransitionType.PAG_INTEREST_GLITCH7,
        )
    }

    /**
     * 初始化下载路径
     */
    fun initDownloadTransition() {
        transiInterst
        transiMg
        transiParticle
        transiGlitch
    }

    /**
     *转场集合
     */
    val transitionSets: List<FilterEntity> by lazy {
        listOf(
            FilterEntity(R.string.transitions_classic, "N", R.drawable.filter_thumb_art, -0x33d1d9dd, CLASSIC),
            FilterEntity(R.string.transitions_slide, "P", R.drawable.retro, -0x33ff6f01, SLIDE),
            FilterEntity(R.string.transitions_grid, "P", R.drawable.filter_thumb_people, -0x33ca4239, GRID),
            FilterEntity(R.string.transitions_graph, "C", R.drawable.filter_thumb_colorful, -0x330066b5, GRAPH),
            FilterEntity(R.string.transitions_rock, "F", R.drawable.filter_thumb_food, -0x33ca4239, ROCK),
            FilterEntity(R.string.transitions_interest, "F", R.drawable.filter_thumb_bw, -0x33ca4239, INTEREST),
            FilterEntity(R.string.transitions_mg, "F", R.drawable.filter_thumb_people, -0x33ca4239, MG),
            FilterEntity(R.string.transitions_particle, "F", R.drawable.filter_thumb_food, -0x33ca4239, PARTICLE),
            FilterEntity(R.string.transitions_glitch, "F", R.drawable.filter_thumb_food, -0x33ca4239, GLITCH),
        )
    }


    /**
     * 分好类的所有转场
     */
    val allTransitions = listOf(
        transiClassis,
        transiSlide,
        transiGrid,
        transiGraph,
        transiRock,
        transiInterst,
        transiMg,
        transiParticle,
        transiGlitch
    )

    /**
     * 获取转场类型
     */
    fun getTransitions(type: Int): List<TransitionType> {
        when (type) {
            CLASSIC -> {
                return transiClassis
            }
            SLIDE -> {
                return transiSlide
            }
            GRID -> {
                return transiGrid
            }
            GRAPH -> {
                return transiGraph
            }
            ROCK -> {
                return transiRock
            }
            INTEREST -> {
                return transiInterst
            }
            MG -> {
                return transiMg
            }
            PARTICLE -> {
                return transiParticle
            }
            GLITCH -> {
                return transiGlitch
            }

        }
        return transiClassis;
    }

    /**
     * 随机转场group
     */
    fun randomTranGroup(): Int {
        val ranValue = Random.nextInt(100)
        if (ranValue < 30) {
            return GROUP_BASE
        } else if (ranValue < 47) {
            return GROUP_INTEREST
        } else if (ranValue < 65) {
            return GROUP_MG
        } else if (ranValue < 82) {
            return GROUP_PARTICLE
        } else {
            return GROUP_GLITCH
        }
    }

    //base转场（原来传统转场）
    val baseTransitions: MutableList<TransitionType> = mutableListOf<TransitionType>().apply {
        addAll(transiClassis)
        addAll(transiSlide)
        addAll(transiGrid)
        addAll(transiGraph)
        addAll(transiRock)
    }

    /**
     * 检测是否已经下载
     */
    fun checkHadDown(transitionType: TransitionType): Boolean {
        if (PAGDownloadFilter::class.java.isAssignableFrom(transitionType.transitionClass)) {
            //检测下载
            if (FileUtils.checkFileExist(dealLocalPath(transitionType))) {
                return true
            }
            return false;
        }
        return true;
    }

    /**
     * 不停遍历直到获取到位置，这就要就list 必须有一个本地文件
     */
    fun getExistTranByGroup(list: List<TransitionType>): TransitionType {
        val transitionType = list.get(Random.nextInt(list.size))
        if (checkHadDown(transitionType)) {
            return transitionType;
        }
        return getExistTranByGroup(list);
    }

    /**
     * 随机产生transition
     * 第一张无转场，第二张转场不为3D
     *
     * @return
     */
    fun randomTransitionFilter(group: Int, index: Int): TransitionFilter? {
        if (index == 0 || group == TransiGroup.GROUP_NONE) {
            return null
        }
        val random = java.util.Random()
        var transitionType = TransitionType.FADE_IN;
        when (group) {
            GROUP_BASE -> {
                val seq = random.nextInt(baseTransitions.size);
                transitionType = baseTransitions.get(seq)
            }
            GROUP_INTEREST -> {
                transitionType = getExistTranByGroup(transiInterst)
            }
            GROUP_MG -> {
                transitionType = getExistTranByGroup(transiMg)
            }
            GROUP_PARTICLE -> {
                transitionType = getExistTranByGroup(transiParticle)
            }
            GROUP_GLITCH -> {
                transitionType = getExistTranByGroup(transiGlitch)
            }
        }
        return TransitionFactory.initFilters(transitionType)
    }


    @IntDef(CLASSIC, SLIDE, GRID, GRAPH, ROCK, INTEREST, MG, PARTICLE, GLITCH)
    @Retention(RetentionPolicy.SOURCE)
    annotation class TransiArray {
        companion object {
            //经典
            const val CLASSIC = 0

            //滑动
            const val SLIDE = 1

            //网格
            const val GRID = 2

            //图形
            const val GRAPH = 3

            //晃动
            const val ROCK = 4

            //酷炫
            const val INTEREST = 5

            //MG动画
            const val MG = 6

            //粒子光效
            const val PARTICLE = 7

            //毛刺
            const val GLITCH = 8
        }
    }


    @IntDef(GROUP_NONE, GROUP_BASE, GROUP_INTEREST, GROUP_MG, GROUP_PARTICLE, GROUP_GLITCH)
    @Retention(RetentionPolicy.SOURCE)
    annotation class TransiGroup {
        companion object {
            const val GROUP_NONE = -1

            //基础
            const val GROUP_BASE = 0

            //酷炫
            const val GROUP_INTEREST = 1

            //MG动画
            const val GROUP_MG = 2

            //粒子光效
            const val GROUP_PARTICLE = 3

            //毛刺
            const val GROUP_GLITCH = 4
        }
    }


    /**
     * 获取转场标签类别
     */
    fun getTransitionArray(transition: TransitionType) = getTransitionArray(transition.ordinal)

    /**
     * 获取转场标签类别
     */
    fun getTransitionArray(ordinal: Int) =
        if (ordinal > TransitionType.PAG_HALO9.ordinal) {
            GLITCH
        } else if (ordinal > TransitionType.PAG_GRAPH5.ordinal) {
            PARTICLE
        } else if (ordinal > TransitionType.PAG_PEN8.ordinal) {
            MG
        } else if (ordinal > TransitionType.SHADE_MOVE_SHUTTER.ordinal) {
            INTEREST
        } else if (ordinal > TransitionType.SHADE_SPLIT_RE.ordinal) {
            ROCK
        } else if (ordinal > TransitionType.SHADE_RANDOMBAR.ordinal) {
            GRAPH
        } else if (ordinal > TransitionType.MOVE_PUSH_B.ordinal) {
            GRID
        } else if (ordinal > TransitionType.WIPE_LEFT.ordinal) {
            SLIDE
        } else if (ordinal > TransitionType.NONE.ordinal) {
            CLASSIC
        } else {
            -1
        }

    /**
     * 获取类别转场起始位置
     */
    fun getTransitionStartOrdinal(transiArray: Int) = when (transiArray) {
        CLASSIC -> TransitionType.FADE_IN.ordinal
        SLIDE -> TransitionType.MOVE_LEFT.ordinal
        GRID -> TransitionType.SQUARE_TWINKLE.ordinal
        GRAPH -> TransitionType.CIRCLE_ZOOM.ordinal
        ROCK -> TransitionType.HAHA_MIRROR.ordinal
        INTEREST -> TransitionType.PAG_INTEREST_CAMERA1.ordinal
        MG -> TransitionType.PAG_CARTON1.ordinal
        PARTICLE -> TransitionType.PAG_BOOM1.ordinal
        GLITCH -> TransitionType.PAG_INTEREST_GLITCH1.ordinal
        else -> if (transiArray < CLASSIC) {
            0
        } else {
            TransitionType.PAG_INTEREST_GLITCH7.ordinal + 1
        }
    }


    /**
     * mix转场数量
     */
    const val SERIES_GROUP_COUNT = TransitionSeries.MIX_TRANSITION_COUNT



}


