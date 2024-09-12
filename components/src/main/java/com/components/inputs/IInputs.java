package com.components.inputs;

import android.graphics.drawable.Drawable;

public interface IInputs {

    void setText(String value);
    void setLabel(String value);
    void setHint(String value);
    void setAlert(String value);
    void setBackground(Drawable value);
    void setInputValidations();
    boolean isInputValid();


    void setValidationListener(InputValidationListener listener);
    interface InputValidationListener {
        void onInputChanged(boolean isValid);
    }

}
