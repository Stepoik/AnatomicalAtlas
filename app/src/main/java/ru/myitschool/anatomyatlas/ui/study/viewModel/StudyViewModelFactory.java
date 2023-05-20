package ru.myitschool.anatomyatlas.ui.study.viewModel;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.myitschool.anatomyatlas.data.repositories.BodyPartRepository;

public class StudyViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    public StudyViewModelFactory(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new StudyViewModel(new BodyPartRepository(context));
    }
}
