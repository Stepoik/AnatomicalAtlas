package ru.myitschool.anatomyatlas.ui.study.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import ru.myitschool.anatomyatlas.databinding.FragmentStudyBinding;
import ru.myitschool.anatomyatlas.ui.UseSkeleton;
import ru.myitschool.anatomyatlas.ui.custom_views.BodyPartView;
import ru.myitschool.anatomyatlas.ui.study.viewModel.StudyViewModel;
import ru.myitschool.anatomyatlas.ui.study.viewModel.StudyViewModelFactory;

public class StudyFragment extends Fragment implements UseSkeleton {
    private FragmentStudyBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;
    private StudyViewModel viewModel;
    private BodyPartView selectedView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, new StudyViewModelFactory(requireContext())).get(StudyViewModel.class);
        binding = FragmentStudyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    private void subscribe(List<ViewGroup> layouts){
        binding.bottomInfo.contentScrollView.setOnTouchListener((v, event) -> {
            binding.bottomInfo.contentScrollView.onTouchEvent(event);
            binding.bottomInfo.contentScrollView.requestDisallowInterceptTouchEvent(true);
            return true;
        });
        viewModel.getSelectedIdContainer().observe(getViewLifecycleOwner(), id -> {
            if (id == 0){
                if (selectedView == null){
                    return;
                }
                selectedView.stopAnimation();
                binding.bottomInfo.name.setText("");
                binding.bottomInfo.content.setText("");
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                selectedView = null;
                return;
            }
            View v = binding.getRoot().findViewById(id);
            binding.bottomInfo.name.setText(((BodyPartView)v).getName());
            if (selectedView != null){
                selectedView.stopAnimation();
            }
            selectedView = (BodyPartView)v;
            selectedView.startAnimation();
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                    bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });
        viewModel.getInformationContainer().observe(getViewLifecycleOwner(), s ->
                binding.bottomInfo.content.setText(s));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomInfo.getRoot());
        bottomSheetBehavior.setHalfExpandedRatio(400f/getResources().getDisplayMetrics().heightPixels);
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
                BodyPartView bodyPartView = (BodyPartView) views.getChildAt(i);
                bodyPartView.setOnClickListener(v -> viewModel.setSelected(v.getId(),
                        bodyPartView.getName()));
            }
        }
    }

    @Override
    public void onChangeSeekBar(int progress) {
        if (selectedView == null){
            return;
        }
        if (selectedView.getParent() != null &&
                ((View)selectedView.getParent()).getAlpha() < 0){
            selectedView.stopAnimation();
            return;
        }
        if (!selectedView.isAnimated()){
            selectedView.startAnimation();
        }
    }
}