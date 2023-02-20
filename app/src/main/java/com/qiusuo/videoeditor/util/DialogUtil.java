package com.qiusuo.videoeditor.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.qiusuo.videoeditor.R;

/**
 * Created by DELL on 2019/8/13.
 */
public class DialogUtil {

    /*设置dialog全屏   view   DectorView*/
    public static void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions =
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            view.setSystemUiVisibility(uiOptions);
        }
    }

    /*取消dialog焦点*/
    public static void cancelDialogfocusable(Dialog dialog) {
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    /*恢复dialog焦点*/
    public static void recoverDialogFocusable(Dialog dialog) {
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }
//
//    public static CommenMaterialDialog.CommenMaterialParams getCommenMaterialParams(Context context) {
//        CommenMaterialDialog.CommenMaterialParams params = CommenMaterialDialog.CommenMaterialParams.getDefault(context);
//        params.background = context.getResources().getDrawable(R.drawable.dialog_background);
//        params.cancelable = true;
//        params.negativeButtonTextColor = context.getResources().getColor(R.color.colorPrimary);
//        params.positiveButtonTextColor = params.negativeButtonTextColor;
//        params.titleTextColor = 0xDB000000;
//        params.msgTextColor = 0x8A000000;
//        params.negativeButtonBgDrawable = params.positiveButtonBgDrawable = context.getResources().getDrawable(R.drawable.btn_selector);
//        return params;
//    }


    /**
     * 同主题颜色的对话框
     *
     * @param context
     * @param title
     * @param dialogClickListener
     */
    public static void createCommonDialog(Context context, String title, DialogClickListener dialogClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_save_draft_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
        ((TextView) view.findViewById(R.id.message)).setText(title);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.ok).setOnClickListener(v -> {
            alertDialog.dismiss();
            if (dialogClickListener != null) {
                dialogClickListener.ok();
            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(v -> {
            alertDialog.dismiss();
            if (dialogClickListener != null) {
                dialogClickListener.cancel();
            }
        });
        alertDialog.show();
    }

    public static interface DialogClickListener {
        default void cancel() {
        }

        void ok();
    }

}
