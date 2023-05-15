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
    private Sprite sprite;
    private OnClickListener clickListener;
    private String name;
    private String infoText;
    private ValueAnimator animator;

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
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StrangeView, 0, 0);
        name = a.getString(R.styleable.StrangeView_android_text);
        infoText = a.getString(R.styleable.StrangeView_android_tooltipText);
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Bitmap bitmap;
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
        return sprite.isPressed((int)x,(int)y, getWidth(), getHeight())
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
        if (animator.isPaused()){
            animator.resume();
        }
        else {
            animator.start();
        }
    }

    public void stopAnimation() {
        if (!animator.isPaused()) {
            animator.pause();
        }
    }
    public boolean isAnimated(){
        return !animator.isPaused();
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
            setColorFilter(new PorterDuffColorFilter((int)animation.getAnimatedValue(), PorterDuff.Mode.SRC_ATOP));
        });
        animator.addPauseListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationPause(Animator animation) {
                clearColorFilter();
            }
        });
    }
    public class Sprite {
        private Bitmap bitmap;
        public Sprite(Bitmap bitmap){
            this.bitmap = bitmap;
        }
        public boolean isPressed(int x, int y, int width, int height){
            x = (int) (x * (float) bitmap.getWidth() / width);
            y = (int) (y * (float) bitmap.getHeight() / height);
            if (x < bitmap.getWidth() && y < bitmap.getHeight() && x > 0 && y > 0) {
                return !(bitmap.getColor(x, y).alpha() < 0.5);
            }
            return false;
        }
    }

}
