package com.example.components.buttons;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.components.R;
import com.example.components.buttons.DebounceClickListener;
import com.example.components.buttons.IButtons;

public class RecordButton extends ConstraintLayout implements IButtons {

    private Button pre_record;
    private ConstraintLayout recording;
    private ImageView front, back;
    private TextView timer;
    private ProgressBar progressBar;
    private OnRecordListener onRecordListener;
    private CountDownTimer countDownTimer;
    private boolean isRecording = false;

    public RecordButton(Context context) {
        super(context);
        initView();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        declareAttr(context, attrs);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
        declareAttr(context, attrs);
    }

    private void initView() {
        inflate(getContext(), R.layout.record_button, this);
        pre_record = findViewById(R.id.btn_pre_record);
        recording = findViewById(R.id.btn_recording);
        front = findViewById(R.id.imgFront);
        back = findViewById(R.id.img_back);
        timer = findViewById(R.id.timer_text);
        progressBar = findViewById(R.id.progress_bar);

        setTouchListener();
    }

    private void declareAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.button, 0, 0
        );

        String text = array.getString(R.styleable.button_text);
        int textSize = array.getInt(R.styleable.button_textSize, 18);
        boolean enabled = array.getBoolean(R.styleable.button_enabled, true);
        int textColor = array.getInt(R.styleable.button_textColor, 0);
        Drawable background = array.getDrawable(R.styleable.button_background);

        setText(text);
        setTextSize(textSize);
        setEnabled(enabled);
        setTextColor(textColor);
        setBackground(background);
        array.recycle();
    }

    private void setTouchListener() {
        pre_record.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    stopRecording();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.performClick();
                    }
                }
                return false;
            }
        });
    }

    private void startRecording() {
        if (!isRecording) {
            isRecording = true;
            recording.setVisibility(VISIBLE);
            pre_record.setVisibility(INVISIBLE);
            timer.setVisibility(VISIBLE);
            progressBar.setVisibility(VISIBLE);
            progressBar.setProgress(0);
            startTimer();
            animateButton(true);
            if (onRecordListener != null) {
                onRecordListener.onStartRecording();
            }
        }
    }

    private void stopRecording() {
        if (isRecording) {
            isRecording = false;
            recording.setVisibility(GONE);
            pre_record.setVisibility(VISIBLE);
            stopTimer();
            animateButton(false);
            if (onRecordListener != null) {
                onRecordListener.onStopRecording();
            }
        }
    }

    private void animateButton(boolean startRecording) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(back, "scaleX", startRecording ? 1.3f : 1f),
                ObjectAnimator.ofFloat(back, "scaleY", startRecording ? 1.3f : 1f),
                ObjectAnimator.ofFloat(front, "scaleX", startRecording ? 0.5f : 1f),
                ObjectAnimator.ofFloat(front, "scaleY", startRecording ? 0.5f : 1f)
        );
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(15000, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (15000 - millisUntilFinished) / 1000;
                timer.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
                animateProgressBar((15000 - millisUntilFinished) / 15000.0f * 100);
            }

            @Override
            public void onFinish() {
                stopRecording(); // Calls stopRecording to handle the end of the timer.
            }
        };
        countDownTimer.start();
    }

    private void animateProgressBar(float toProgress) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", (int) toProgress);
        progressAnimator.setDuration(100);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timer.setText("00:00");
        progressBar.setProgress(0);
    }

    @Override
    public void setText(String value) {

    }

    @Override
    public void setTextSize(int value) {

    }

    @Override
    public void setTextColor(int value) {

    }

    public interface OnRecordListener {
        void onStartRecording();
        void onStopRecording();
    }

    public void setOnRecordListener(OnRecordListener listener) {
        this.onRecordListener = listener;
    }
}
