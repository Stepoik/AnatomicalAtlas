package ru.myitschool.anatomyatlas.ui.list.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import ru.myitschool.anatomyatlas.R;
import ru.myitschool.anatomyatlas.databinding.FragmentListBinding;
import ru.myitschool.anatomyatlas.ui.adapters.BodyPartGroupListAdapter;
import ru.myitschool.anatomyatlas.ui.list.viewModel.ListViewModel;
import ru.myitschool.anatomyatlas.ui.list.viewModel.ListViewModelFactory;


public class ListFragment extends Fragment {
    private FragmentListBinding binding;
    private ListViewModel viewModel;
    private BodyPartGroupListAdapter adapter;
    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this, new ListViewModelFactory(getContext()))
                .get(ListViewModel.class);
        adapter = new BodyPartGroupListAdapter(viewModel, getViewLifecycleOwner());
        viewModel.getBodyPartContainer().observe(getViewLifecycleOwner(), bodyParts -> adapter.setData(bodyParts));
        binding.informationList.setAdapter(adapter);
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.action_listFragment_to_searchFragment);
            }
        });
    }
}