package ru.myitschool.anatomyatlas.ui.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import ru.myitschool.anatomyatlas.ui.study.view.StudyFragment;

public class ZoomLayout extends ConstraintLayout {
    private boolean fixed;
    private float lastX;
    private float lastY;
    private ScaleGesture gestureScale;
    private OnTouchListener touchListener;
    private boolean zoomed = false;
    public ZoomLayout(@NonNull Context context) {
        super(context);
    }

    public ZoomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ZoomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init(){
        gestureScale = new ScaleGesture(getContext());
    }
    public void setZoomed(boolean action){
        if (action){
            setOnTouchListener(gestureScale);
            zoomed = true;
        }
        else{
            setOnTouchListener((v, event) -> false);
            zoomed = false;
        }
    }
    public void setZoomLayout(ViewGroup layout){
        gestureScale.setLayout(layout);
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
        if (zoomed) {
            switch (ev.getAction()) {
                case (MotionEvent.ACTION_DOWN):
                    fixed = false;
                    lastX = ev.getX();
                    lastY = ev.getY();
                    if (touchListener != null) {
                        touchListener.onTouch(this, ev);
                    }
                case (MotionEvent.ACTION_MOVE):
                    float difX = Math.abs(ev.getX() - lastX);
                    float difY = Math.abs(ev.getY() - lastY);
                    if (difX + difY > ViewConfiguration.getTouchSlop() * 2) {
                        return true;
                    }
            }
        }
        return false;
    }
    private ZoomLayout getThis(){
        return this;
    }
    public void setFixed(){
        fixed = true;
    }
    class ScaleGesture extends GestureDetector.SimpleOnGestureListener implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener{
        private ScaleGestureDetector scaleGestureDetector;
        private GestureDetector gestureDetector;
        private final float SCALE_MAX = 3f;
        private final float SCALE_MIN = 0.75f;
        private final float MAX_X = 800f;
        private final float MIN_X = -800f;
        private final float MAX_Y = 1700f;
        private final float MIN_Y = -1700f;
        private float currentMaxX = MAX_X;
        private float currentMinX = MIN_X;
        private float currentMaxY = MAX_Y;
        private float currentMinY = MIN_Y;
        private float currentScale = 1;
        private ViewGroup layout;
        public void setLayout(ViewGroup layout){
            this.layout = layout;
        }
        public ScaleGesture(Context context){
            layout = getThis();
            scaleGestureDetector = new ScaleGestureDetector(context, this);
            gestureDetector = new GestureDetector(context, this);
        }
        @Override
        public boolean onScale(@NonNull ScaleGestureDetector detector) {
            currentScale = Math.max(SCALE_MIN, Math.min(SCALE_MAX, layout.getScaleX()*detector.getScaleFactor()));
            layout.setScaleX(currentScale);
            layout.setScaleY(currentScale);
            currentMaxX = currentScale*MAX_X;
            currentMaxY = currentScale*MAX_Y;
            currentMinX = currentScale*MIN_X;
            currentMinY = currentScale*MIN_Y;
            return true;
        }

        @Override
        public boolean onScaleBegin(@NonNull ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(@NonNull ScaleGestureDetector detector) {

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getPointerCount() < 2) {
                gestureDetector.onTouchEvent(event);
            }
            else{
                scaleGestureDetector.onTouchEvent(event);
            }
            return true;
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            float xNew = Math.max(currentMinX, Math.min(currentMaxX, (layout.getX()-distanceX/currentScale)));
            float yNew = Math.max(currentMinY, Math.min(currentMaxY, (layout.getY()-distanceY/currentScale)));
            float xChange = layout.getX()-xNew;
            float yChange = layout.getY()-yNew;
//            float currentPivotX = layout.getPivotX();
            layout.setX(xNew);
            layout.setY(yNew);
            System.out.println(layout.getX());
            layout.setPivotX(layout.getPivotX()+xChange);
            layout.setPivotY(layout.getPivotY()+yChange);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
}
