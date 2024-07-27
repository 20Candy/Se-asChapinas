package com.example.components.buttons;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.components.R;

public class MainButton extends ConstraintLayout {

    public MainButton(Context context) {
        super(context);
        initView();
    }

    public MainButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MainButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.main_button, this);
    }

}
