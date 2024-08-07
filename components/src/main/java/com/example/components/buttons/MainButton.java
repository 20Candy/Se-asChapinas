package com.example.components.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.components.R;

public class MainButton extends ConstraintLayout implements IButtons {

    //Atributos del componente ---------------------------------------------------------------------
    private Button mainButton;

    // Metodos de ContraintLayout ------------------------------------------------------------------
    public MainButton(Context context) {
        super(context);
        initView();
    }

    public MainButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        declareAttr(context, attrs);
    }

    public MainButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
        declareAttr(context, attrs);
    }

    private void initView() {
        inflate(getContext(), R.layout.main_button, this);
        mainButton = findViewById(R.id.btn_main);
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

    // Metodos de IButtons -------------------------------------------------------------------------
    @Override
    public void setText(String value) {
        mainButton.setText(value);
    }

    @Override
    public void setTextSize(int value) {
        mainButton.setTextSize(value);
    }

    @Override
    public void setEnabled(boolean value) {
        if (!value) {
            mainButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.light_green_background));
            mainButton.setTextColor(ContextCompat.getColor(getContext(), R.color.desable_text_green));
        } else {
            mainButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.green_background));
            mainButton.setTextColor(ContextCompat.getColor(getContext(), R.color.background));
        }
        mainButton.setEnabled(value);
        mainButton.setClickable(value);
    }

    @Override
    public void setTextColor(int color) {
        mainButton.setTextColor(color);
    }

    @Override
    public void setBackground(Drawable value) {
        mainButton.setBackground(value);
    }

    @Override
    public void setBackgroundColor(int value) {
        mainButton.setBackgroundColor(value);
    }

    // Listener ------------------------------------------------------------------------------------
    public void setOnClickListener(OnClickListener listener) {
        mainButton.setOnClickListener(new DebounceClickListener(listener));
    }

}
