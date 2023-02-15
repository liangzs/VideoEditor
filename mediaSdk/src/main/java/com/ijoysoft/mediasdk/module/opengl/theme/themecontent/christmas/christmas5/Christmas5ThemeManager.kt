package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.christmas.christmas5

import android.graphics.Bitmap
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.IBaseTimeCusFragmeng
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.EvaluatorHelper
import android.opengl.GLES20
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.action.ThemeWidgetHelper
import com.ijoysoft.mediasdk.module.opengl.theme.action.moveaction.AnimationBuilder
import java.util.*

/**
 * 该主题用了重度的动感模糊，所以重新定义模糊方案
 */
class Christmas5ThemeManager : BaseTimeThemeManager(), IBaseTimeCusFragmeng {
    private val CUS_FRAGMENT = """  #define PITwo 6.28318530718//2pi
    #define PI 3.141592653589793
    precision mediump float;
    varying vec2 textureCoordinate;
    varying float vAlpha;
    uniform sampler2D vTexture;
    uniform float range;   //0-10 半径
    uniform float angle;//0-180 角度
	uniform int blurType;
	uniform float radioStrength;
    void main(){ 
    float rad=PI/180.0*angle;
	if(blurType==2){
	  vec2 center = vec2(.5, .5);
	  vec3 color = vec3(0.0);
	  float total = 0.0;
	  vec2 toCenter = center - textureCoordinate;
	  for (float t = 0.0; t <= 20.0; t++) {
	   float percent = (t) / 20.0;
	   float weight = 1.0 * (percent - percent * percent);
	   color += texture2D(vTexture, textureCoordinate + toCenter * percent * radioStrength).rgb * weight;
	   total += weight;
	  }
	  gl_FragColor = vec4(color / total, 1.0);
	  return;
	 };
    if(range==0.0){
    gl_FragColor=texture2D(vTexture, textureCoordinate);
    return;   
	}
    vec4 clraverge=vec4(0.0);
    float samplerPre=1.0;
    for(float j = 1.0; j <= range; j+=samplerPre){
        float dx=0.01*cos(rad);
        float dy=0.01*sin(rad);
        vec2 samplerTexCoord = vec2(textureCoordinate.x+j*dx, textureCoordinate.y+j*dy);
        vec2 samplerTexCoord1= vec2(textureCoordinate.x-j*dx, textureCoordinate.y-j*dy);
        vec4 tc= texture2D(vTexture, samplerTexCoord);
        vec4 tc1= texture2D(vTexture, samplerTexCoord1);
        clraverge+=tc;
        clraverge+=tc1;
    }
    clraverge/=(range*2.0/samplerPre);
    gl_FragColor=clraverge;
	}"""
    private var rangeLocation = 0
    private var angleLocation = 0
    override fun createWidgetByIndex(index: Int): ThemeWidgetInfo? {
        return null
    }


    override fun createThemePags(): List<ThemeWidgetInfo> {
        return listOf(ThemeWidgetInfo(600, 1200), ThemeWidgetInfo(1800, 2400))
    }

    override fun customCreateWidget(widgetMipmaps: MutableList<Bitmap>?): Boolean {
        var themeWidgetInfo = ThemeWidgetInfo(1200, 0, 1000, 0, 2400)
        themeWidgetInfo.bitmap = widgetMipmaps!![0]
        var widgetExample = createWidgetTimeExample(themeWidgetInfo)
        widgetExample.setEnterAction(ThemeWidgetHelper.springMove(2000, AnimateInfo.ORIENTATION.TOP))
        widgetExample.setOutAction(AnimationBuilder().setFade(1f, 0f))

        //控件
        themeWidgetInfo = ThemeWidgetInfo(1200, 0, 800, 3200, 5600)
        themeWidgetInfo.bitmap = widgetMipmaps[1]
        widgetExample = createWidgetTimeExample(themeWidgetInfo)
        widgetExample.setEnterAction(AnimationBuilder().setFade(0f, 1f))
        widgetExample.setOutAction(AnimationBuilder().setFade(1f, 0f))

        //三
        themeWidgetInfo = ThemeWidgetInfo(1200, 0, 800, 3200, 5600)
        themeWidgetInfo.bitmap = widgetMipmaps[2]
        widgetExample = createWidgetTimeExample(themeWidgetInfo)
        widgetExample.setEnterAction(ThemeWidgetHelper.springMove(1200, AnimateInfo.ORIENTATION.BOTTOM))
        widgetExample.setOutAction(AnimationBuilder().setFade(1f, 0f))
        return true
    }

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {
        widgetTimeExample?.apply {
            when (index) {
                0 -> {
                    if (width > height) {
                        adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 2.2f)
                    } else {
                        adjustScalingFixX(AnimateInfo.ORIENTATION.TOP)
                    }
                }
                1 -> {
                    if (width > height) {
                        adjustScaling(AnimateInfo.ORIENTATION.TOP, 0f, 2.2f)
                    } else {
                        adjustScalingFixX(AnimateInfo.ORIENTATION.TOP)
                    }
                }
                2 -> adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, 0.6f, 2.2f)
            }
        }
    }

    override fun existPag(): Boolean {
        return true
    }

    override fun blurBackground(): Boolean {
        return true
    }

    override fun isTextureInside(): Boolean {
        return false
    }

    /**
     * 添加 自定义fragment
     *
     * @return
     */
    override fun customFragment(): String {
        setBaseTimeCusFragmeng(this)
        return CUS_FRAGMENT
    }

    override fun computeThemeCycleTime(): Int {
        return 6800
    }

    override fun computeClipTime(): Int {
        return 2400
    }

    /**
     * setMoveBlurRange 暂时取消，影响到控件的抖动，未找到合适解决方案
     *
     * @param index
     * @param duration
     * @return switch (index % getMipmapsCount()) {
     * return 800;
     * }
     */
    override fun createAnimateEvaluate(index: Int, duration: Long): ArrayList<BaseEvaluate> {
        val list = ArrayList<BaseEvaluate>()
        currentDuration = 1200
        if (index % 2 == 0) {
            list.addAll(EvaluatorHelper.springYBycornorCenter(0.9f, currentDuration.toLong(), duration, width, height))
        } else {
            list.addAll(EvaluatorHelper.springYBycornorCenter(-0.9f, currentDuration.toLong(), duration, width, height))
        }
        //右下角弹动，弹动角度归正
        return list
    }

    override fun createActions(): Int {
        return 12
    }

    override fun initLocation(programe: Int) {
        rangeLocation = GLES20.glGetUniformLocation(programe, "range")
        angleLocation = GLES20.glGetUniformLocation(programe, "angle")
    }

    override fun drawLocation(baseEvaluate: BaseEvaluate) {
//        LogUtils.v("", "baseEvaluate.getCustomValue():" + baseEvaluate.getCustomValue());
        GLES20.glUniform1f(rangeLocation, baseEvaluate.customValue.toInt().toFloat())
        GLES20.glUniform1f(angleLocation, baseEvaluate.customValue2)
    }

}