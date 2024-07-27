package com.example.components.buttons;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;


public class DebounceClickListener implements View.OnClickListener {

    private static final long DEBOUNCE_TIME = 700;
    private final Runnable clickRunnable;

    private View view;
    private Context context;

    private final Handler handler;
    private boolean isEnabled = true;
//    private CustomProgressBarDialog progressBarDialog;

    public DebounceClickListener(View.OnClickListener clickListener) {
        this.clickRunnable = new Runnable() {
            @Override
            public void run() {
                if (isEnabled) {
                    disableListeners();
                    if (view != null) {
                        clickListener.onClick(view);
                        enableListenersDelayed();
                    }
                }
            }
        };
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onClick(View v) {
        if (isEnabled) {
            context = v.getContext();
            showCustomDialogProgress();
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(clickRunnable, DEBOUNCE_TIME);
            view = v;
        }
    }

    private void enableListenersDelayed() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isEnabled = true;
//                if (progressBarDialog != null && progressBarDialog.isShowing()) {
//                    progressBarDialog.dismiss();
//                }
            }
        }, DEBOUNCE_TIME);
    }

    protected void showCustomDialogProgress() {
//        if (progressBarDialog == null || !progressBarDialog.isShowing()) {
//            progressBarDialog = new CustomProgressBarDialog(context);
//            try {
//                progressBarDialog.setCancelable(false);
//                progressBarDialog.show();
//            } catch (Exception e) {
//                Log.e("Error", "showCustomDialogProgress: ", e);
//            }
//        }
    }

    private void disableListeners() {
        isEnabled = false;
    }


    public void OnClickListener(View view) {
        this.view = view;
    }

    public interface OnClickListener {
        void onClick(View v);
    }
}
