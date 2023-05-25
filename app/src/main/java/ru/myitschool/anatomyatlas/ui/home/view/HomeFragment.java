package ru.myitschool.anatomyatlas.ui.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import ru.myitschool.anatomyatlas.R;
import ru.myitschool.anatomyatlas.databinding.FragmentHomeBinding;
import ru.myitschool.anatomyatlas.ui.skeleton.NavigateFromChild;


public class HomeFragment extends Fragment implements NavigateFromChild {
    private FragmentHomeBinding binding;
    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.main_fragment);
        if (navHostFragment != null){
            controller = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.navigation, controller);
        }
    }

    @Override
    public void navigate(int id) {
        controller.navigate(id);
    }
}