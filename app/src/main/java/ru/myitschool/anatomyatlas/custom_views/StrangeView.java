package ru.myitschool.anatomyatlas.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import ru.myitschool.anatomyatlas.R;

public class StrangeView extends ImageView {
    private Bitmap bitmap;
    private Sprite sprite;
    private OnClickListener clickListener;
    private String text;

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
        text = a.getString(R.styleable.StrangeView_android_text);
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
    }

    public void onClick(MotionEvent event) {
        if (startPress(event)) {
            if (clickListener != null) {
                playSoundEffect(SoundEffectConstants.CLICK);
                clickListener.onClick(this);
            }
        }
    }

    public boolean startPress(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return sprite.isPressed((int) (x*(float)bitmap.getWidth()/getWidth()), (int) (y*(float)bitmap.getHeight()/getHeight()));
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
    public String getText(){
        return text;
    }
}
