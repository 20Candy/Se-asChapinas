package com.example.components.bottomsheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.components.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet extends BottomSheetDialogFragment {

    // Atributos de la clase -----------------------------------------------------------------------
    private OnContinueClickListener mContinueListener;
    private OnCancelClickListener mCancelListener;
    private boolean isContinuePressed = false;


    // Metodos de ciclo de vida --------------------------------------------------------------------


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

    // Listeners -----------------------------------------------------------------------------------
    public interface OnContinueClickListener {
        void onContinueClick();
    }

    public interface OnCancelClickListener {
        void mCancelListener();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_layout, container, false);

        return view;
    }
}
