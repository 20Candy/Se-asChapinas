package com.components.challenge;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.components.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChallengeCards extends ConstraintLayout {

    private ImageView imgAqua, imgBlue, imgYellow, imgGreen;
    private List<Integer> imageResources; // Lista de todos los recursos de imágenes
    private int correctImageRes; // Recurso de la imagen correcta

    private Callback callback;

    public ChallengeCards(@NonNull Context context) {
        super(context);
        initView();
    }

    public ChallengeCards(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public ChallengeCards(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    public ChallengeCards(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();

    }


    private void initView(){
        inflate(getContext(), R.layout.challenge_cards, this);
        imgAqua = findViewById(R.id.imgAqua);
        imgBlue = findViewById(R.id.imgBlue);
        imgYellow = findViewById(R.id.imgYellow);
        imgGreen = findViewById(R.id.imgGreen);

    }

    public void startGame() {
        if (imageResources == null || correctImageRes == 0) {
            throw new IllegalStateException("Image resources and correct image resource must be set before starting the game.");
        }
        setupGame();
    }

    private void setupGame() {
        // Asegurar que la lista de recursos no incluya la imagen correcta
        imageResources.remove((Integer) correctImageRes);

        // Seleccionar 3 imágenes al azar de la lista
        Collections.shuffle(imageResources);
        List<Integer> selectedImages = imageResources.subList(0, 3);

        // Agregar la imagen correcta y mezclar la lista final
        selectedImages.add(correctImageRes);
        Collections.shuffle(selectedImages);

        // Asignar las imágenes a los ImageView
        imgAqua.setImageResource(selectedImages.get(0));
        imgBlue.setImageResource(selectedImages.get(1));
        imgYellow.setImageResource(selectedImages.get(2));
        imgGreen.setImageResource(selectedImages.get(3));

        // Configurar listeners para la selección de imágenes
        setupListeners();
    }


    private void setupListeners() {
        View.OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout clickedLayout = (ConstraintLayout) v;
                ImageView imageView = (ImageView) clickedLayout.getChildAt(0);
                if (imageView.getDrawable().getConstantState().equals(getResources().getDrawable(correctImageRes).getConstantState())) {
                    // Imagen correcta
                    handleCorrectAnswer();
                } else {
                    // Imagen incorrecta
                    handleIncorrectAnswer(clickedLayout);
                }
            }
        };

        findViewById(R.id.card_aqua).setOnClickListener(listener);
        findViewById(R.id.card_blue).setOnClickListener(listener);
        findViewById(R.id.card_yellow).setOnClickListener(listener);
        findViewById(R.id.card_green).setOnClickListener(listener);
    }

    private void handleIncorrectAnswer(ConstraintLayout clickedLayout) {
        // Temporalmente ajustar el brillo para la animación
        adjustBrightness(false, clickedLayout);
        vibrateCard(clickedLayout, () -> adjustBrightness(true, clickedLayout));
    }

    private void handleCorrectAnswer() {
        // Obtener los views de las tarjetas
        ConstraintLayout cardAqua = findViewById(R.id.card_aqua);
        ConstraintLayout cardBlue = findViewById(R.id.card_blue);
        ConstraintLayout cardYellow = findViewById(R.id.card_yellow);
        ConstraintLayout cardGreen = findViewById(R.id.card_green);

        // Lista de todas las tarjetas para facilitar la iteración
        ConstraintLayout[] allCards = {cardAqua, cardBlue, cardYellow, cardGreen};

        // Variable para almacenar la tarjeta correcta
        ConstraintLayout correctCard = null;

        // Iterar sobre todas las tarjetas para identificar y ocultar las incorrectas
        for (ConstraintLayout card : allCards) {
            ImageView imageView = (ImageView) card.getChildAt(0);
            if (imageView.getDrawable().getConstantState().equals(getResources().getDrawable(correctImageRes).getConstantState())) {
                correctCard = card;
            } else {
                card.setVisibility(View.GONE);  // Ocultar las tarjetas incorrectas
            }
        }

        // Animar la tarjeta correcta si existe
        if (correctCard != null) {
            animateCard(correctCard);
        }

        // Notificar al fragment o actividad
        if (callback != null) {
            callback.onCorrectAnswer();
        }
    }


    private void animateCard(ConstraintLayout card) {
        // Acceder a los parámetros de layout del card
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) card.getLayoutParams();

        // Eliminar márgenes
        params.setMargins(0, 0, 0, 0);

        // Establecer las restricciones para que la tarjeta esté vinculada a todos los lados del padre
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

        // Aplicar los nuevos parámetros al card
        card.setLayoutParams(params);

        // Animación para escalar la tarjeta
        card.animate()
                .scaleX(1.5f)  // Aumentar el tamaño en X
                .scaleY(1.5f)  // Aumentar el tamaño en Y
                .setDuration(300)  // Duración de la animación en milisegundos
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        card.bringToFront();  // Traer al frente después de la animación
                    }
                })
                .start();
    }



    private void adjustBrightness(boolean brighten, View... views) {
        float opacity = brighten ? 1.0f : 0.5f;

        for (View view : views) {
            if (view.getBackground() != null) {
                view.setAlpha(opacity);
            }
        }
    }


    private void vibrateCard(View view, Runnable onAnimationEnd) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        animator.setDuration(500);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimationEnd != null) {
                    onAnimationEnd.run(); // Restablecer el brillo una vez que termine la animación
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (onAnimationEnd != null) {
                    onAnimationEnd.run(); // Asegurarse de restablecer el brillo incluso si la animación es cancelada
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animator.start();
    }

    public int getCorrectImageRes() {
        return correctImageRes;
    }

    public void setCorrectImageRes(int correctImageRes) {
        this.correctImageRes = correctImageRes;
    }

    public List<Integer> getImageResources() {
        return imageResources;
    }

    public void setImageResources(List<Integer> imageResources) {
        this.imageResources = imageResources;
    }

    public interface Callback {
        void onCorrectAnswer();
        void onIncorrectAnswer();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
