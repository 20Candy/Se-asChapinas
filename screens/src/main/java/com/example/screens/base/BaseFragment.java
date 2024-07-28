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

import com.example.screens.R;

import java.util.List;

public class BaseFragment extends Fragment {

    protected void navigateTo(View view, int destiny_id, Bundle bundle) {
        hideCustomDialogProgress();
        Navigation.findNavController(view).navigate(destiny_id, bundle);
    }

    protected void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    protected void clearBackStackTo(View view, int destiny_id) {
        Navigation.findNavController(view).popBackStack(destiny_id, false);
    }


    public Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            return fragments.get(fragments.size() - 1); // Obtén el último fragmento de la lista
        }
        return null;
    }


    protected void showCustomDialogMessage(String message, String title, String actionOkText) {
//        BasicDialogFragment dialog = new BasicDialogFragment(title, message, actionOkText);
//        dialog.show(getChildFragmentManager(), "MyCustomDialog");
    }

    protected void hideCustomDialogProgress() {
//
//        if (progressBarDialog != null && progressBarDialog.isShowing()) {
//            progressBarDialog.dismiss();
//
//            Fragment currentFragment = getCurrentFragment();
//            if (getInteraction() != null && !(currentFragment instanceof LoginFragment))
//                getInteraction().onStartTimer();
//        }
    }

    protected void makeToast(String message) {
//        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
//        View view = toast.getView();
//        if (view == null) { // Android 11
//            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
//        } else {
////            view.setBackgroundResource(R.drawable.background_toast);
//            TextView text = (TextView) view.findViewById(android.R.id.message);
//            text.setTextColor(getResources().getColor(com.example.components.R.color.background, null));
//            toast.show();
//        }
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
