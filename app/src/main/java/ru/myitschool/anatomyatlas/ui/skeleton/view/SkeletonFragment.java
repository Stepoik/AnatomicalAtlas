package ru.myitschool.anatomyatlas.ui.skeleton.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;

import ru.myitschool.anatomyatlas.databinding.FragmentSkeletonBinding;
import ru.myitschool.anatomyatlas.ui.UseSkeleton;
import ru.myitschool.anatomyatlas.ui.skeleton.viewModel.SkeletonViewModel;

public class SkeletonFragment extends Fragment {

    private FragmentSkeletonBinding binding;
    private SkeletonViewModel viewModel;
    private final int SKELETON_PROGRESS = 100;
    private final int ORGANS_PROGRESS = 0;
    private final int PROGRESS_VISIBLE = 70;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSkeletonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SkeletonViewModel.class);
        binding.getRoot().setZoomed(true);
        binding.getRoot().setZoomLayout(binding.content);
        if (getParentFragment() instanceof UseSkeleton){
            ((UseSkeleton) getParentFragment()).onLoadChildFragment(
                    Arrays.asList(binding.skeleton.getRoot(),
                            binding.organs.getRoot()));
        }
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewModel.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        viewModel.getProgressContainer().observe(getViewLifecycleOwner(), integer -> {
            if (getParentFragment() instanceof UseSkeleton){
                ((UseSkeleton) getParentFragment()).onChangeSeekBar(integer);
            }
            binding.seekBar.setProgress(integer);
            binding.skeleton.getRoot().setAlpha((float)((PROGRESS_VISIBLE-Math.abs(SKELETON_PROGRESS-integer)))/PROGRESS_VISIBLE);
            binding.organs.getRoot().setAlpha((float)((PROGRESS_VISIBLE-Math.abs(ORGANS_PROGRESS-integer)))/PROGRESS_VISIBLE);
        });
        viewModel.setProgress(100);
    }
}