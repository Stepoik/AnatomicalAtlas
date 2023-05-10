package ru.myitschool.anatomyatlas.ui.list.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.myitschool.anatomyatlas.data.repositories.BodyPartRepository;

public class SearchViewModelFactory implements ViewModelProvider.Factory {
    Context context;
    public SearchViewModelFactory(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new SearchViewModel(new BodyPartRepository(context));
    }
}
