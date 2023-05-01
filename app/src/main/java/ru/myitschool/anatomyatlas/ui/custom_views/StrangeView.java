package ru.myitschool.anatomyatlas.ui.custom_views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import ru.myitschool.anatomyatlas.R;

public class StrangeView extends ImageView {
    private Bitmap bitmap;
    private Sprite sprite;
    private OnClickListener clickListener;
    private String name;
    private String infoText;
    private ObjectAnimator animator;

    public StrangeView(Context context) {
        super(context);
    }

    public StrangeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StrangeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public StrangeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
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
        animator = ObjectAnimator.ofInt(this,
                "colorFilter",
                Color.parseColor("#7D000000"),
                Color.parseColor("#7DFFFFFF"),
                Color.parseColor("#7D000000"));
        animator.setDuration(1000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(Animation.INFINITE);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                clearColorFilter();
            }

        });
    }

    public void onClick(MotionEvent event) {
        if (startPress(event)) {
            if (clickListener != null) {
                if (getAlpha() > 0.5) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    clickListener.onClick(this);
                }
            }
        }
    }

    public boolean startPress(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return sprite.isPressed((int) (x * (float) bitmap.getWidth() / getWidth()), (int) (y * (float) bitmap.getHeight() / getHeight())) && getAlpha() > 0.5;
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
        animator.start();
    }

    public void stopAnimation() {
        animator.cancel();
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return infoText;
    }

}
