package ru.myitschool.anatomyatlas.ui.quiz.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

import ru.myitschool.anatomyatlas.R;
import ru.myitschool.anatomyatlas.databinding.FragmentQuizBinding;
import ru.myitschool.anatomyatlas.ui.UseSkeleton;
import ru.myitschool.anatomyatlas.ui.custom_views.BodyPartView;
import ru.myitschool.anatomyatlas.ui.dialogs.BackDialog;
import ru.myitschool.anatomyatlas.ui.quiz.viewModel.QuizViewModel;
import ru.myitschool.anatomyatlas.ui.quiz.viewModel.QuizViewModelFactory;

public class QuizFragment extends Fragment implements UseSkeleton {
    private FragmentQuizBinding binding;
    private QuizViewModel viewModel;
    private BodyPartView selectedView;
    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this, new QuizViewModelFactory(getContext(), 500))
                .get(QuizViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        controller = NavHostFragment.findNavController(this);
        OnBackPressedDispatcher dispatcher = requireActivity().getOnBackPressedDispatcher();
        dispatcher.addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                pauseTest(true, false);
                BackDialog dialog = new BackDialog(new BackDialog.DialogListener() {
                    @Override
                    public void onDialogPositiveClick() {
                        controller.navigate(R.id.action_quizFragment_to_homeFragment);
                    }

                    @Override
                    public void onDialogNegativeClick() {
                        pauseTest(false, false);
                    }

                    @Override
                    public void onDialogDismiss() {
                        pauseTest(false, false);
                    }
                });
                dialog.show(getParentFragmentManager(), "TAG");
            }
        });
        controller.setOnBackPressedDispatcher(dispatcher);
        viewModel.getProgress().observe(getViewLifecycleOwner(), progress -> binding.timer.setProgress(progress));
        viewModel.getBodyPartContainer().observe(getViewLifecycleOwner(), bodyParts -> {
            if (!viewModel.isShuffled()){
                viewModel.shuffleData();
                return;
            }
            if (bodyParts.size() > 0) {
                binding.name.setText(bodyParts.get(0).getName());
                viewModel.startTimer();
                return;
            }
            Bundle data = new Bundle();
            data.putInt("score", viewModel.getScore());
            data.putInt("count", viewModel.getCounter());
            controller.navigate(R.id.action_quizFragment_to_resultFragment, data);
        });
        binding.answer.setOnClickListener(v -> {
            if (selectedView != null) {
                select(selectedView.getName());
                return;
            }
            select(null);
        });
        binding.pauseLayout.setOnTouchListener((v, event) -> true);
        binding.pause.setOnClickListener(v -> {
            pauseTest(true, true);
        });
        binding.unpause.setOnClickListener(v -> {
            pauseTest(false, true);
        });
        viewModel.getAnswerCorrect().observe(getViewLifecycleOwner(), isCorrect -> {
            Animator animator;
            if (isCorrect){
                animator = ObjectAnimator.ofArgb(binding.isAnswerCorrect,
                        "backgroundColor",
                        Color.parseColor("#00FF5A5A"),
                        Color.parseColor("#9A46FF4F"),
                        Color.parseColor("#00FF5A5A"));
            }
            else{
                animator = ObjectAnimator.ofArgb(binding.isAnswerCorrect,
                        "backgroundColor",
                        Color.parseColor("#00FF5A5A"),
                        Color.parseColor("#97FF5A5A"),
                        Color.parseColor("#00FF5A5A"));
            }
            animator.setDuration(500);
            animator.start();
        });
    }
    void pauseTest(boolean pause, boolean usePausePanel){
        if (!pause && usePausePanel){
            binding.pauseLayout.setVisibility(View.GONE);
        }
        else if (usePausePanel){
            binding.pauseLayout.setVisibility(View.VISIBLE);
        }
        viewModel.setPause(pause);
    }
    private void makeViewsClickable(List<ViewGroup> views){
        for (ViewGroup viewGroup: views){
            for (int i = 0; i < viewGroup.getChildCount(); i++){
                BodyPartView v = (BodyPartView) viewGroup.getChildAt(i);
                v.setOnClickListener(v1 -> {
                    BodyPartView currentView = (BodyPartView) v1;
                    if (selectedView != null){
                        selectedView.stopAnimation();
                    }
                    if (selectedView == currentView){
                        selectedView = null;
                        binding.answer.setVisibility(View.GONE);
                        return;
                    }
                    binding.answer.setVisibility(View.VISIBLE);
                    selectedView = currentView;
                    selectedView.startAnimation();
                });
            }
        }
    }
    private void select(String name){
        if (selectedView != null){
            selectedView.stopAnimation();
            selectedView = null;
        }
        binding.answer.setVisibility(View.GONE);
        viewModel.select(name);
    }

    @Override
    public void onLoadChildFragment(List<ViewGroup> views) {
        makeViewsClickable(views);
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