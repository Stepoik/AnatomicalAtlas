package ru.myitschool.anatomyatlas.ui.study.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StudyViewModel extends ViewModel {
    private MutableLiveData<Integer> selectedIdContainer = new MutableLiveData<>();
    public void setSelected(int id){
        if (selectedIdContainer.getValue() != null && id == selectedIdContainer.getValue()){
            selectedIdContainer.setValue(0);
            return;
        }
        selectedIdContainer.setValue(id);
    }
    public MutableLiveData<Integer> getSelectedIdContainer(){
        return selectedIdContainer;
    }
}
