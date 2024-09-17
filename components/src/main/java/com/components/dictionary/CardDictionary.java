package com.components.dictionary;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.components.R;

public class CardDictionary extends ConstraintLayout {

    // Atributos del componente --------------------------------------------------------------------
    private ImageView imgHeart, imgTurn, imgHeartBack, imgTurnBack;
    private TextView tvTitle;
    private CardView card;
    private ConstraintLayout front, back;

    private ImageView imgLensegua, imgCard;

    private AnimatorSet frontAnim, backAnim;
    private boolean isFront = true;
    private boolean heartFull = false;


    // Métodos de ConstraintLayout -----------------------------------------------------------------
    public CardDictionary(@NonNull Context context) {
        super(context);
        initView();
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
        imgLensegua = findViewById(R.id.imgLensegua);
        imgCard = findViewById(R.id.imgCard);

        float scale = getContext().getResources().getDisplayMetrics().density;
        front.setCameraDistance(8000 * scale);
        back.setCameraDistance(8000 * scale);

        frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_animator);
        backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.back_animator);

        setupListeners();
    }

    // Métodos para configurar el contenido del CardDictionary --------------------------------------

    public void setImgLensegua(Drawable drawable){
        imgLensegua.setImageDrawable(drawable);
    }

    public void setImgCard(Drawable drawable){
        imgCard.setImageDrawable(drawable);
    }

    public void setTvTitle(String title) {
        tvTitle.setText(title);
    }

    public void setFavorite(Boolean favorite){
        if(favorite){
            imgHeart.setImageDrawable(getResources().getDrawable(R.drawable.full_heart));
        }else{
            imgHeart.setImageDrawable(getResources().getDrawable(R.drawable.heart));
        }
    }

    // Listener ------------------------------------------------------------------------------------
    private void setupListeners() {
        View.OnClickListener heartClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleHeartState();
            }
        };

        imgHeart.setOnClickListener(heartClickListener);
        imgHeartBack.setOnClickListener(heartClickListener);

        View.OnClickListener turnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCard();
            }
        };

        imgTurn.setOnClickListener(turnClickListener);
        imgTurnBack.setOnClickListener(turnClickListener);
    }

    private void toggleHeartState() {
        // Cambia el estado del corazón
        heartFull = !heartFull;

        // Actualiza ambos corazones
        updateHeartImages();

        // Notifica a cualquier listener externo si es necesario
        if (this.heartClickListener != null) {
            this.heartClickListener.onHeartClicked(heartFull);
        }
    }

    private void updateHeartImages() {
        if (heartFull) {
            imgHeart.setImageDrawable(getResources().getDrawable(R.drawable.full_heart));
            imgHeartBack.setImageDrawable(getResources().getDrawable(R.drawable.full_heart));
        } else {
            imgHeart.setImageDrawable(getResources().getDrawable(R.drawable.heart));
            imgHeartBack.setImageDrawable(getResources().getDrawable(R.drawable.heart));
        }
    }

    private void toggleCard() {
        if (isFront) {
            prepareCardForAnimation();
            frontAnim.setTarget(front);
            backAnim.setTarget(back);
            startAnimationSequence(false);
        } else {
            prepareCardForAnimation();
            frontAnim.setTarget(back);
            backAnim.setTarget(front);
            startAnimationSequence(true);
        }
    }

    private void prepareCardForAnimation() {
        GradientDrawable transparentDrawable = new GradientDrawable();
        transparentDrawable.setColor(Color.TRANSPARENT);
        transparentDrawable.setCornerRadius(10 * getResources().getDisplayMetrics().density);
        card.setBackgroundDrawable(transparentDrawable);
    }

    private void startAnimationSequence(boolean returningToFront) {
        AnimatorListenerAdapter onAnimationEndListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                GradientDrawable backgroundDrawable = new GradientDrawable();
                backgroundDrawable.setColor(getResources().getColor(R.color.background));
                backgroundDrawable.setCornerRadius(10 * getResources().getDisplayMetrics().density);
                card.setBackgroundDrawable(backgroundDrawable);
                if (returningToFront) {
                    frontAnim.removeListener(this);
                } else {
                    backAnim.removeListener(this);
                }
                isFront = returningToFront;
            }
        };

        if (returningToFront) {
            frontAnim.addListener(onAnimationEndListener);
        } else {
            backAnim.addListener(onAnimationEndListener);
        }

        frontAnim.start();
        backAnim.start();
    }

    // Interface para eventos de clic en el corazón ------------------------------------------------
    public interface HeartClickListener {
        void onHeartClicked(boolean heartFull);
    }

    private HeartClickListener heartClickListener;

    public void setHeartClickListener(HeartClickListener listener) {
        this.heartClickListener = listener;
    }

    public TextView getTvLetter() {
        return findViewById(R.id.tvLetter);
    }

}
