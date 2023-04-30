package ru.myitschool.anatomyatlas.ui.result.view;

import android.annotation.SuppressLint;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import ru.myitschool.anatomyatlas.R;
import ru.myitschool.anatomyatlas.data.models.Money;
import ru.myitschool.anatomyatlas.databinding.FragmentResultBinding;
import ru.myitschool.anatomyatlas.ui.result.viewModel.ResultViewModel;
import ru.myitschool.anatomyatlas.ui.result.viewModel.ResultViewModelFactory;


public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;
    private NavController controller;
    private ResultViewModel viewModel;
    private final int MAX_PROGRESS_VALUE = 83;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentResultBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int score, count;
        if (getArguments() != null) {
            score = getArguments().getInt("score",0);
            count = getArguments().getInt("count", 0);
            binding.accuracy.setText((int) ((double) score / count * 100) +"%");
            Thread thread = new Thread(() -> {
                for (int i = 0; i < (int)((double) score /count*MAX_PROGRESS_VALUE); i++){
                   Handler handler = new Handler(Looper.getMainLooper());
                   int value = i;
                    handler.post(() -> binding.accuracyProgressbar.setProgress(Math.max(value, 5)));
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        OnBackPressedDispatcher dispatcher = requireActivity().getOnBackPressedDispatcher();
        controller = NavHostFragment.findNavController(this);
        dispatcher.addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                controller.navigate(R.id.studyFragment);
            }
        });
        controller.setOnBackPressedDispatcher(dispatcher);
        binding.again.setOnClickListener(v -> controller.navigate(R.id.action_resultFragment_to_quizFragment));
        binding.back.setOnClickListener(v -> controller.navigate(R.id.action_resultFragment_to_homeFragment));
        viewModel = new ViewModelProvider(this, new ResultViewModelFactory(getContext())).get(ResultViewModel.class);
        viewModel.getMoneyContainer().observe(getViewLifecycleOwner(), new Observer<Money>() {
            @Override
            public void onChanged(Money money) {
                int score = 0;
                if (getArguments() != null) {
                    score = getArguments().getInt("score",0);
                }
                viewModel.addMoney(score);
            }
        });
    }
}