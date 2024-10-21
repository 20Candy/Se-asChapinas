package com.components.bottomsheet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.components.R;
import com.components.buttons.MainButton;
import com.components.buttons.TransparentButton;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet extends BottomSheetDialogFragment {

    private OnContinueClickListener mContinueListener;
    private OnCancelClickListener mCancelListener;

    private String mtitle, mcontent, mbutton1, mbutton2;
    private Drawable mimage;
    private boolean isContinuePressed = false;
    private boolean showEdittext;
    private String report = "";

    public BottomSheet(OnCancelClickListener cancelListener, OnContinueClickListener continueListener) {
        mCancelListener = cancelListener;
        mContinueListener = continueListener;
    }

    public BottomSheet(OnCancelClickListener cancelListener, OnContinueClickListener continueListener, Drawable image) {
        mCancelListener = cancelListener;
        mContinueListener = continueListener;
        mimage = image;
    }

    public BottomSheet(OnCancelClickListener cancelListener, OnContinueClickListener continueListener, String title, String content, String button1, String button2) {
        mCancelListener = cancelListener;
        mContinueListener = continueListener;
        mtitle = title;
        mcontent = content;
        mbutton1 = button1;
        mbutton2 = button2;

    }

    public BottomSheet(OnCancelClickListener cancelListener, OnContinueClickListener continueListener, String title, String content, String button1, String button2, Boolean showEdittext) {
        mCancelListener = cancelListener;
        mContinueListener = continueListener;
        mtitle = title;
        mcontent = content;
        mbutton1 = button1;
        mbutton2 = button2;
        this.showEdittext = showEdittext;
    }


    // Interfaces para los listeners
    public interface OnContinueClickListener {
        void onContinueClick();
    }

    public interface OnCancelClickListener {
        void mCancelListener();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme); // Aplicar el estilo personalizado
        setCancelable(true);
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_layout, container, false);

        TextView titleView = view.findViewById(R.id.tvTtitle);
        TextView contentView = view.findViewById(R.id.tvContent);
        MainButton button1View = view.findViewById(R.id.mainButton);
        TransparentButton button2View = view.findViewById(R.id.secondButton);
        EditText editText = view.findViewById(R.id.tvReport);

        // Configurar título, contenido y textos de botones si no son nulos
        if (mtitle != null) {
            titleView.setText(mtitle);
        }
        if (mcontent != null) {
            contentView.setText(mcontent);
        }
        if (mbutton1 != null) {
            button1View.setText(mbutton1);
        }
        if (mbutton2 != null) {
            button2View.setText(mbutton2);
        }
        if (mimage != null) {
            button1View.setButtonImage(mimage);
        }

        // Configurar el boton transparente para continuar
        button2View.setOnClickListener(v -> {
            if (mContinueListener != null) {
                mContinueListener.onContinueClick();
            }
            dismiss(); // Cerrar el BottomSheet después de la acción
        });

        // Configurar el botón relleno para cerrar
        button1View.setOnClickListener(v -> {
            isContinuePressed = true;
            if (mCancelListener != null) {
                mCancelListener.mCancelListener();
                this.report = String.valueOf(editText.getText());
            }
            dismiss(); // Cerrar el BottomSheet después de la acción
        });

        // Mostrar eddittext, no text
        editText.setVisibility(showEdittext ? View.VISIBLE : View.GONE);

        return view;
    }

    // Getter Setter


    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
