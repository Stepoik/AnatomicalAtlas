package ru.myitschool.anatomyatlas.ui.quiz.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.myitschool.anatomyatlas.data.repositories.BodyPartRepository;
import ru.myitschool.anatomyatlas.ui.quiz_start.viewModel.QuizStartViewModelFactory;

public class QuizViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    private int progressMax;
    public QuizViewModelFactory(Context context, int progressMax){
        this.context = context;
        this.progressMax = progressMax;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new QuizViewModel(new BodyPartRepository(context), progressMax);
    }
}
