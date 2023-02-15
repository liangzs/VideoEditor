package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.sport.sport2

import android.graphics.Matrix
import com.ijoysoft.mediasdk.common.utils.MatrixUtils
import com.ijoysoft.mediasdk.module.entity.ThemeWidgetInfo
import com.ijoysoft.mediasdk.module.opengl.filter.ThemePagFilter
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager.ReflectOpenglThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.EmptyBlurAction
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class Sport2ThemeManager: ReflectOpenglThemeManager() {
    override fun getFinalActionClasses() = mutableListOf(EmptyBlurAction::class.java)

    override fun createThemePags(): List<ThemeWidgetInfo> {
        return listOf(ThemeWidgetInfo(0, Long.MAX_VALUE), ThemeWidgetInfo(0, Long.MAX_VALUE))
    }

    override fun dealThemePag(index: Int, themePagFilter: ThemePagFilter?) {
        super.dealThemePag(index, themePagFilter)

        if (index == 0) {
            themePagFilter?.pagFile?.pagFile?.let { pag ->
                pag.setMatrix(Matrix().also {
                    it.postTranslate(0f, 0f)
                })
            }
        } else {
            themePagFilter?.pagFile?.pagFile?.let { pag ->
                pag.setMatrix(Matrix().also {
                    it.postTranslate(0f, height.toFloat() - pag.height())
                })
            }
        }
        themePagFilter?.matrix = MatrixUtils.getOriginalMatrix().run {
            MatrixUtils.flip(this, false, true)
        }
    }


}