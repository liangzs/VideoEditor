package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.food

import android.graphics.Bitmap
import android.graphics.Matrix
import android.opengl.GLES20
import com.ijoysoft.mediasdk.common.utils.MatrixUtils
import com.ijoysoft.mediasdk.module.entity.MediaItem
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.GlobalDrawer
import com.ijoysoft.mediasdk.module.opengl.filter.GifOriginFilter
import com.ijoysoft.mediasdk.module.opengl.filter.ThemePagFilter
import com.ijoysoft.mediasdk.module.opengl.theme.AnimateInfo
import com.ijoysoft.mediasdk.module.opengl.theme.BaseTimeThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.IBaseTimeCusFragmeng
import com.ijoysoft.mediasdk.module.opengl.theme.action.BaseEvaluate
import com.ijoysoft.mediasdk.module.opengl.theme.action.EvaluatorHelper
import com.ijoysoft.mediasdk.module.opengl.theme.action.WidgetTimeExample
import com.ijoysoft.mediasdk.module.opengl.theme.ternary
import org.libpag.PAGFile

/**
 * 该主题用了重度的动感模糊，所以重新定义模糊方案
 */
class Food0ThemeManager : BaseTimeThemeManager(), IBaseTimeCusFragmeng {
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

    //复用的图片两组顶点
    var gifVertexs = arrayOf<FloatArray>(FloatArray(16), FloatArray(16))

    override fun createThemePags(): List<ThemeWidgetInfo> {
        //return listOf(ThemeWidgetInfo(0, recyleTime.toLong()), ThemeWidgetInfo(0, recyleTime.toLong()))
        return listOf(ThemeWidgetInfo(0, recyleTime.toLong()))
    }

    /**
     * inside居中
     *
     * @return
     */
    fun insideCenter(mPAGFile: PAGFile): Matrix? {
        val matrix = Matrix()
        val scale = 0.7f;
        val offsetx = (width - mPAGFile.width() * scale) / 2
        val offsety = (height - mPAGFile.height() * scale) / 2
        matrix.postScale(scale, scale)
        matrix.postTranslate(offsetx, offsety)
        return matrix
    }

    override fun dealThemePag(index: Int, themePagFilter: ThemePagFilter?) {
        super.dealThemePag(index, themePagFilter)
        if (index == 0) {
            themePagFilter?.pagFile?.pagFile?.let {
                it.setMatrix(insideCenter(it))

                val text = it.getTextData(0)
                text.text = "Happy Day"
                it.replaceText(0, text)
            }
            themePagFilter?.matrix = MatrixUtils.getOriginalMatrix().run {
                MatrixUtils.flip(this, false, true)
            }
        }
    }

    override fun drawPrepare(mediaDrawer: GlobalDrawer?, mediaItem: MediaItem?, index: Int) {
        super.drawPrepare(mediaDrawer, mediaItem, index)
        mThemePagFilters.get(0).pagFile?.pagFile?.let {
            val text = it.getTextData(1)
            text.text = index.toString()
            it.replaceText(1, text)
        }
    }

    override fun customCreateWidget(widgetMipmaps: MutableList<Bitmap>?): Boolean {
        var themeWidgetInfo = ThemeWidgetInfo(0, recyleTime, 0, 0, recyleTime.toLong())
        themeWidgetInfo.bitmap = widgetMipmaps!![0]
        createWidgetTimeExample(themeWidgetInfo)
        //widgetExample.setEnterAction(ThemeWidgetHelper.springMove(2000, AnimateInfo.ORIENTATION.TOP))
        //widgetExample.setOutAction(AnimationBuilder().setFade(1f, 0f))
        themeWidgetInfo = ThemeWidgetInfo(0, recyleTime, 0, 0, recyleTime.toLong())
        themeWidgetInfo.bitmap = widgetMipmaps!![0]
        createWidgetTimeExample(themeWidgetInfo).run {
            setOnSizeChangedListener { w, h ->
            }
        }
        return true
    }

    override fun intWidget(index: Int, widgetTimeExample: WidgetTimeExample?) {
        widgetTimeExample?.apply {
            when (index) {
                0 -> {
                    adjustScaling(AnimateInfo.ORIENTATION.BOTTOM, 0.6f, (width < height).ternary(2.8f,
                        (width == height).ternary(3.0f, 2.2f)))
                    if (width > height) {
                        var temp = cube;
                        temp[1] += 0.2f
                        temp[3] += 0.2f
                        temp[5] += 0.2f
                        temp[7] += 0.2f
                        setVertex(temp)
                    }

                }
                1 -> {
                    adjustScaling(AnimateInfo.ORIENTATION.TOP, -0.6f, (width < height).ternary(2.8f,
                        (width == height).ternary(3.0f, 2.2f)));
                    //横屏向下平移一下
                    if (width > height) {
                        var temp = cube;
                        temp[1] -= 0.2f
                        temp[3] -= 0.2f
                        temp[5] -= 0.2f
                        temp[7] -= 0.2f
                        setVertex(temp)
                    }
                }
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

    override fun onGifSizeChanged(index: Int, width: Int, height: Int) {
        super.onGifSizeChanged(index, width, height)
        gifVertexs[1] = globalizedGifOriginFilters[0].adjustScaling(width, height, AnimateInfo.ORIENTATION.BOTTOM,
            (width < height).ternary(-0.6f, -0.7f), (width < height).ternary(2.8f,
                (width == height).ternary(3.0f, 2.2f)))

        gifVertexs[0] = globalizedGifOriginFilters[0].adjustScaling(width, height, AnimateInfo.ORIENTATION.TOP,
            (width < height).ternary(0.6f, 0.7f), (width < height).ternary(2.8f,
                (width == height).ternary(3.0f, 2.2f)));

        if (width > height) {
            gifVertexs[1][1] += 0.1f
            gifVertexs[1][3] += 0.1f
            gifVertexs[1][5] += 0.1f
            gifVertexs[1][7] += 0.1f

            gifVertexs[0][1] -= 0.1f
            gifVertexs[0][3] -= 0.1f
            gifVertexs[0][5] -= 0.1f
            gifVertexs[0][7] -= 0.1f
        }

    }

    override fun onGifDrawerCreated(mediaItem: MediaItem?, index: Int) {
        mediaItem?.let {
            globalizedGifOriginFilters = ArrayList()
            globalizedGifOriginFilters.add(GifOriginFilter().also {
                it.setFrames(mediaItem.dynamicMitmaps[0])
            })
        }
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
        currentDuration = if (index < 6) {
            1000
        } else {
            600
        }
        if (index % 2 == 0) {
            if (index < 6) {
                list.addAll(EvaluatorHelper.springXBycornorCenterNomal(0.9f, currentDuration.toLong(), duration, width, height))
            } else {
                list.addAll(EvaluatorHelper.springXBycornorCenter(0.9f, currentDuration.toLong(), duration, width, height))
            }
        } else {
            if (index < 6) {
                list.addAll(EvaluatorHelper.springXBycornorCenterNomal(-0.9f, currentDuration.toLong(), duration, width, height))
            } else {
                list.addAll(EvaluatorHelper.springXBycornorCenter(-0.9f, currentDuration.toLong(), duration, width, height))
            }

        }
        //右下角弹动，弹动角度归正
        return list
    }

    override fun createActions(): Int {
        return Int.MAX_VALUE
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

    /**
     * 复用控件和gif的重写
     */
    override fun onDrawFrame(currenPostion: Int) {
        super.onDrawFrame(currenPostion)
        //修改顶点
        globalizedGifOriginFilters.get(0).setVertex(gifVertexs[1])
        globalizedGifOriginFilters.get(0).draw()
        globalizedGifOriginFilters.get(0).setVertex(gifVertexs[0])
    }
}