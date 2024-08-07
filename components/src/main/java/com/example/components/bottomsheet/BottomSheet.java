package com.example.components.bottomsheet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.components.R;
import com.example.components.buttons.MainButton;
import com.example.components.buttons.TransparentButton;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet extends BottomSheetDialogFragment {

    private OnContinueClickListener mContinueListener;
    private OnCancelClickListener mCancelListener;
    private boolean isContinuePressed = false;

    public BottomSheet(OnCancelClickListener cancelListener, OnContinueClickListener continueListener) {
        mCancelListener = cancelListener;
        mContinueListener = continueListener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mCancelListener != null && !isContinuePressed) {
            mCancelListener.mCancelListener();
        }
        isContinuePressed = false;
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

        // Configurar el botón Relleno
        MainButton continueButton = view.findViewById(R.id.mainButton);
        continueButton.setOnClickListener(v -> {
            if (mContinueListener != null) {
                mContinueListener.onContinueClick();
            }
            dismiss(); // Cerrar el BottomSheet después de la acción
        });

        // Configurar el botón Transparente
        TransparentButton cancelButton = view.findViewById(R.id.secondButton);
        cancelButton.setOnClickListener(v -> {
            isContinuePressed = true;
            if (mCancelListener != null) {
                mCancelListener.mCancelListener();
            }
            dismiss(); // Cerrar el BottomSheet después de la acción
        });

        return view;
    }
}
