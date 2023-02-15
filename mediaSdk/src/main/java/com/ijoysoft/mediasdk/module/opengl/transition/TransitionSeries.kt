package com.ijoysoft.mediasdk.module.opengl.transition

import com.ijoysoft.mediasdk.R

/**
 *
 *
 * 转场系列
 * 多个转场的组合
 * @date 2022/2/16  11:38
 * @author hayring
 * @param index 序号， 单个的序号与多个的序号独立
 * @param nameId 名称对应的string资源id
 * @param iconId 图标对应的drawable资源id
 * @param array 转场组合数组
 */
enum class TransitionSeries(val index: Int, val nameId: Int, val iconId: Int, val array: Array<TransitionType?>) {


    /**
     * array 中 使用NONE以便清除转场
     */
    NONE(TransitionType.NONE),


    //负的是组合
//    MOVING(-1, R.string.array_move_top, R.mipmap.move_top, arrayOf(TransitionType.MOVE_LEFT,
//        TransitionType.MOVE_TOP, TransitionType.MOVE_RIGHT, TransitionType.MOVE_BOTTOM)),

    SERIES1(-1, R.string.array_group_1, R.mipmap.shade_square_re, arrayOf(
        TransitionType.SHADE_SQUARE_RE, TransitionType.TRIANGLE, TransitionType.MOVE_LEFT, TransitionType.MOVE_RIGHT, TransitionType.MOVE_TOP, TransitionType.MOVE_BOTTOM, TransitionType.SHADE_PORTRAIT,
        TransitionType.SHADE_LANDSCAPE, TransitionType.SQUARE_TWINKLE, TransitionType.SHADE_SQUARE
    )),
    SERIES2(-2, R.string.array_group_2, R.mipmap.move_push_b, arrayOf(
        TransitionType.MOVE_PUSH_B, TransitionType.MOVE_PUSH_L, TransitionType.MOVE_PUSH_T, TransitionType.MOVE_PUSH_R, TransitionType.THUMB_MOVE, TransitionType.SQUARE_TWINKLE,
        TransitionType.SQUARE_3D, TransitionType.TRIANGLE, TransitionType.HEART_SHAPE, TransitionType.ROTATE_AXIS_Y
    )),
    SERIES3(-3, R.string.array_group_3, R.mipmap.circlerotate, arrayOf(
        TransitionType.CIRCLE_ROTATE, TransitionType.WHOLE_ZOOM, TransitionType.OVAL_ZOOM, TransitionType.BLUR, TransitionType.SHAKE, TransitionType.SHADE_SPLIT, TransitionType.SHADE_SPLIT_RE,
        TransitionType.SHADE_PLUS, TransitionType.SHADE_PLUS_RE, TransitionType.SHADE_RANDOMBAR
    )),
    SERIES4(-4, R.string.array_group_4, R.mipmap.shade_split, arrayOf(
        TransitionType.SHADE_SPLIT, TransitionType.MOVE_PUSH_T, TransitionType.MOVE_PUSH_T, TransitionType.MOVE_PUSH_R, TransitionType.ROTATE_AXIS_Y, TransitionType.MOVE_PUSH_B, TransitionType.MOVE_PUSH_B,
        TransitionType.MOVE_PUSH_L, TransitionType.MOVE_PUSH_L, TransitionType.SHADE_SPLIT_RE
    )),
    SERIES5(-5, R.string.array_group_5, R.mipmap.fade, arrayOf(
        TransitionType.FADE_IN, TransitionType.WIPE_LEFT, TransitionType.OVAL_ZOOM, TransitionType.HAHA_MIRROR, TransitionType.WHOLE_ZOOM, TransitionType.SHADE_PLUS, TransitionType.WIPE_LEFT,
        TransitionType.WAVE, TransitionType.SQUARE_TWINKLE, TransitionType.SHADE_PLUS_RE
    )),

    /*INTEREST*/
    SERIES6(-6, R.string.array_group_6, R.mipmap.tran_pag_camera1, arrayOf(
        TransitionType.PAG_INTEREST_CAMERA1, TransitionType.PAG_INTEREST_CAMERA2, TransitionType.PAG_INTEREST_PAPER1
    )),

    /*MG*/
    SERIES7(-7, R.string.array_group_7, R.mipmap.tran_pag_carton1, arrayOf(
        TransitionType.PAG_CARTON1, TransitionType.PAG_CARTON2, TransitionType.PAG_CARTON3, TransitionType.PAG_CARTON4,

        )),
    SERIES8(-8, R.string.array_group_8, R.mipmap.tran_pag_carton5, arrayOf(
        TransitionType.PAG_CARTON5, TransitionType.PAG_CARTON6, TransitionType.PAG_CARTON9
    )),
    SERIES9(-9, R.string.array_group_9, R.mipmap.tran_pag_carton7, arrayOf(
        TransitionType.PAG_CARTON7, TransitionType.PAG_CARTON8, TransitionType.PAG_CARTON10, TransitionType.PAG_CARTON11
    )),

    /*PARTICLE*/
    SERIES10(-10, R.string.array_group_10, R.mipmap.tran_pag_boom2, arrayOf(
        TransitionType.PAG_BOOM2, TransitionType.PAG_BOOM3, TransitionType.PAG_BOOM4
    )),

    SERIES11(-11, R.string.array_group_11, R.mipmap.tran_pag_leaf2, arrayOf(
        TransitionType.PAG_INTEREST_LEAF2, TransitionType.PAG_INTEREST_LEAF3, TransitionType.PAG_INTEREST_LEAF4, TransitionType.PAG_INTEREST_LEAF5
    )),

    SERIES12(-12, R.string.array_group_11, R.mipmap.tran_pag_boom3, arrayOf(
        TransitionType.PAG_BOOM3, TransitionType.PAG_BOOM4, TransitionType.PAG_HALO1, TransitionType.PAG_HALO4, TransitionType.PAG_HALO8
    )),

    SERIES13(-13, R.string.array_group_13, R.mipmap.tran_pag_glitch1, arrayOf(
        TransitionType.PAG_INTEREST_GLITCH1, TransitionType.PAG_INTEREST_GLITCH2, TransitionType.PAG_INTEREST_GLITCH3
        , TransitionType.PAG_INTEREST_GLITCH4, TransitionType.PAG_INTEREST_GLITCH5
    )),

    SERIES14(-14, R.string.array_group_14, R.mipmap.tran_pag_flow1, arrayOf(
        TransitionType.PAG_FLOW1, TransitionType.PAG_FLOW2, TransitionType.PAG_MG1, TransitionType.PAG_MG5
    )),

    SERIES15(-15, R.string.array_group_15, R.mipmap.tran_pag_paper2, arrayOf(
        TransitionType.PAG_INTEREST_PAPER2, TransitionType.PAG_INTEREST_PAPER3, TransitionType.PAG_INTEREST_PAPER4,
     TransitionType.PAG_INTEREST_PAPER1
    )),

    //正的是单个
    /*classis*/
    FADE_IN(TransitionType.FADE_IN),
    BLUR(TransitionType.BLUR),
    FLASH_WHITE(TransitionType.FLASH_WHITE),
    FLASH_BLACK(TransitionType.FLASH_BLACK),
    SHADE_DISSOLVE(TransitionType.SHADE_DISSOLVE),
    CIRCLE_ROTATE(TransitionType.CIRCLE_ROTATE),
    READ_BOOK(TransitionType.READ_BOOK),
    TRIANGLE(TransitionType.TRIANGLE),
    SHADE_REVEAL(TransitionType.SHADE_REVEAL),
    SHADE_REVEAL_RE(TransitionType.SHADE_REVEAL_RE),
    SHADE_TWO_LINE_CLOCK(TransitionType.SHADE_TWO_LINE_CLOCK),
    WIPE_LEFT(TransitionType.WIPE_LEFT),



    /*slide*/

    MOVE_LEFT(TransitionType.MOVE_LEFT),
    MOVE_RIGHT(TransitionType.MOVE_RIGHT),
    MOVE_TOP(TransitionType.MOVE_TOP),
    MOVE_BOTTOM(TransitionType.MOVE_BOTTOM),
    MOVE_PUSH_L(TransitionType.MOVE_PUSH_L),
    MOVE_PUSH_R(TransitionType.MOVE_PUSH_R),
    MOVE_PUSH_T(TransitionType.MOVE_PUSH_T),
    MOVE_PUSH_B(TransitionType.MOVE_PUSH_B),


    /*grid*/
    SQUARE_TWINKLE(TransitionType.SQUARE_TWINKLE),
    SHADE_PORTRAIT(TransitionType.SHADE_PORTRAIT),
    SHADE_LANDSCAPE(TransitionType.SHADE_LANDSCAPE),
    GRID_SHOP(TransitionType.GRID_SHOP),
    SHADE_BAR_CROSS(TransitionType.SHADE_BAR_CROSS),
    SHADE_BAR_CROSS_Y(TransitionType.SHADE_BAR_CROSS_Y),
    SHADE_FOUR_CORNER(TransitionType.SHADE_FOUR_CORNER),
    SHADE_FOUR_CORNER_RE(TransitionType.SHADE_FOUR_CORNER_RE),
    SHADE_MULTI_SQUARE(TransitionType.SHADE_MULTI_SQUARE),
    SHADE_MULTI_SQUARE_RE(TransitionType.SHADE_MULTI_SQUARE_RE),
    SHADE_MULTI_TRIANGLE(TransitionType.SHADE_MULTI_TRIANGLE),
    SHADE_MULTI_DIAMOND(TransitionType.SHADE_MULTI_DIAMOND),
    SHADE_MULTI_DIAMOND_RE(TransitionType.SHADE_MULTI_DIAMOND_RE),
    SHADE_RANDOMBAR(TransitionType.SHADE_RANDOMBAR),


    /*Graph*/
    CIRCLE_ZOOM(TransitionType.CIRCLE_ZOOM),
    OVAL_ZOOM(TransitionType.OVAL_ZOOM),
    HEART_SHAPE(TransitionType.HEART_SHAPE),
    WHOLE_ZOOM(TransitionType.WHOLE_ZOOM),
    SHADE_PLUS(TransitionType.SHADE_PLUS),
    SHADE_PLUS_RE(TransitionType.SHADE_PLUS_RE),
    SHADE_DIAMOND(TransitionType.SHADE_DIAMOND),
    SHADE_DIAMOND_RE(TransitionType.SHADE_DIAMOND_RE),
    SHADE_SQUARE(TransitionType.SHADE_SQUARE),
    SHADE_SQUARE_RE(TransitionType.SHADE_SQUARE_RE),
    SHADE_SPLIT(TransitionType.SHADE_SPLIT),
    SHADE_SPLIT_RE(TransitionType.SHADE_SPLIT_RE),

    /*rock*/
    HAHA_MIRROR(TransitionType.HAHA_MIRROR),
    SHAKE(TransitionType.SHAKE),
    SQUARE_3D(TransitionType.SQUARE_3D),
    WAVE(TransitionType.WAVE),
    THUMB_MOVE(TransitionType.THUMB_MOVE),
     ROTATE_AXIS_Y(TransitionType.ROTATE_AXIS_Y),
    SHADE_MOVE_SHUTTER(TransitionType.SHADE_MOVE_SHUTTER),


    /*PAG interest*/
    PAG_INTEREST_CAMERA1(TransitionType.PAG_INTEREST_CAMERA1),
    PAG_INTEREST_CAMERA2(TransitionType.PAG_INTEREST_CAMERA2),
    PAG_INTEREST_PAPER1(TransitionType.PAG_INTEREST_PAPER1),
    PAG_INTEREST_PAPER2(TransitionType.PAG_INTEREST_PAPER2),
    PAG_INTEREST_PAPER3(TransitionType.PAG_INTEREST_PAPER3),
    PAG_INTEREST_PAPER4(TransitionType.PAG_INTEREST_PAPER4),
    PAG_INTEREST_LEAF1(TransitionType.PAG_INTEREST_LEAF1),
    PAG_INTEREST_LEAF2(TransitionType.PAG_INTEREST_LEAF2),
    PAG_INTEREST_LEAF3(TransitionType.PAG_INTEREST_LEAF3),
    PAG_INTEREST_LEAF4(TransitionType.PAG_INTEREST_LEAF4),
    PAG_INTEREST_LEAF5(TransitionType.PAG_INTEREST_LEAF5),
    PAG_INTEREST_FACE1(TransitionType.PAG_INTEREST_FACE1),
    /*interest_PEN*/
    PAG_PEN1(TransitionType.PAG_PEN1),
    PAG_PEN2(TransitionType.PAG_PEN2),
    PAG_PEN3(TransitionType.PAG_PEN3),
    PAG_PEN4(TransitionType.PAG_PEN4),
    PAG_PEN5(TransitionType.PAG_PEN5),
    PAG_PEN6(TransitionType.PAG_PEN6),
    PAG_PEN7(TransitionType.PAG_PEN7),
    PAG_PEN8(TransitionType.PAG_PEN8),



    /*PAG MG系列*/
    PAG_CARTON1(TransitionType.PAG_CARTON1),
    PAG_CARTON2(TransitionType.PAG_CARTON2),
    PAG_CARTON3(TransitionType.PAG_CARTON3),
    PAG_CARTON4(TransitionType.PAG_CARTON4),
    PAG_CARTON5(TransitionType.PAG_CARTON5),
    PAG_CARTON6(TransitionType.PAG_CARTON6),
    PAG_CARTON7(TransitionType.PAG_CARTON7),
    PAG_CARTON8(TransitionType.PAG_CARTON8),
    PAG_CARTON9(TransitionType.PAG_CARTON9),
    PAG_CARTON10(TransitionType.PAG_CARTON10),
    PAG_CARTON11(TransitionType.PAG_CARTON11),
    PAG_FLOW1(TransitionType.PAG_FLOW1),
    PAG_FLOW2(TransitionType.PAG_FLOW2),
    PAG_MG1(TransitionType.PAG_MG1),
    PAG_MG2(TransitionType.PAG_MG2),
    PAG_MG3(TransitionType.PAG_MG3),
    PAG_MG4(TransitionType.PAG_MG4),
    PAG_MG5(TransitionType.PAG_MG5),
    PAG_MG6(TransitionType.PAG_MG6),
    PAG_GRAPH1(TransitionType.PAG_GRAPH1),
    PAG_GRAPH2(TransitionType.PAG_GRAPH2),
    PAG_GRAPH3(TransitionType.PAG_GRAPH3),
    PAG_GRAPH4(TransitionType.PAG_GRAPH4),
    PAG_GRAPH5(TransitionType.PAG_GRAPH5),


    /*PAG particle*/
    PAG_BOOM1(TransitionType.PAG_BOOM1),
    PAG_BOOM2(TransitionType.PAG_BOOM2),
    PAG_BOOM3(TransitionType.PAG_BOOM3),
    PAG_BOOM4(TransitionType.PAG_BOOM4),
    PAG_BOOM5(TransitionType.PAG_BOOM5),
    PAG_COLOR_HEART(TransitionType.PAG_COLOR_HEART),
    PAG_PARTICLE1(TransitionType.PAG_PARTICLE1),
    /* particle_halo*/
    PAG_HALO1(TransitionType.PAG_HALO1),
    PAG_HALO2(TransitionType.PAG_HALO2),
    PAG_HALO3(TransitionType.PAG_HALO3),
    PAG_HALO4(TransitionType.PAG_HALO4),
    PAG_HALO5(TransitionType.PAG_HALO5),
    PAG_HALO6(TransitionType.PAG_HALO6),
    PAG_HALO7(TransitionType.PAG_HALO7),
    PAG_HALO8(TransitionType.PAG_HALO8),
    PAG_HALO9(TransitionType.PAG_HALO9),



    /*glitch毛刺*/
    PAG_INTEREST_GLITCH1(TransitionType.PAG_INTEREST_GLITCH1),
    PAG_INTEREST_GLITCH2(TransitionType.PAG_INTEREST_GLITCH2),
    PAG_INTEREST_GLITCH3(TransitionType.PAG_INTEREST_GLITCH3),
    PAG_INTEREST_GLITCH4(TransitionType.PAG_INTEREST_GLITCH4),
    PAG_INTEREST_GLITCH5(TransitionType.PAG_INTEREST_GLITCH5),
    PAG_INTEREST_GLITCH6(TransitionType.PAG_INTEREST_GLITCH6),
    PAG_INTEREST_GLITCH7(TransitionType.PAG_INTEREST_GLITCH7),






    //枚举结束分号
    ;




    companion object {
        /**
         * 组合的个数
         */
        const val MIX_TRANSITION_COUNT = 15

        @JvmStatic
        fun getTransitionSeries(transitionType: TransitionType) = valueOf(transitionType.name)
    }


    /**
     * 单个转场直接引用
     */
    constructor(type: TransitionType): this(type.index + MIX_TRANSITION_COUNT, type.nameid, type.iconId, arrayOf(type))


}