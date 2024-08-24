package com.example.screens.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.components.dialog.CustomDialogFragment;
import com.example.components.navMenu.BottomNavMenu;
import com.example.components.spinner.CustomProgressBarDialog;
import com.example.screens.R;
import com.example.screens.utils.SharedPreferencesManager;

import java.util.List;

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
        sharedPreferencesManager.setLogged(false);

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
