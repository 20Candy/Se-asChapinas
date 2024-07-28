package com.example.components.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.components.R;

public class RecordButton extends ConstraintLayout implements  IButtons {

    //Atributos del componente ---------------------------------------------------------------------
    private Button button;
    private OnRecordListener onRecordListener; // Listener para los eventos de grabación


    // Metodos de ContraintLayout ------------------------------------------------------------------
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
        button = findViewById(R.id.btn_main);
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

        if (text != null && !text.isEmpty()) {
            setText(text);
        }

        setTextSize(textSize);
        setEnabled(enabled);

        if (textColor != 0) {
            setTextColor(textColor);
        }

        if (background != null) {
            setBackground(background);
        }

    }

    private void setTouchListener() {
        final Drawable normalDrawable = ContextCompat.getDrawable(getContext(), R.drawable.record_button);
        final Drawable recordingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.recording_button);
        final TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] { normalDrawable, recordingDrawable });

        button.setBackground(normalDrawable);  // Set the initial background

        button.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        button.setBackground(transitionDrawable);
                        transitionDrawable.startTransition(300); // Start the transition over 300 milliseconds
                        if (onRecordListener != null) {
                            onRecordListener.onStartRecording();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        transitionDrawable.reverseTransition(300); // Reverse the transition
                        if (onRecordListener != null) {
                            onRecordListener.onStopRecording();
                        }
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            v.performClick();
                        }
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    public boolean performClick() {
        // Call the superclass to handle the click
        return super.performClick();
    }


    // Metodos de IButtons -------------------------------------------------------------------------
    @Override
    public void setText(String value) {
        button.setText(value);
    }

    @Override
    public void setTextSize(int value) {
        button.setTextSize(value);
    }

    @Override
    public void setEnabled(boolean value) {
        //no aplica
    }

    @Override
    public void setTextColor(int color) {
        button.setTextColor(color);
    }

    @Override
    public void setBackground(Drawable value) {
        button.setBackground(value);
    }

    // Listener ------------------------------------------------------------------------------------
    public void setOnClickListener(OnClickListener listener) {
        button.setOnClickListener(new DebounceClickListener(listener));
    }

    // Interfaz
    public interface OnRecordListener {
        void onStartRecording();
        void onStopRecording();
    }

    public void setOnRecordListener(OnRecordListener listener) {
        this.onRecordListener = listener;
    }




}
