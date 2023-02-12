package com.qiusuo.videoeditor.module.select;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.qiusuo.videoeditor.R;
import com.qiusuo.videoeditor.base.FragmentInjectManager;
import com.qiusuo.videoeditor.common.bean.LocalMedia;
import com.qiusuo.videoeditor.module.select.config.PictureConfig;
import com.qiusuo.videoeditor.module.select.config.PictureSelectionConfig;
import com.qiusuo.videoeditor.module.select.repository.SelectedManager;
import com.qiusuo.videoeditor.module.select.style.PictureWindowAnimationStyle;
import com.qiusuo.videoeditor.module.select.style.SelectMainStyle;
import com.qiusuo.videoeditor.util.ImmersiveManager;
import com.qiusuo.videoeditor.util.StyleUtils;

import java.util.ArrayList;


public class PictureSelectorSupporterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        immersive();
        setContentView(R.layout.ps_activity_container);
        setupFragment();
    }

    private void immersive() {
        SelectMainStyle mainStyle = PictureSelectionConfig.selectorStyle.getSelectMainStyle();
        int statusBarColor = mainStyle.getStatusBarColor();
        int navigationBarColor = mainStyle.getNavigationBarColor();
        boolean isDarkStatusBarBlack = mainStyle.isDarkStatusBarBlack();
        if (!StyleUtils.checkStyleValidity(statusBarColor)) {
            statusBarColor = ContextCompat.getColor(this, R.color.ps_color_grey);
        }
        if (!StyleUtils.checkStyleValidity(navigationBarColor)) {
            navigationBarColor = ContextCompat.getColor(this, R.color.ps_color_grey);
        }
        ImmersiveManager.immersiveAboveAPI23(this, statusBarColor, navigationBarColor, isDarkStatusBarBlack);
    }

    private void setupFragment() {
        if (getIntent().hasExtra(PictureConfig.EXTRA_EXTERNAL_PREVIEW)
                && getIntent().getBooleanExtra(PictureConfig.EXTRA_EXTERNAL_PREVIEW, false)) {
            int position = getIntent().getIntExtra(PictureConfig.EXTRA_PREVIEW_CURRENT_POSITION, 0);
            PictureSelectorPreviewFragment fragment = PictureSelectorPreviewFragment.newInstance();
            ArrayList<LocalMedia> previewResult = SelectedManager.getSelectedPreviewResult();
            ArrayList<LocalMedia> previewData = new ArrayList<>(previewResult);
            boolean isDisplayDelete = getIntent()
                    .getBooleanExtra(PictureConfig.EXTRA_EXTERNAL_PREVIEW_DISPLAY_DELETE, false);
            fragment.setExternalPreviewData(position, previewData.size(), previewData, isDisplayDelete);
            FragmentInjectManager.injectFragment(this, PictureSelectorPreviewFragment.TAG, fragment);
        } else {
            FragmentInjectManager.injectFragment(this, PictureSelectorFragment.TAG,
                    PictureSelectorFragment.newInstance());
        }
    }

    /**
     * set app language
     */
    public void initAppLanguage() {
        PictureSelectionConfig config = PictureSelectionConfig.getInstance();
//        if (config.language != LanguageConfig.UNKNOWN_LANGUAGE && !config.isOnlyCamera) {
//            PictureLanguageUtils.setAppLanguage(this, config.language);
//        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initAppLanguage();
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(PictureContextWrapper.wrap(newBase,
//                PictureSelectionConfig.getInstance().language));
//    }

    @Override
    public void finish() {
        super.finish();
        PictureWindowAnimationStyle windowAnimationStyle = PictureSelectionConfig.selectorStyle.getWindowAnimationStyle();
        overridePendingTransition(0, windowAnimationStyle.activityExitAnimation);
    }
}
