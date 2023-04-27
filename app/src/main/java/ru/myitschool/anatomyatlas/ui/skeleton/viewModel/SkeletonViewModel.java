package ru.myitschool.anatomyatlas.ui.skeleton.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SkeletonViewModel extends ViewModel {
    private MutableLiveData<Integer> progress = new MutableLiveData<>();
    public MutableLiveData<Integer> getProgressContainer(){
        return progress;
    }

    public void setProgress(int progressValue) {
        progress.setValue(progressValue);
    }
}
