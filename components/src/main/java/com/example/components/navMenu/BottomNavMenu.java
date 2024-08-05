package com.example.components.navMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.components.R;

public class BottomNavMenu extends ConstraintLayout {

    public interface OnTabSelectedListener {
        void onTabSelected(int tabId);
    }

    // Definir constantes para los IDs de los tabs
    public static final int TAB_HOME = 1;
    public static final int TAB_TRANSLATE = 2;
    public static final int TAB_DICTIONARY = 3;
    public static final int TAB_PROFILE = 4;

    private LinearLayout llHome, llTranslate, llDictionary, llProfile;
    private ImageView imgHome, imgTranslate, imgDictionary, imgProfile, imgLineHome, imgLineTranslate, imgLineDictionary, imgLineProfile;
    private TextView tvHome, tvTranslate, tvDictionary, tvProfile;

    private OnTabSelectedListener tabSelectedListener;

    public BottomNavMenu(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public BottomNavMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BottomNavMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public BottomNavMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        inflate(getContext(), R.layout.bottom_nav_menu, this);

        llHome = findViewById(R.id.ll_home);
        llTranslate = findViewById(R.id.ll_translate);
        llDictionary = findViewById(R.id.ll_dictionary);
        llProfile = findViewById(R.id.ll_profile);

        imgHome = findViewById(R.id.img_home);
        imgTranslate = findViewById(R.id.img_translate);
        imgDictionary = findViewById(R.id.img_dictionary);
        imgProfile = findViewById(R.id.img_profile);

        imgLineHome = findViewById(R.id.img_line_home);
        imgLineTranslate = findViewById(R.id.img_line_translate);
        imgLineDictionary = findViewById(R.id.img_line_dictionary);
        imgLineProfile = findViewById(R.id.img_line_profile);

        tvHome = findViewById(R.id.tv_home);
        tvTranslate = findViewById(R.id.tv_translate);
        tvDictionary = findViewById(R.id.tv_dictionary);
        tvProfile = findViewById(R.id.tv_profile);

        setupListeners();
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        this.tabSelectedListener = listener;
    }

    private void setupListeners() {
        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveTab(TAB_HOME);
            }
        });

        llTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveTab(TAB_TRANSLATE);
            }
        });

        llDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveTab(TAB_DICTIONARY);
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveTab(TAB_PROFILE);
            }
        });
    }

    public void setActiveTab(int tabId) {
        resetTabs();

        switch (tabId) {
            case TAB_HOME:
                imgHome.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.video_bold));
                tvHome.setTextColor(ContextCompat.getColor(getContext(), R.color.base_blue));
                imgLineHome.setVisibility(View.VISIBLE);
                break;
            case TAB_TRANSLATE:
                imgTranslate.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.translate_bold));
                tvTranslate.setTextColor(ContextCompat.getColor(getContext(), R.color.base_blue));
                imgLineTranslate.setVisibility(View.VISIBLE);
                break;
            case TAB_DICTIONARY:
                imgDictionary.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.search_bold));
                tvDictionary.setTextColor(ContextCompat.getColor(getContext(), R.color.base_blue));
                imgLineDictionary.setVisibility(View.VISIBLE);
                break;
            case TAB_PROFILE:
                imgProfile.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.profile_bold));
                tvProfile.setTextColor(ContextCompat.getColor(getContext(), R.color.base_blue));
                imgLineProfile.setVisibility(View.VISIBLE);
                break;
        }

        if (tabSelectedListener != null) {
            tabSelectedListener.onTabSelected(tabId);
        }
    }

    private void resetTabs() {
        imgHome.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.video_normal));
        imgTranslate.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.translate_normal));
        imgDictionary.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.search_normal));
        imgProfile.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.profile_normal));

        tvHome.setTextColor(ContextCompat.getColor(getContext(), R.color.light_blue));
        tvTranslate.setTextColor(ContextCompat.getColor(getContext(), R.color.light_blue));
        tvDictionary.setTextColor(ContextCompat.getColor(getContext(), R.color.light_blue));
        tvProfile.setTextColor(ContextCompat.getColor(getContext(), R.color.light_blue));

        imgLineHome.setVisibility(View.INVISIBLE);
        imgLineTranslate.setVisibility(View.INVISIBLE);
        imgLineDictionary.setVisibility(View.INVISIBLE);
        imgLineProfile.setVisibility(View.INVISIBLE);
    }
}
