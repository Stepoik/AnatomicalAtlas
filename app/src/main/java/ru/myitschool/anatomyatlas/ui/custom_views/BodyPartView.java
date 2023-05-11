package ru.myitschool.anatomyatlas.ui.custom_views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.myitschool.anatomyatlas.R;

public class BodyPartView extends androidx.appcompat.widget.AppCompatImageView {
    private Bitmap bitmap;
    private Sprite sprite;
    private OnClickListener clickListener;
    private String name;
    private String infoText;
    private ValueAnimator animator;
    private boolean animated = false;
    private boolean useAnimation = false;

    public BodyPartView(Context context) {
        super(context);
    }

    public BodyPartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BodyPartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        if (!Build.MANUFACTURER.contains("HUAWEI")){
            useAnimation = true;
        }
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StrangeView, 0, 0);
        name = a.getString(R.styleable.StrangeView_android_text);
        infoText = a.getString(R.styleable.StrangeView_android_tooltipText);
        Drawable drawable = getDrawable();
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            } else {
                bitmap = Bitmap.createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888
                );
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            }
            sprite = new Sprite(bitmap);
        }
        configureAnimator();
    }

    public void onClick(MotionEvent event) {
        if (startPress(event)) {
            if (clickListener != null) {
                if (getAlpha() > 0.5 && ((View)getParent()).getAlpha() > 0.5) {
                    clickListener.onClick(this);
                }
            }
        }
    }

    public boolean startPress(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return sprite.isPressed((int) (x * (float) bitmap.getWidth() / getWidth()), (int) (y * (float) bitmap.getHeight() / getHeight()))
                && getAlpha() > 0.5
                && ((View)getParent()).getAlpha() > 0.5;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        clickListener = l;
        setClickable(true);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return startPress(event);
            case MotionEvent.ACTION_UP:
                onClick(event);
                break;
        }
        return false;
    }

    public void startAnimation() {
        if (useAnimation) {
            animator.start(); // lagging
        }
        else {
            setColorFilter(Color.parseColor("#7AFF0F63"));
        }
        animated = true;
    }

    public void stopAnimation() {
        if (animated) {
            if (useAnimation) {
                animator.end(); // lagging
                animator = animator.clone(); // lagging
            } else {
                clearColorFilter();
            }
        }
        animated = false;
    }
    public boolean isAnimated(){
        return animated;
    }
    public String getName() {
        return name;
    }

    public String getInfo() {
        return infoText;
    }
    private void configureAnimator(){
        animator = ValueAnimator.ofArgb(Color.parseColor("#7D000000"),Color.parseColor("#7DFFFFFF"), Color.parseColor("#7D000000"));
        animator.setDuration(1000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(Animation.INFINITE);
        animator.addUpdateListener(animation -> {
            setColorFilter((Integer) animation.getAnimatedValue());
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                clearColorFilter();
            }
        });
    }
}
