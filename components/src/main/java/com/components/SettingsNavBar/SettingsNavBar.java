package com.components.SettingsNavBar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.components.R;

public class SettingsNavBar extends ConstraintLayout {

    private ImageView imgRecord;
    private ImageView imgRecordSelector;
    private ImageView imgTranslate;
    private ImageView imgTranslateSelector;

    public static final String VIDEO = "video";
    public static final String TRANSLATE = "translate";

    private OnTabSelectedListener tabSelectedListener;

    public SettingsNavBar(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public SettingsNavBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SettingsNavBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public SettingsNavBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        inflate(getContext(), R.layout.settings_nav_menu, this);

        imgRecord = findViewById(R.id.imgRecord);
        imgRecordSelector = findViewById(R.id.imgRecordSelector);
        imgTranslate = findViewById(R.id.imgTranslate);
        imgTranslateSelector = findViewById(R.id.imgTranslateSelector);

        imgRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveTab(R.id.imgRecord);
            }
        });

        imgTranslate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveTab(R.id.imgTranslate);
            }
        });

        // Initialize with the first tab selected
        setActiveTab(R.id.imgRecord);
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        this.tabSelectedListener = listener;
    }

    public void setActiveTab(int tabId) {
        String selected = "";
        if (tabId == R.id.imgRecord) {
            imgRecord.setImageResource(R.drawable.video_bold);
            imgRecordSelector.setVisibility(View.VISIBLE);

            imgTranslate.setImageResource(R.drawable.translate_normal);
            imgTranslateSelector.setVisibility(View.INVISIBLE);

            selected = this.VIDEO;

        } else if (tabId == R.id.imgTranslate) {
            imgRecord.setImageResource(R.drawable.video_normal);
            imgRecordSelector.setVisibility(View.INVISIBLE);

            imgTranslate.setImageResource(R.drawable.translate_bold);
            imgTranslateSelector.setVisibility(View.VISIBLE);

            selected = this.TRANSLATE;
        }

        if (tabSelectedListener != null) {
            tabSelectedListener.onTabSelected(selected);
        }
    }

    public interface OnTabSelectedListener {
        void onTabSelected(String tabId);
    }
}
