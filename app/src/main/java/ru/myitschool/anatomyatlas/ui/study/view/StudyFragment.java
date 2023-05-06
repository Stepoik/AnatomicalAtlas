package ru.myitschool.anatomyatlas.ui.study.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import ru.myitschool.anatomyatlas.ui.UseSkeleton;
import ru.myitschool.anatomyatlas.ui.custom_views.StrangeView;
import ru.myitschool.anatomyatlas.databinding.FragmentStudyBinding;
import ru.myitschool.anatomyatlas.ui.custom_views.ZoomLayout;
import ru.myitschool.anatomyatlas.ui.study.viewModel.StudyViewModel;

public class StudyFragment extends Fragment implements UseSkeleton {
    private FragmentStudyBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;
    private StudyViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(StudyViewModel.class);
        binding = FragmentStudyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    private void subscribe(List<ViewGroup> layouts){
        binding.bottomInfo.contentScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.bottomInfo.contentScrollView.onTouchEvent(event);
                binding.bottomInfo.contentScrollView.requestDisallowInterceptTouchEvent(true);
                return true;
            }
        });
        viewModel.getSelectedIdContainer().observe(getViewLifecycleOwner(), id -> {
            View v = binding.getRoot().findViewById(id);
            binding.bottomInfo.name.setText(((StrangeView)v).getName());
            binding.bottomInfo.content.setText(((StrangeView)v).getInfo());
            for (ViewGroup layout: layouts) {
                for (int i = 0; i < layout.getChildCount(); i++) {
                    StrangeView imageView = (StrangeView) layout.getChildAt(i);
                    imageView.stopAnimation();
                }
            }
            ((StrangeView)v).startAnimation();
            System.out.println(bottomSheetBehavior.getState());
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                    bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomInfo.getRoot());
        bottomSheetBehavior.setHalfExpandedRatio(250f/getResources().getDisplayMetrics().heightPixels);
        bottomSheetBehavior.setMaxHeight((int)(getResources().getDisplayMetrics().heightPixels*0.6));
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.getRoot().setFixed();
            }
        });
    }

    @Override
    public void onLoadChildFragment(List<ViewGroup> listViews) {
        subscribe(listViews);
        for (ViewGroup views: listViews) {
            for (int i = 0; i < views.getChildCount(); i++) {
                ImageView imageView = (ImageView) views.getChildAt(i);
                imageView.setOnClickListener(v -> viewModel.setSelected(v.getId()));
            }
        }
    }
}