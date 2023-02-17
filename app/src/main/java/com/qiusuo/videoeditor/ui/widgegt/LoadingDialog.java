package com.qiusuo.videoeditor.ui.widgegt;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.qiusuo.videoeditor.R;


/**
 * 自定义LoadingDialog
 */
public class LoadingDialog extends Dialog {
    private LinearLayout mLinearLayout;
    private LinearLayout llBg;
    /**
     * 标题
     */
    private TextView titleView;
    /**
     * 提示信息
     */
    private TextView tipsView;
    /**
     * 内容id
     */
    private int tipsId = 0;
    /**
     * 内容
     */
    private String strTips = null;
    /**
     * 标题
     */
    private String strTitle = null;

    /**
     * 是否可取消
     */
    private boolean cancelable = false;

    public interface BlossomDialogListener {
        void onClick(View view);
    }

    /**
     * 上层ativity
     */
    AppCompatActivity activity;


    public LoadingDialog(Context context, String tips, boolean cancelable) {
        super(context, R.style.dialog_style);
        activity = (AppCompatActivity) context;
        this.cancelable = cancelable;
        this.strTips = tips;
    }


    /**
     * 自定义Dialog
     *
     * @param context
     */
    public LoadingDialog(Context context) {
        super(context, R.style.dialog_style);
        activity = (AppCompatActivity) context;
        //  getWindow().setWindowAnimations(R.style.dialogWindowAnim);
    }

    /**
     * 自定义Dialog
     *
     * @param context
     * @param tipsId  提示信息Id, 0为不改变提示信息
     */
    public LoadingDialog(Context context, int tipsId) {
        super(context, R.style.dialog_style);
        activity = (AppCompatActivity) context;
        this.tipsId = tipsId;
        //   getWindow().setWindowAnimations(R.style.dialogWindowAnim);
    }

    /**
     * 自定义Dialog
     *
     * @param context
     */
    public LoadingDialog(Context context, String title, String tips) {
        super(context, R.style.dialog_style);
        activity = (AppCompatActivity) context;
        this.strTitle = title;
        this.strTips = tips;
        //   getWindow().setWindowAnimations(R.style.dialogWindowAnim);
    }

    /**
     * 是否包含标题
     *
     * @return
     */
    public boolean isWithTitle() {
        return null != strTitle;
    }

    private Handler timerHandler = null;
    int count = 0;
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            try {
                timerHandler.postDelayed(timer, 1000);
                String dots = null;
                switch (count % 4) {
                    case 0:
                        dots = "";
                        break;
                    case 1:
                        dots = ".";
                        break;
                    case 2:
                        dots = "..";
                        break;
                    case 3:
                        dots = "...";
                        break;
                }
                tipsView.setText(strTips + dots);
                count++;
            } catch (Exception e) {
                LogUtils.e("倒计时timer", e.toString());
            }
        }
    };

    public void setTips(String strTips) {
        this.strTips = strTips;
        if (tipsView != null) {
            if (TextUtils.isEmpty(strTips)) {
                tipsView.setVisibility(View.GONE);
            } else {
                tipsView.setVisibility(View.VISIBLE);
            }
        }
        if (null != tipsView && null != strTips) {
            tipsView.setText(strTips);
            LogUtils.v("LoadingDialog", "tips: " + strTips);
        }
    }


    public void setMask(boolean isMask) {
        if (isMask) {
            mLinearLayout.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
        } else {
            mLinearLayout.setBackgroundColor(getContext().getResources().getColor(R.color.black_31_color));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_loading);
        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelable);
        findViews();
        fillContent();
    }

    @SuppressLint("ResourceAsColor")
    private void findViews() {
        tipsView = (TextView) findViewById(R.id.tipsLoding);
        titleView = (TextView) findViewById(R.id.title);
        mLinearLayout = findViewById(R.id.ll_loading);
        llBg = findViewById(R.id.ll_bg);
        //缩小窗口防止影响导航栏颜色
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        //不影导航栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialogWindow.setStatusBarColor(R.color.transparent);
        }
        //显示的坐标
        lp.x = 0;
//        View top = activity.findViewById(R.id.custom_toolbar_layout);
//        int[] loaction = new int[2];
//        if (top == null) {
//            lp.y = 0;
//        } else {
//            top.getLocationOnScreen(loaction);
//            lp.y = loaction[1] + top.getHeight();
//        }
        //dialog的大小
        lp.width = activity.getWindow().getAttributes().width;
//        View bottom = activity.findViewById(R.id.main_adv_banner_layout);
//        if (bottom == null) {
        lp.height = activity.getWindow().getDecorView().getBottom() - lp.y;
//        } else {
//            bottom.getLocationOnScreen(loaction);
//            lp.height = loaction[1] - lp.y;
//        }
        dialogWindow.setAttributes(lp);
    }

    private void fillContent() {
        if (0 != tipsId) {
            tipsView.setText(tipsId);
        } else if (null != strTips) {
            tipsView.setText(strTips);
        }
        if (tipsId == 0 && TextUtils.isEmpty(strTips)) {
            tipsView.setVisibility(View.GONE);
        } else {
            tipsView.setVisibility(View.VISIBLE);
        }

        if (null != strTitle && null != titleView) {
            titleView.setText(strTitle);
            count = 0;
            timerHandler = new Handler();
            timerHandler.post(timer);
        }
    }

    @Override
    public void dismiss() {
        if (null != timerHandler)
            timerHandler.removeCallbacks(timer);
        super.dismiss();
    }


    /**
     * 缩小窗口防止影响导航栏颜色
     */
    @Override
    public void show() {
        super.show();
//        if (tipsView != null && ObjectUtils.isEmpty(tipsView.getText().toString())) {
//            llBg.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
//        } else {
//            llBg.setBackgroundResource(R.drawable.shape_loading_bg);
//        }

    }
}