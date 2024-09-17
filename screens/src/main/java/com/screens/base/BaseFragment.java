package com.screens.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.components.dialog.CustomDialogFragment;
import com.components.spinner.CustomProgressBarDialog;
import com.screens.R;
import com.screens.utils.SharedPreferencesManager;

public class BaseFragment extends Fragment {

    private CustomProgressBarDialog progressBarDialog;


    protected void navigateTo(View view, int destiny_id, Bundle bundle) {
        hideCustomDialogProgress();
        Navigation.findNavController(view).navigate(destiny_id, bundle);
    }

    protected void clearBackStackTo(View view, int destiny_id) {
        Navigation.findNavController(view).popBackStack(destiny_id, false);
    }



    protected void showCustomDialogMessage(String message, String title, String actionOkText, String actionCancelText, CustomDialogFragment.OnConfirmListener confirmListener) {
        CustomDialogFragment dialog = new CustomDialogFragment(
                title,
                message,
                actionOkText,
                actionCancelText,
                confirmListener
        );
        dialog.show(getChildFragmentManager(), "MyCustomDialog");
    }

    protected void showCustomDialogMessage(String message, String title, String actionOkText, String actionCancelText, CustomDialogFragment.OnConfirmListener confirmListener, int color) {
        CustomDialogFragment dialog = new CustomDialogFragment(
                title,
                message,
                actionOkText,
                actionCancelText,
                confirmListener,
                color
        );
        dialog.show(getChildFragmentManager(), "MyCustomDialog");
    }

    protected void showCustomDialogMessage(String message, String title, String actionOkText, String actionCancelText, CustomDialogFragment.OnConfirmListener confirmListener, CustomDialogFragment.OnCancelListener cancelListener, int color) {
        CustomDialogFragment dialog = new CustomDialogFragment(
                title,
                message,
                actionOkText,
                actionCancelText,
                confirmListener,
                cancelListener,
                color
        );
        dialog.show(getChildFragmentManager(), "MyCustomDialog");
    }


    public interface OnBackPressedAction {
        void onBackPressedAction();
    }

    protected void onBackPressed(OnBackPressedAction listener) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                listener.onBackPressedAction();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void goToLandingFragment(View view) {
        SharedPreferencesManager sharedPreferencesManager;
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        sharedPreferencesManager.setIdUsuario("");

        NavController navController = Navigation.findNavController(view);
        navController.setGraph(R.navigation.main_nav);

    }


    protected void showCustomDialogProgress(Context context) {
        progressBarDialog = new CustomProgressBarDialog(requireActivity());
        if (context != null && !progressBarDialog.isShowing()) {
            try {
                progressBarDialog.setCancelable(false);
                progressBarDialog.show();
            } catch (Exception e) {
                Log.e("Error", "showCustomDialogProgress: ", e);
            }
        }
    }

    protected void hideCustomDialogProgress() {

        if (progressBarDialog != null && progressBarDialog.isShowing()) {
            progressBarDialog.dismiss();

        }
    }





}
