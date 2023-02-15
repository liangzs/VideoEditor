package com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily5

import com.ijoysoft.mediasdk.common.global.ConstantMediaSize
import com.ijoysoft.mediasdk.common.utils.BitmapUtil
import com.ijoysoft.mediasdk.module.opengl.filter.ImageOriginFilter
import com.ijoysoft.mediasdk.module.opengl.theme.ThemeOpenglManager.ReflectOpenglThemeManager
import com.ijoysoft.mediasdk.module.opengl.theme.action.EmptyBlurAction
import com.ijoysoft.mediasdk.module.opengl.theme.themecontent.daily.daily4.Daily4ThemeManager
import com.ijoysoft.mediasdk.module.opengl.transition.TransitionType

class Daily5ThemeManager : Daily4ThemeManager() {

    override fun themeTransition(): TransitionType {
        return TransitionType.PAG_INTEREST_CAMERA2
    }
}