package ru.myitschool.anatomyatlas.ui.list.view;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import ru.myitschool.anatomyatlas.data.models.BodyPart;
import ru.myitschool.anatomyatlas.databinding.FragmentSearchBinding;
import ru.myitschool.anatomyatlas.ui.adapters.SearchBodyPartAdapter;
import ru.myitschool.anatomyatlas.ui.list.viewModel.SearchViewModel;
import ru.myitschool.anatomyatlas.ui.list.viewModel.SearchViewModelFactory;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private SearchBodyPartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SearchBodyPartAdapter();
        binding.foundList.setAdapter(adapter);
        viewModel = new ViewModelProvider(this, new SearchViewModelFactory(requireContext())).get(SearchViewModel.class);
        viewModel.getBodyPartList().observe(getViewLifecycleOwner(), bodyParts -> {
            adapter.setBodyPartList(bodyParts);
        });
        NavController controller = NavHostFragment.findNavController(this);
        binding.toolbar.setNavigationOnClickListener(v -> {
            controller.popBackStack();
        });
        if (binding.search.requestFocus()){
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.search, InputMethodManager.SHOW_IMPLICIT);
        }
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.search(binding.search.getText().toString());
            }
        });
    }
}