package ru.myitschool.anatomyatlas.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ZoomLayout extends ConstraintLayout {
    private boolean fixed;
    private float lastX;
    private float lastY;
    private boolean startScaling = false;
    private OnTouchListener touchListener;
    public ZoomLayout(@NonNull Context context) {
        super(context);
    }

    public ZoomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ZoomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        touchListener = l;
        super.setOnTouchListener(l);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (fixed && ev.getAction() != MotionEvent.ACTION_DOWN){
            return false;
        }
        switch (ev.getAction()){
            case (MotionEvent.ACTION_DOWN):
                fixed = false;
                lastX = ev.getX();
                lastY = ev.getY();
                if (touchListener != null){
                    touchListener.onTouch(this, ev);
                }
            case (MotionEvent.ACTION_MOVE):
                float difX = Math.abs(ev.getX()-lastX);
                float difY = Math.abs(ev.getY()-lastY);
                if (difX+difY > ViewConfiguration.getTouchSlop()*2){
                    return true;
                }
        }
        return false;
    }
    public void setFixed(){
        fixed = true;
    }
}
