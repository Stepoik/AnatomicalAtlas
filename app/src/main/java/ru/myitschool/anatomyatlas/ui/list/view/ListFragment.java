package ru.myitschool.anatomyatlas.ui.list.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import ru.myitschool.anatomyatlas.data.models.BodyPart;
import ru.myitschool.anatomyatlas.databinding.FragmentListBinding;
import ru.myitschool.anatomyatlas.ui.adapters.BodyPartGroupAdapter;
import ru.myitschool.anatomyatlas.ui.list.viewModel.ListViewModel;
import ru.myitschool.anatomyatlas.ui.list.viewModel.ListViewModelFactory;


public class ListFragment extends Fragment {
    private FragmentListBinding binding;
    private ListViewModel viewModel;
    private BodyPartGroupAdapter adapter;

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
        viewModel = new ViewModelProvider(this, new ListViewModelFactory(getContext()))
                .get(ListViewModel.class);
        adapter = new BodyPartGroupAdapter(viewModel, getViewLifecycleOwner());
        viewModel.getBodyPartContainer().observe(getViewLifecycleOwner(), bodyParts -> adapter.setData(bodyParts));
        binding.informationList.setAdapter(adapter);
    }
}