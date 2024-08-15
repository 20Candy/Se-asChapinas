package com.example.screens.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import com.example.components.dialog.CustomDialogFragment;
import com.example.components.navMenu.BottomNavMenu;
import com.example.screens.R;

import java.util.List;

public class BaseFragment extends Fragment {

    protected void navigateTo(View view, int destiny_id, Bundle bundle) {
//        hideCustomDialogProgress();
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



}
