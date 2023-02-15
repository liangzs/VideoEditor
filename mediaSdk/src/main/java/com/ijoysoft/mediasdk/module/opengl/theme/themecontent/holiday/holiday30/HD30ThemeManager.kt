package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.holiday.holiday30

import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager.ReflectOpenglThemeManager

class HD30ThemeManager: ReflectOpenglThemeManager() {


    val actions = mutableListOf(HD30Action::class.java)


    override fun getFinalActionClasses() = actions


}