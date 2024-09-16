package com.components.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.components.R;

public class CustomDialogFragment extends DialogFragment {

    private OnConfirmListener confirmListener;

    private OnCancelListener cancelListener;
    private String mTitle, mContent, mConfirmText, mCancelText;

    private TextView mTextViewTitle, mTextViewContent, mTextViewConfirm, mTextViewCancel;

    private int mColor = -1;

    private ConstraintLayout main;

    // Constructor que ahora sólo necesita el confirmListener
    public CustomDialogFragment(String title, String content, String confirmText, String cancelText, OnConfirmListener confirmListener) {
        mTitle = title;
        mContent = content;
        mConfirmText = confirmText;
        mCancelText = cancelText;
        this.confirmListener = confirmListener;
    }

    public CustomDialogFragment(String title, String content, String confirmText, String cancelText, OnConfirmListener confirmListener, int color) {
        mTitle = title;
        mContent = content;
        mConfirmText = confirmText;
        mCancelText = cancelText;
        this.confirmListener = confirmListener;
        mColor = color;

    }

    public CustomDialogFragment(String title, String content, String confirmText, String cancelText, OnConfirmListener confirmListener, OnCancelListener cancelListener ,int color) {
        mTitle = title;
        mContent = content;
        mConfirmText = confirmText;
        mCancelText = cancelText;
        this.confirmListener = confirmListener;
        this.cancelListener = cancelListener;
        mColor = color;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_dialog, container, false);
        mTextViewTitle = view.findViewById(R.id.tvTitle);
        mTextViewContent = view.findViewById(R.id.tvContent);
        mTextViewConfirm = view.findViewById(R.id.btnConfirmar);
        mTextViewCancel = view.findViewById(R.id.btnCancelar);
        main = view.findViewById(R.id.clMain);

        mTextViewTitle.setText(mTitle);
        mTextViewContent.setText(mContent);
        mTextViewConfirm.setText(mConfirmText);

        if(mCancelText.equals("")){
            mTextViewCancel.setVisibility(View.GONE);
        }else{
            mTextViewCancel.setText(mCancelText);

        }

        if(mColor!= -1){
            mTextViewTitle.setTextColor(mColor);
        }

        // Configurar ambos botones para cerrar el diálogo
        mTextViewConfirm.setOnClickListener(v -> {
            if (confirmListener != null) {
                confirmListener.onConfirm();
            }
            dismiss();
        });

        mTextViewCancel.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onCancel();
            }
            dismiss();
        });



        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        confirmListener = null;
    }

    public interface OnConfirmListener {
        void onConfirm();
    }
    public interface OnCancelListener {
        void onCancel();
    }

}
