package com.example.components.cardDictionary;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.components.R;

public class CardDictionary extends ConstraintLayout {

    //Atributos del componente ---------------------------------------------------------------------
    private ImageView imgHeart,imgTurn, imgHeartBack, imgTurnBack;
    private TextView tvTitle;
    private CardView card;
    private ConstraintLayout front, back;

    private AnimatorSet frontAnim, backAnim;
    private boolean isFront = true;

    // Metodos de ContraintLayout ------------------------------------------------------------------
    public CardDictionary(@NonNull Context context) {
        super(context);
    }

    public CardDictionary(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public CardDictionary(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    public CardDictionary(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();

    }

    private void initView() {
        inflate(getContext(), R.layout.card_dictionary, this);
        front = findViewById(R.id.frente);
        back = findViewById(R.id.atras);
        card = findViewById(R.id.card);
        imgHeart = findViewById(R.id.imgHeart);
        imgTurn = findViewById(R.id.imgTurn);
        tvTitle = findViewById(R.id.tvTitle);
        imgHeartBack = findViewById(R.id.imgHeartBack);
        imgTurnBack = findViewById(R.id.imgTurnBack);



        float scale = getContext().getResources().getDisplayMetrics().density;
        front.setCameraDistance(8000 * scale);
        back.setCameraDistance(8000 * scale);

        frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_animator);
        backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.back_animator);

        setupListeners();
    }


    // Getters y Setters ---------------------------------------------------------------------------
    public ImageView getImgHeart() {
        return imgHeart;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String title) {
        tvTitle.setText(title);
    }


    // Listener ------------------------------------------------------------------------------------

    private void setupListeners() {
        card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFront) {
                    // Crea y establece un GradientDrawable transparente para la animación
                    GradientDrawable transparentDrawable = new GradientDrawable();
                    transparentDrawable.setColor(Color.TRANSPARENT);
                    transparentDrawable.setCornerRadius(10 * getResources().getDisplayMetrics().density);
                    card.setBackgroundDrawable(transparentDrawable);

                    frontAnim.setTarget(front);
                    backAnim.setTarget(back);

                    backAnim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // Crea y establece un GradientDrawable con color de fondo original después de la animación
                            GradientDrawable backgroundDrawable = new GradientDrawable();
                            backgroundDrawable.setColor(getResources().getColor(R.color.background));
                            backgroundDrawable.setCornerRadius(10 * getResources().getDisplayMetrics().density);
                            card.setBackgroundDrawable(backgroundDrawable);
                            backAnim.removeListener(this);
                        }
                    });

                    frontAnim.start();
                    backAnim.start();
                    isFront = false;
                } else {
                    // Crea y establece un GradientDrawable transparente para la animación
                    GradientDrawable transparentDrawable = new GradientDrawable();
                    transparentDrawable.setColor(Color.TRANSPARENT);
                    transparentDrawable.setCornerRadius(10 * getResources().getDisplayMetrics().density);
                    card.setBackgroundDrawable(transparentDrawable);

                    frontAnim.setTarget(back);
                    backAnim.setTarget(front);

                    frontAnim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // Crea y establece un GradientDrawable con color de fondo original después de la animación
                            GradientDrawable backgroundDrawable = new GradientDrawable();
                            backgroundDrawable.setColor(getResources().getColor(R.color.background));
                            backgroundDrawable.setCornerRadius(10 * getResources().getDisplayMetrics().density);
                            card.setBackgroundDrawable(backgroundDrawable);
                            frontAnim.removeListener(this);
                        }
                    });

                    backAnim.start();
                    frontAnim.start();
                    isFront = true;
                }
            }
        });

        imgHeart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heartClickListener != null) {
                    heartClickListener.onHeartClicked();
                }
            }
        });
    }

    public interface HeartClickListener {
        void onHeartClicked();
    }

    private HeartClickListener heartClickListener;

    public void setHeartClickListener(HeartClickListener listener) {
        this.heartClickListener = listener;
    }

}
