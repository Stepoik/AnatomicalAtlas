package ru.myitschool.anatomyatlas.ui.quiz.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.myitschool.anatomyatlas.data.repositories.BodyPartRepository;
import ru.myitschool.anatomyatlas.ui.quiz_start.viewModel.QuizStartViewModelFactory;

public class QuizViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    public QuizViewModelFactory(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new QuizViewModel(new BodyPartRepository(context));
    }
}
