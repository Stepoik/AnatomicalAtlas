package ru.myitschool.anatomyatlas.ui.teach.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomButton;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import ru.myitschool.anatomyatlas.R;
import ru.myitschool.anatomyatlas.custom_views.StrangeView;
import ru.myitschool.anatomyatlas.databinding.FragmentStudyBinding;

public class StudyFragment extends Fragment {
    private FragmentStudyBinding binding;
    private ScaleGesture gestureScale;
    private MyGestureDetectorListener gestureDetectorListener;
    private BottomSheetBehavior bottomSheetBehavior;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        gestureScale = new ScaleGesture(getContext());
        gestureDetectorListener = new MyGestureDetectorListener(getContext());
        binding = FragmentStudyBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(gestureScale);
        binding.bottomInfo.getRoot().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.getRoot().setFixed();
                return false;
            }
        });
        System.out.println(binding.bottomInfo.getRoot());
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomInfo.getRoot());
        bottomSheetBehavior.setHalfExpandedRatio(0.3f);
        for (int i = 0; i < binding.content.getRoot().getChildCount(); i++){
            ImageView imageView = (ImageView) binding.content.getRoot().getChildAt(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.bottomInfo.name.setText(((StrangeView)v).getText());
                    for (int i = 0; i < binding.content.getRoot().getChildCount(); i++) {
                        ImageView imageView = (ImageView) binding.content.getRoot().getChildAt(i);
                        imageView.setColorFilter(Color.parseColor("#0009FF00"));
                    }
                    ((ImageView)v).setColorFilter(Color.parseColor("#5C09FF00"));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
            });
        }
    }

    class ScaleGesture extends GestureDetector.SimpleOnGestureListener implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener{
        private ScaleGestureDetector scaleGestureDetector;
        private GestureDetector gestureDetector;
        private float SCALE_MAX = 3f;
        private float SCALE_MIN = 0.75f;
        private float MAX_X = 800f;
        private float MIN_X = -800f;
        private float MAX_Y = 1700f;
        private float MIN_Y = -1700f;
        private boolean scaling = false;
        public ScaleGesture(Context context){
            scaleGestureDetector = new ScaleGestureDetector(context, this);
            gestureDetector = new GestureDetector(context, this);
        }
        @Override
        public boolean onScale(@NonNull ScaleGestureDetector detector) {
            ViewGroup layout = binding.content.getRoot();
            System.out.println(layout.getScaleX());
            System.out.println(layout.getScaleY());
            float current_scale = Math.max(SCALE_MIN, Math.min(SCALE_MAX, layout.getScaleX()*detector.getScaleFactor()));
            layout.setScaleX(current_scale);
            layout.setScaleY(current_scale);
            return true;
        }

        @Override
        public boolean onScaleBegin(@NonNull ScaleGestureDetector detector) {
            scaling = true;
            return true;
        }

        @Override
        public void onScaleEnd(@NonNull ScaleGestureDetector detector) {
            scaling = false;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getPointerCount() < 2) {
                System.out.println(binding.bottomInfo);
                gestureDetector.onTouchEvent(event);
            }
            else{
                scaleGestureDetector.onTouchEvent(event);
            }
            return true;
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            binding.getRoot().setPivotX(0);
            binding.getRoot().setPivotY(0);
            ViewGroup layout = binding.content.getRoot();
            float x_new = Math.max(MIN_X, Math.min(MAX_X, layout.getX()-distanceX));
            float y_new = Math.max(MIN_Y, Math.min(MAX_Y, layout.getY()-distanceY));
            layout.setX(x_new);
            layout.setY(y_new);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
    class MyGestureDetectorListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
        private GestureDetector gestureDetector;
        public MyGestureDetectorListener(Context context){
            gestureDetector = new GestureDetector(context, this);
        }
        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            ViewGroup layout = binding.content.getRoot();
            layout.setX(layout.getX()-distanceX);
            layout.setY(layout.getY()-distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
    }
}