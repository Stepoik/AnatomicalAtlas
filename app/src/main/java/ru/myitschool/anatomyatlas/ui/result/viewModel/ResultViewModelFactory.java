package ru.myitschool.anatomyatlas.ui.result.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.myitschool.anatomyatlas.data.repositories.MoneyRepository;

public class ResultViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    public ResultViewModelFactory(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new ResultViewModel(new MoneyRepository(context));
    }
}
