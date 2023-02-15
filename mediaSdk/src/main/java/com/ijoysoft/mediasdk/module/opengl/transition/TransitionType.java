package com.ijoysoft.mediasdk.module.opengl.transition;

import com.ijoysoft.mediasdk.R;

import java.util.Random;

/**
 * 转场类型
 */
public enum TransitionType {

    NONE(0, R.string.array_none, 0, TransitionFilter.class),
    /*classis*/
    FADE_IN(NONE.index + 1, R.string.array_fade, R.mipmap.fade, FadeTransitionFilter.class),
    BLUR(FADE_IN.index + 1, R.string.array_blur, R.mipmap.blur, BlurTransitionFilter.class),
    FLASH_WHITE(BLUR.index + 1, R.string.array_flashwhite, R.mipmap.flashwhite, FlashColorTransitionFilter.class),
    FLASH_BLACK(FLASH_WHITE.index + 1, R.string.array_flashblack, R.mipmap.flashblack, FlashColorTransitionFilter.class),
    SHADE_DISSOLVE(FLASH_BLACK.index + 1, R.string.array_shade_dissolve, R.mipmap.shade_dissolve, ShadeDissolveTransitionFilter.class),
    CIRCLE_ROTATE(SHADE_DISSOLVE.index + 1, R.string.array_circlerotate, R.mipmap.circlerotate, CircleRotateTransitionFilter.class),
    READ_BOOK(CIRCLE_ROTATE.index + 1, R.string.array_readbook, R.mipmap.readbook, ReadBookTransitionFilter.class),
    TRIANGLE(READ_BOOK.index + 1, R.string.array_triangle, R.mipmap.triangle, TriangleTransitionFilter.class),
    SHADE_REVEAL(TRIANGLE.index + 1, R.string.array_reveal, R.mipmap.shade_reveal, ShadeRevealTransitionFilter.class),
    SHADE_REVEAL_RE(SHADE_REVEAL.index + 1, R.string.array_reveal_re, R.mipmap.shade_reveal_re, ShadeRevealTransitionFilter.class),
    SHADE_TWO_LINE_CLOCK(SHADE_REVEAL_RE.index + 1, R.string.array_two_clock, R.mipmap.shade_two_clock, ShadeTwoColockTransitionFilter.class),
    WIPE_LEFT(SHADE_TWO_LINE_CLOCK.index + 1, R.string.array_wipe, R.mipmap.wipe, WipeTransitionFilter.class),

    /*slide*/
    MOVE_LEFT(WIPE_LEFT.index + 1, R.string.array_move_left, R.mipmap.move_left, MoveTransitionFilter.class),
    MOVE_RIGHT(MOVE_LEFT.index + 1, R.string.array_move_right, R.mipmap.move_right, MoveTransitionFilter.class),
    MOVE_TOP(MOVE_RIGHT.index + 1, R.string.array_move_top, R.mipmap.move_top, MoveTransitionFilter.class),
    MOVE_BOTTOM(MOVE_TOP.index + 1, R.string.array_move_bottom, R.mipmap.move_bottom, MoveTransitionFilter.class),
    MOVE_PUSH_L(MOVE_BOTTOM.index + 1, R.string.array_push_l, R.mipmap.move_push_l, PushTransitionFilter.class),
    MOVE_PUSH_R(MOVE_PUSH_L.index + 1, R.string.array_push_r, R.mipmap.move_push_r, PushTransitionFilter.class),
    MOVE_PUSH_T(MOVE_PUSH_R.index + 1, R.string.array_push_t, R.mipmap.move_push_t, PushTransitionFilter.class),
    MOVE_PUSH_B(MOVE_PUSH_T.index + 1, R.string.array_push_b, R.mipmap.move_push_b, PushTransitionFilter.class),

    /*grid*/
    SQUARE_TWINKLE(MOVE_PUSH_B.index + 1, R.string.array_square_twinkle, R.mipmap.square_twinkle, SquareTwinkleTransitionFilter.class),
    SHADE_PORTRAIT(SQUARE_TWINKLE.index + 1, R.string.array_shutterv, R.mipmap.shutter_horizontal, WindowShadeVTTransitionFilter.class),
    SHADE_LANDSCAPE(SHADE_PORTRAIT.index + 1, R.string.array_shutterh, R.mipmap.shutter_vertical, WindowShadeHZTransitionFilter.class),
    GRID_SHOP(SHADE_LANDSCAPE.index + 1, R.string.array_gridshop, R.mipmap.gridshop, GridShopTransitionFilter.class),
    SHADE_BAR_CROSS(GRID_SHOP.index + 1, R.string.array_bar_croass, R.mipmap.shade_bar_croass, ShadeBarCrossTransitionFilter.class),
    SHADE_BAR_CROSS_Y(SHADE_BAR_CROSS.index + 1, R.string.array_bar_croass_re, R.mipmap.shade_bar_croass_y, ShadeBarCrossYAxisTransitionFilter.class),
    SHADE_FOUR_CORNER(SHADE_BAR_CROSS_Y.index + 1, R.string.array_four_corner, R.mipmap.shade_four_corner, ShadeFourCornerTransitionFilter.class),
    SHADE_FOUR_CORNER_RE(SHADE_FOUR_CORNER.index + 1, R.string.array_four_corner_re, R.mipmap.shade_four_corner_re, ShadeFourCornerReTransitionFilter.class),
    SHADE_MULTI_SQUARE(SHADE_FOUR_CORNER_RE.index + 1, R.string.array_multi_square, R.mipmap.shade_multi_square, ShadeMultiSquareTransitionFilter.class),
    SHADE_MULTI_SQUARE_RE(SHADE_MULTI_SQUARE.index + 1, R.string.array_multi_square_re, R.mipmap.shade_multi_square_re, ShadeMultiSquareTransitionFilter.class),
    SHADE_MULTI_TRIANGLE(SHADE_MULTI_SQUARE_RE.index + 1, R.string.array_multi_triangle, R.mipmap.shade_multi_triangle, ShadeMultiTriangleTransitionFilter.class),
    SHADE_MULTI_DIAMOND(SHADE_MULTI_TRIANGLE.index + 1, R.string.array_multi_diamond, R.mipmap.shade_multi_diamond, ShadeMultiDiamondTransitionFilter.class),
    SHADE_MULTI_DIAMOND_RE(SHADE_MULTI_DIAMOND.index + 1, R.string.array_multi_diamond_re, R.mipmap.shade_multi_diamond_re, ShadeMultiDiamondTransitionFilter.class),
    SHADE_RANDOMBAR(SHADE_MULTI_DIAMOND_RE.index + 1, R.string.array_randombar, R.mipmap.shade_randombar, ShadeRandBarTransitionFilter.class),

    /*Graph*/
    CIRCLE_ZOOM(SHADE_RANDOMBAR.index + 1, R.string.array_circlerzoom, R.mipmap.circlerzoom, CircleZoomTransitionFilter.class),
    OVAL_ZOOM(CIRCLE_ZOOM.index + 1, R.string.array_ovalzoom, R.mipmap.ovalzoom, OvalZoomTransitionFilter.class),
    HEART_SHAPE(OVAL_ZOOM.index + 1, R.string.array_heart, R.mipmap.heart, HeartTransitionFilter.class),
    WHOLE_ZOOM(HEART_SHAPE.index + 1, R.string.array_wholezoom, R.mipmap.wholezoom, WholeZoomTransitionFilter.class),
    SHADE_PLUS(WHOLE_ZOOM.index + 1, R.string.array_shade_plus, R.mipmap.shade_plus, ShadePlusTransitionFilter.class),
    SHADE_PLUS_RE(SHADE_PLUS.index + 1, R.string.array_shade_plus_re, R.mipmap.shade_plus_re, ShadePlusTransitionFilter.class),
    SHADE_DIAMOND(SHADE_PLUS_RE.index + 1, R.string.array_shade_diamond, R.mipmap.shade_diamond, ShadeDiamondTransitionFilter.class),
    SHADE_DIAMOND_RE(SHADE_DIAMOND.index + 1, R.string.array_shade_diamond_re, R.mipmap.shade_diamond_re, ShadeDiamondTransitionFilter.class),
    SHADE_SQUARE(SHADE_DIAMOND_RE.index + 1, R.string.array_shade_square, R.mipmap.shade_square, ShadeSquareTransitionFilter.class),
    SHADE_SQUARE_RE(SHADE_SQUARE.index + 1, R.string.array_shade_square_re, R.mipmap.shade_square_re, ShadeSquareTransitionFilter.class),
    SHADE_SPLIT(SHADE_SQUARE_RE.index + 1, R.string.array_split, R.mipmap.shade_split, ShadeSplitTransitionFilter.class),
    SHADE_SPLIT_RE(SHADE_SPLIT.index + 1, R.string.array_split_re, R.mipmap.shade_split_re, ShadeSplitTransitionFilter.class),

    /*rock*/
    HAHA_MIRROR(SHADE_SPLIT_RE.index + 1, R.string.array_hahamirror, R.mipmap.hahamirror, HaHaMirrorTransitionFilter.class),
    SHAKE(HAHA_MIRROR.index + 1, R.string.array_shake, R.mipmap.shake, ShakeTransitionFilter.class),
    SQUARE_3D(SHAKE.index + 1, R.string.array_3D, R.mipmap.square_3d, Square3DTransitionFilter.class),
    WAVE(SQUARE_3D.index + 1, R.string.array_wave, R.mipmap.wave, WaveTransitionFilter.class),
    THUMB_MOVE(WAVE.index + 1, R.string.array_thumb_move, R.mipmap.thumbmove, ThumbMixMoveTransitionFilter.class),
    ROTATE_AXIS_Y(THUMB_MOVE.index + 1, R.string.array_axis_y, R.mipmap.rotate_axis_y, RotateAxisYTransitionFilter.class),
    SHADE_MOVE_SHUTTER(ROTATE_AXIS_Y.index + 1, R.string.array_shutter_move, R.mipmap.shade_shutter_move, ShadeShutterMoveTransitionFilter.class),


    /*PAG interest*/
    PAG_INTEREST_CAMERA1(SHADE_MOVE_SHUTTER.index + 1, R.string.array_pag_camera1, R.mipmap.tran_pag_camera1, "pag_tran/camera1.pag", PAGLocalTransitionFilter.class),
    PAG_INTEREST_CAMERA2(PAG_INTEREST_CAMERA1.index + 1, R.string.array_pag_camera2, R.mipmap.tran_pag_camera2, "pag_tran/camera2.pag", PAGLocalTwoTransitionFilter.class, true),
    PAG_INTEREST_PAPER1(PAG_INTEREST_CAMERA2.index + 1, R.string.array_transition_page1, R.mipmap.tran_pag_paper1, "pag_tran/paper1.pag", PAGLocalTwoTransitionFilter.class),
    PAG_INTEREST_PAPER2(PAG_INTEREST_PAPER1.index + 1, R.string.array_transition_page2, R.mipmap.tran_pag_paper2, PAGDownTwoTransitionFilter.class, "transitions/interest/paper2"),
    PAG_INTEREST_PAPER3(PAG_INTEREST_PAPER2.index + 1, R.string.array_transition_page3, R.mipmap.tran_pag_paper3, PAGDownTwoTransitionFilter.class, "transitions/interest/paper3"),
    PAG_INTEREST_PAPER4(PAG_INTEREST_PAPER3.index + 1, R.string.array_transition_page4, R.mipmap.tran_pag_paper4, PAGDownTwoTransitionFilter.class, "transitions/interest/paper4"),
    PAG_INTEREST_LEAF1(PAG_INTEREST_PAPER4.index + 1, R.string.array_transition_leaf1, R.mipmap.tran_pag_leaf1, PAGDownloadFilter.class, "transitions/interest/leaf1"),
    PAG_INTEREST_LEAF2(PAG_INTEREST_LEAF1.index + 1, R.string.array_transition_leaf2, R.mipmap.tran_pag_leaf2, PAGDownloadFilter.class, "transitions/interest/leaf2"),
    PAG_INTEREST_LEAF3(PAG_INTEREST_LEAF2.index + 1, R.string.array_transition_leaf3, R.mipmap.tran_pag_leaf3, PAGDownloadFilter.class, "transitions/interest/leaf3_1"),
    PAG_INTEREST_LEAF4(PAG_INTEREST_LEAF3.index + 1, R.string.array_transition_leaf4, R.mipmap.tran_pag_leaf4, PAGDownloadFilter.class, "transitions/interest/leaf4_1"),
    PAG_INTEREST_LEAF5(PAG_INTEREST_LEAF4.index + 1, R.string.array_transition_leaf5, R.mipmap.tran_pag_leaf5, PAGDownloadFilter.class, "transitions/interest/leaf5"),
    PAG_INTEREST_FACE1(PAG_INTEREST_LEAF5.index + 1, R.string.array_transition_face1, R.mipmap.tran_pag_face1, PAGDownloadFilter.class, "transitions/interest/face1"),
    /*interest_PEN*/
    PAG_PEN1(PAG_INTEREST_FACE1.index + 1, R.string.array_transition_pen1, R.mipmap.tran_pag_pen1, PAGDownTwoTransitionFilter.class, "transitions/interest/pen1"),
    PAG_PEN2(PAG_PEN1.index + 1, R.string.array_transition_pen2, R.mipmap.tran_pag_pen2, PAGDownTwoTransitionFilter.class, "transitions/interest/pen2"),
    PAG_PEN3(PAG_PEN2.index + 1, R.string.array_transition_pen3, R.mipmap.tran_pag_pen3, PAGDownTwoTransitionFilter.class, "transitions/interest/pen3"),
    PAG_PEN4(PAG_PEN3.index + 1, R.string.array_transition_pen4, R.mipmap.tran_pag_pen4, PAGDownTwoTransitionFilter.class, "transitions/interest/pen4"),
    PAG_PEN5(PAG_PEN4.index + 1, R.string.array_transition_pen5, R.mipmap.tran_pag_pen5, PAGDownTwoTransitionFilter.class, "transitions/interest/pen5"),
    PAG_PEN6(PAG_PEN5.index + 1, R.string.array_transition_pen6, R.mipmap.tran_pag_pen6, PAGDownTwoTransitionFilter.class, "transitions/interest/pen6"),
    PAG_PEN7(PAG_PEN6.index + 1, R.string.array_transition_pen7, R.mipmap.tran_pag_pen7, PAGDownTwoTransitionFilter.class, "transitions/interest/pen7_1"),
    PAG_PEN8(PAG_PEN7.index + 1, R.string.array_transition_pen8, R.mipmap.tran_pag_pen8, PAGDownTwoTransitionFilter.class, "transitions/interest/pen8"),

    /*PAG MG系列*/
    PAG_CARTON1(PAG_PEN8.index + 1, R.string.array_transition_carton1, R.mipmap.tran_pag_carton1, "pag_tran/carton1.pag", PAGLocalTwoTransitionFilter.class),
    PAG_CARTON2(PAG_CARTON1.index + 1, R.string.array_transition_carton2, R.mipmap.tran_pag_carton2, "pag_tran/carton2.pag", PAGLocalTwoTransitionFilter.class),
    PAG_CARTON3(PAG_CARTON2.index + 1, R.string.array_transition_carton3, R.mipmap.tran_pag_carton3, "pag_tran/carton3.pag", PAGLocalTwoTransitionFilter.class),
    PAG_CARTON4(PAG_CARTON3.index + 1, R.string.array_transition_carton4, R.mipmap.tran_pag_carton4, "pag_tran/carton4.pag", PAGLocalTwoTransitionFilter.class),
    PAG_CARTON5(PAG_CARTON4.index + 1, R.string.array_transition_carton5, R.mipmap.tran_pag_carton5, "pag_tran/carton5.pag", PAGLocalTwoTransitionFilter.class),
    PAG_CARTON6(PAG_CARTON5.index + 1, R.string.array_transition_carton6, R.mipmap.tran_pag_carton6, "pag_tran/carton6.pag", PAGLocalTwoTransitionFilter.class),
    PAG_CARTON7(PAG_CARTON6.index + 1, R.string.array_transition_carton7, R.mipmap.tran_pag_carton7, "pag_tran/carton7.pag", PAGLocalTwoTransitionFilter.class),
    PAG_CARTON8(PAG_CARTON7.index + 1, R.string.array_transition_carton8, R.mipmap.tran_pag_carton8, "pag_tran/carton8.pag", PAGLocalTwoTransitionFilter.class),
    PAG_CARTON9(PAG_CARTON8.index + 1, R.string.array_transition_carton9, R.mipmap.tran_pag_carton9, "pag_tran/carton9.pag", PAGLocalTwoTransitionFilter.class),
    PAG_CARTON10(PAG_CARTON9.index + 1, R.string.array_transition_carton10, R.mipmap.tran_pag_carton10, "pag_tran/carton10.pag", PAGLocalTwoTransitionFilter.class),
    PAG_CARTON11(PAG_CARTON10.index + 1, R.string.array_transition_carton11, R.mipmap.tran_pag_carton11, "pag_tran/carton11.pag", PAGLocalTwoTransitionFilter.class),
    PAG_FLOW1(PAG_CARTON11.index + 1, R.string.array_transition_flow1, R.mipmap.tran_pag_flow1, PAGDownTwoTransitionFilter.class, "transitions/mg/flow1"),
    PAG_FLOW2(PAG_FLOW1.index + 1, R.string.array_transition_flow2, R.mipmap.tran_pag_flow2, PAGDownTwoTransitionFilter.class, "transitions/mg/flow2_1"),
    PAG_MG1(PAG_FLOW2.index + 1, R.string.array_transition_mg1, R.mipmap.tran_pag_mg1, PAGDownTwoTransitionFilter.class, "transitions/mg/mg1_1"),
    PAG_MG2(PAG_MG1.index + 1, R.string.array_transition_mg2, R.mipmap.tran_pag_mg2, PAGDownTwoTransitionFilter.class, "transitions/mg/mg2"),
    PAG_MG3(PAG_MG2.index + 1, R.string.array_transition_mg3, R.mipmap.tran_pag_mg3, PAGDownloadFilter.class, "transitions/mg/mg3"),
    PAG_MG4(PAG_MG3.index + 1, R.string.array_transition_mg4, R.mipmap.tran_pag_mg4, PAGDownloadFilter.class, "transitions/mg/mg4"),
    PAG_MG5(PAG_MG4.index + 1, R.string.array_transition_mg5, R.mipmap.tran_pag_mg5, PAGDownloadFilter.class, "transitions/mg/mg5"),
    PAG_MG6(PAG_MG5.index + 1, R.string.array_transition_mg6, R.mipmap.tran_pag_mg6, PAGDownloadFilter.class, "transitions/mg/mg6"),
    PAG_GRAPH1(PAG_MG6.index + 1, R.string.array_transition_graph1, R.mipmap.tran_pag_graph1, PAGDownTwoTransitionFilter.class, "transitions/mg/graph1"),
    PAG_GRAPH2(PAG_GRAPH1.index + 1, R.string.array_transition_graph2, R.mipmap.tran_pag_graph2, PAGDownTwoTransitionFilter.class, "transitions/mg/graph2"),
    PAG_GRAPH3(PAG_GRAPH2.index + 1, R.string.array_transition_graph3, R.mipmap.tran_pag_graph3, PAGDownTwoTransitionFilter.class, "transitions/mg/graph3"),
    PAG_GRAPH4(PAG_GRAPH3.index + 1, R.string.array_transition_graph4, R.mipmap.tran_pag_graph4, PAGDownTwoTransitionFilter.class, "transitions/mg/graph4"),
    PAG_GRAPH5(PAG_GRAPH4.index + 1, R.string.array_transition_graph5, R.mipmap.tran_pag_graph5, PAGDownTwoTransitionFilter.class, "transitions/mg/graph5"),


    /*PAG particle*/
    PAG_BOOM1(PAG_PEN8.index + 1, R.string.array_transition_boom1, R.mipmap.tran_pag_boom1, PAGDownTwoTransitionFilter.class, "transitions/particle/boom1_1"),
    PAG_BOOM2(PAG_BOOM1.index + 1, R.string.array_transition_boom2, R.mipmap.tran_pag_boom2, PAGDownTwoTransitionFilter.class, "transitions/particle/boom2_1"),
    PAG_BOOM3(PAG_BOOM2.index + 1, R.string.array_transition_boom3, R.mipmap.tran_pag_boom3, PAGDownTwoTransitionFilter.class, "transitions/particle/boom3_1"),
    PAG_BOOM4(PAG_BOOM3.index + 1, R.string.array_transition_boom4, R.mipmap.tran_pag_boom4, PAGDownTwoTransitionFilter.class, "transitions/particle/boom4_1"),
    PAG_BOOM5(PAG_BOOM4.index + 1, R.string.array_transition_boom5, R.mipmap.tran_pag_boom5, PAGDownTwoTransitionFilter.class, "transitions/particle/boom5"),
    PAG_COLOR_HEART(PAG_BOOM5.index + 1, R.string.array_color_heart, R.mipmap.tran_pag_heart, PAGDownloadFilter.class, "transitions/particle/heart1"),
    PAG_PARTICLE1(PAG_COLOR_HEART.index + 1, R.string.array_particle1, R.mipmap.tran_pag_particle1, PAGDownloadFilter.class, "transitions/particle/particle1_1"),
    /* particle_halo*/
    PAG_HALO1(PAG_PARTICLE1.index + 1, R.string.array_halo1, R.mipmap.tran_pag_halo1, PAGDownTwoTransitionFilter.class, "transitions/particle/halo1"),
    PAG_HALO2(PAG_HALO1.index + 1, R.string.array_halo2, R.mipmap.tran_pag_halo2, PAGDownTwoTransitionFilter.class, "transitions/particle/halo2"),
    PAG_HALO3(PAG_HALO2.index + 1, R.string.array_halo3, R.mipmap.tran_pag_halo3, "pag_tran/halo3.pag", PAGLocalTwoTransitionFilter.class),
    PAG_HALO4(PAG_HALO3.index + 1, R.string.array_halo4, R.mipmap.tran_pag_halo4, PAGDownTwoTransitionFilter.class, "transitions/particle/halo4"),
    PAG_HALO5(PAG_HALO4.index + 1, R.string.array_halo5, R.mipmap.tran_pag_halo5, PAGDownTwoTransitionFilter.class, "transitions/particle/halo5"),
    PAG_HALO6(PAG_HALO5.index + 1, R.string.array_halo6, R.mipmap.tran_pag_halo6, "pag_tran/halo6.pag", PAGLocalTwoTransitionFilter.class),
    PAG_HALO7(PAG_HALO6.index + 1, R.string.array_halo7, R.mipmap.tran_pag_halo7, PAGDownTwoTransitionFilter.class, "transitions/particle/halo7"),
    PAG_HALO8(PAG_HALO7.index + 1, R.string.array_halo8, R.mipmap.tran_pag_halo8, PAGDownTwoTransitionFilter.class, "transitions/particle/halo8"),
    PAG_HALO9(PAG_HALO8.index + 1, R.string.array_halo9, R.mipmap.tran_pag_halo9, "pag_tran/halo9.pag", PAGLocalTwoTransitionFilter.class),

    /*glitch毛刺*/
    PAG_INTEREST_GLITCH1(PAG_HALO9.index + 1, R.string.array_transition_glitch1, R.mipmap.tran_pag_glitch1, PAGDownTwoTransitionFilter.class, "transitions/glitch/glitch1"),
    PAG_INTEREST_GLITCH2(PAG_INTEREST_GLITCH1.index + 1, R.string.array_transition_glitch2, R.mipmap.tran_pag_glitch2, PAGDownTwoTransitionFilter.class, "transitions/glitch/glitch2"),
    PAG_INTEREST_GLITCH3(PAG_INTEREST_GLITCH2.index + 1, R.string.array_transition_glitch3, R.mipmap.tran_pag_glitch3, "pag_tran/glitch3.pag", PAGLocalTwoTransitionFilter.class),
    PAG_INTEREST_GLITCH4(PAG_INTEREST_GLITCH3.index + 1, R.string.array_transition_glitch4, R.mipmap.tran_pag_glitch4, PAGDownTwoTransitionFilter.class, "transitions/glitch/glitch4"),
    PAG_INTEREST_GLITCH5(PAG_INTEREST_GLITCH4.index + 1, R.string.array_transition_glitch5, R.mipmap.tran_pag_glitch5, PAGDownTwoTransitionFilter.class, "transitions/glitch/glitch5"),
    PAG_INTEREST_GLITCH6(PAG_INTEREST_GLITCH5.index + 1, R.string.array_transition_glitch6, R.mipmap.tran_pag_glitch6, PAGDownTwoTransitionFilter.class, "transitions/glitch/glitch6"),
    PAG_INTEREST_GLITCH7(PAG_INTEREST_GLITCH6.index + 1, R.string.array_transition_glitch7, R.mipmap.tran_pag_glitch7, "pag_tran/glitch7.pag", PAGLocalTwoTransitionFilter.class),
    //

    /*theme--inner*/
    THEME_TURN_PAGE(PAG_INTEREST_GLITCH7.index + 1, R.string.array_none, R.mipmap.tran_pag_glitch7, TurnPageFilter.class),

    ;


    int index;
    int nameid;
    int iconId;
    Class<? extends TransitionFilter> transitionClass;
    String localPath;
    String downOffet;
    String requestPath;//下载请求地址
    /**
     * 解决拉伸问题，适配比例转场:
     * 对于一些pag，需要完整性显示的可以inside，比如相机长方框
     * 而像树叶类似的，只能crop进行显示了，因为要铺满，inside反而不合适
     */
    boolean isInside;


    //    TransitionType(int index, int nameid, int iconId) {
//        this.index = index;
//        this.nameid = nameid;
//        this.iconId = iconId;
//    }
    TransitionType(int index, int nameid, int iconId, Class<? extends TransitionFilter> transitionClass) {
        this.index = index;
        this.nameid = nameid;
        this.iconId = iconId;
        this.transitionClass = transitionClass;

    }

    /*针对本地*/
    TransitionType(int index, int nameid, int iconId, String localPath, Class<? extends TransitionFilter> transitionClass) {
        this.index = index;
        this.nameid = nameid;
        this.iconId = iconId;
        this.localPath = localPath;
        this.transitionClass = transitionClass;
    }

    /*针对下载*/
    TransitionType(int index, int nameid, int iconId, Class<? extends TransitionFilter> transitionClass, String downOffet) {
        this.index = index;
        this.nameid = nameid;
        this.iconId = iconId;
        this.transitionClass = transitionClass;
        this.downOffet = downOffet;
    }


    /*针对inside的字段多态*/
    TransitionType(int index, int nameid, int iconId, String localPath, Class<? extends TransitionFilter> transitionClass, boolean isInside) {
        this.index = index;
        this.nameid = nameid;
        this.iconId = iconId;
        this.localPath = localPath;
        this.transitionClass = transitionClass;
        this.isInside = isInside;
    }

    /*针对inside的字段多态*/
    TransitionType(int index, int nameid, int iconId, Class<? extends TransitionFilter> transitionClass, String downOffet, boolean isInside) {
        this.index = index;
        this.nameid = nameid;
        this.iconId = iconId;
        this.transitionClass = transitionClass;
        this.downOffet = downOffet;
        this.isInside = isInside;
    }

    public static TransitionType randomTransition() {
        int index = 1 + new Random().nextInt(23);
        return TransitionType.values()[index];
    }

    public int getIndex() {
        return index;
    }


    /**
     * 是否为下载类型
     *
     * @return
     */
    public boolean isDownTransi() {
        return (PAGDownloadFilter.class.isAssignableFrom(transitionClass));
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getNameid() {
        return nameid;
    }

    public int getIconId() {
        return iconId;
    }


    /**
     * 下载的存储地址
     *
     * @param path
     * @return
     */
    public TransitionType setLocalPath(String path) {
        this.localPath = path;
        return this;
    }

    public TransitionType setRequestPath(String path) {
        this.requestPath = path;
        return this;
    }

    public String getDownOffet() {
        return downOffet;
    }

    public void setDownOffet(String downOffet) {
        this.downOffet = downOffet;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Class<? extends TransitionFilter> getTransitionClass() {
        return transitionClass;
    }

    public void setTransitionClass(Class<? extends TransitionFilter> transitionClass) {
        this.transitionClass = transitionClass;
    }

    public boolean isInside() {
        return isInside;
    }

    public void setInside(boolean inside) {
        isInside = inside;
    }


}

