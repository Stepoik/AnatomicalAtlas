package ru.myitschool.anatomyatlas.ui.quiz.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ru.myitschool.anatomyatlas.R;
import ru.myitschool.anatomyatlas.databinding.FragmentQuizBinding;
import ru.myitschool.anatomyatlas.ui.UseSkeleton;
import ru.myitschool.anatomyatlas.ui.custom_views.StrangeView;
import ru.myitschool.anatomyatlas.ui.dialogs.BackDialog;
import ru.myitschool.anatomyatlas.ui.quiz.viewModel.QuizViewModel;
import ru.myitschool.anatomyatlas.ui.quiz.viewModel.QuizViewModelFactory;

public class QuizFragment extends Fragment implements UseSkeleton {
    private Timer timer = new Timer();
    private FragmentQuizBinding binding;
    private QuizViewModel viewModel;
    private boolean isPaused = false;
    private StrangeView selectedView;
    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this, new QuizViewModelFactory(getContext()))
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
        viewModel.getBodyPartContainer().observe(getViewLifecycleOwner(), bodyParts -> {
            if (!viewModel.isShuffled()){
                viewModel.shuffleData();
                return;
            }
            if (bodyParts.size() > 0) {
                binding.name.setText(bodyParts.get(0).getName());
                startTimer();
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
    }
    void pauseTest(boolean pause, boolean usePausePanel){
        if (!pause && usePausePanel){
            binding.pauseLayout.setVisibility(View.GONE);
        }
        else if (usePausePanel){
            binding.pauseLayout.setVisibility(View.VISIBLE);
        }
        isPaused = pause;
    }
    private void startTimer(){
        Handler handler = new Handler(Looper.getMainLooper());
        binding.timer.setProgress(500);
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isPaused){
                    return;
                }
                if (binding.timer.getProgress() == 0){
                    timer.cancel();
                    handler.post(() -> select(null));
                }
                binding.timer.setProgress(binding.timer.getProgress()-1, true);
            }
        }, 0, 30);
    }
    private void makeViewsClickable(List<ViewGroup> views){
        for (ViewGroup viewGroup: views){
            for (int i = 0; i < viewGroup.getChildCount(); i++){
                StrangeView v = (StrangeView) viewGroup.getChildAt(i);
                v.setOnClickListener(v1 -> {
                    StrangeView currentView = (StrangeView) v1;
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
        if (viewModel.select(name)){
            return;
        }
    }

    @Override
    public void onLoadChildFragment(List<ViewGroup> views) {
        makeViewsClickable(views);
    }
}