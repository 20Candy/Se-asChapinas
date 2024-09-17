package com.components.inputs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.components.R;

public class PasswordInput extends ConstraintLayout implements IInputs {

    // Atributos del componente --------------------------------------------------------------------
    EditText inputText;
    TextView label, alert;
    LinearLayout container;
    ImageView eye;

    private boolean validate = true;


    // Metodos de ContraintLayout ------------------------------------------------------------------
    public PasswordInput(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(getContext(), R.layout.password_input, this);
        inputText = findViewById(R.id.et_input_text);
        label = findViewById(R.id.tv_label);
        alert = findViewById(R.id.tvAlert);
        container = findViewById(R.id.ll_input);
        eye = findViewById(R.id.imgEye);

        // Configurando el tipo de entrada como contraseña
        inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputText.setTypeface(ResourcesCompat.getFont(getContext(), R.font.nunito_medium));
        inputText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.light_blue));

        applyAttributes(context, attrs);
        setInputValidations();
        setupEyeIcon();

    }

    private void applyAttributes(Context context, AttributeSet attrs) {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.input, 0, 0);
        try {
            setLabel(attributes.getString(R.styleable.input_label));
            setHint(attributes.getString(R.styleable.input_hint));
            setValidate(attributes.getBoolean(R.styleable.input_validate, true));
        } finally {
            attributes.recycle();
        }
    }

    // Metodos de IButtons -------------------------------------------------------------------------
    @Override
    public void setInputValidations() {
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validate) {
                    if (isInputValid()) {
                        alert.setVisibility(GONE);
                    } else {
                        alert.setVisibility(VISIBLE);
                        alert.setText(R.string.wrong_password_alert);
                    }

                }

                if (validationListener != null) {
                    validationListener.onInputChanged(isInputValid());
                }

            }
        });
    }

    @Override
    public void setText(String value) {
        inputText.setText(value);
    }

    @Override
    public void setLabel(String value) {
        label.setText(value);
    }

    @Override
    public void setHint(String value) {
        inputText.setHint(value);
    }

    @Override
    public void setAlert(String value) {
        alert.setText(value);
    }

    @Override
    public void setBackground(Drawable value) {
        container.setBackground(value);
    }

    @Override
    public boolean isInputValid() {
        String password = inputText.getText().toString();
        return password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*.])(?=.*[A-Z]).{8,}$");
    }

    //Metodod privados -----------------------------------------------------------------------------
    private void setupEyeIcon() {
        eye.setOnClickListener(new View.OnClickListener() {
            private boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Ocultar contraseña
                    inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    inputText.setTypeface(ResourcesCompat.getFont(getContext(), R.font.nunito_medium));
                    eye.setImageResource(R.drawable.eye_closed);
                    inputText.setSelection(inputText.getText().length());
                } else {
                    // Mostrar contraseña
                    inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    inputText.setTypeface(ResourcesCompat.getFont(getContext(), R.font.nunito_medium));
                    eye.setImageResource(R.drawable.eye_open);
                    inputText.setSelection(inputText.getText().length());
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });
    }

    // Listener ------------------------------------------------------------------------------------
    private InputValidationListener validationListener;

    @Override
    public void setValidationListener(InputValidationListener listener) {
        this.validationListener = listener;
    }

    // Metodos propios  ----------------------------------------------------------------------------
    public void setValidate(Boolean value) {
        this.validate = value;
    }

    public boolean isEmpty(){
        return inputText.getText().toString().isEmpty();
    }

    public String getPassword(){
        return inputText.getText().toString();
    }

}
