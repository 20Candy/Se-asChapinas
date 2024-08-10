package com.example.components.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.components.R;

public class SmallButton extends ConstraintLayout implements IButtons {

    //Atributos del componente ---------------------------------------------------------------------
    private Button mainButton;

    // Metodos de ContraintLayout ------------------------------------------------------------------
    public SmallButton(Context context) {
        super(context);
        initView();
    }

    public SmallButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        declareAttr(context, attrs);
    }

    public SmallButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
        declareAttr(context, attrs);
    }

    private void initView() {
        inflate(getContext(), R.layout.small_button, this);
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
        Drawable image = array.getDrawable(R.styleable.button_image);


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

        if(image != null){
            setButtonImage(image);
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

    public void setButtonImage(Drawable drawable) {
        if (drawable != null) {
            // Define los márgenes como quieras, aquí se establece solo el margen izquierdo
            int left = 8; // 8px de margen izquierdo
            Drawable[] layers = {drawable};
            LayerDrawable layerDrawable = new LayerDrawable(layers);

            // Convertir 8px a dp para mantener la consistencia en diferentes densidades de pantalla
            float density = getContext().getResources().getDisplayMetrics().density;
            int leftInset = (int) (left * density);

            // Crear un nuevo drawable con margen (Insets)
            InsetDrawable insetDrawable = new InsetDrawable(layerDrawable, leftInset, 0, 0, 0);

            // Establece el drawable con el margen aplicado
            mainButton.setCompoundDrawablesWithIntrinsicBounds(insetDrawable, null, null, null);
        } else {
            mainButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    // Listener ------------------------------------------------------------------------------------
    public void setOnClickListener(OnClickListener listener) {
        mainButton.setOnClickListener(new DebounceClickListener(listener));
    }

}
