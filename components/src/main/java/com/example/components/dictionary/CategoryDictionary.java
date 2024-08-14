package com.example.components.dictionary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.components.R;

public class CategoryDictionary extends ConstraintLayout {

    private boolean isSelected = false;
    private ImageView heartImageView;
    private TextView text;

    private ConstraintLayout clItem;

    public CategoryDictionary(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CategoryDictionary(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CategoryDictionary(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CategoryDictionary(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.catergory_dictionary, this);

        heartImageView = findViewById(R.id.heart);
        text = findViewById(R.id.tvCategory);
        clItem = findViewById(R.id.clItem);

        // Configurar el listener de clics
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleState(context);
            }
        });
    }

    private void toggleState(Context context) {
        setSelected(!isSelected);
    }

    public void setSelected(boolean selected) {
        Context context = getContext();
        if (selected) {
            clItem.setBackground(ContextCompat.getDrawable(context, R.drawable.blue_full_background));
            heartImageView.setImageResource(R.drawable.white_heart);
            text.setTextColor(ContextCompat.getColor(context, R.color.background));

        } else {
            clItem.setBackground(ContextCompat.getDrawable(context, R.drawable.blue_transprant_button));
            heartImageView.setImageResource(R.drawable.heart);
            text.setTextColor(ContextCompat.getColor(context, R.color.base_blue));

        }
        isSelected = selected;
    }
}
