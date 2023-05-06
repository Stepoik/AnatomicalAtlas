package ru.myitschool.anatomyatlas.ui.list.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.myitschool.anatomyatlas.data.models.BodyPart;
import ru.myitschool.anatomyatlas.data.repositories.BodyPartRepository;
import ru.myitschool.anatomyatlas.ui.adapters.ListOpener;

public class ListViewModel extends ViewModel implements ListOpener {
    private final BodyPartRepository repository;
    private final LiveData<List<BodyPart>> bodyPartContainer;
    private final MutableLiveData<Map<Integer,Set<Integer>>> openedIndexesContainer = new MutableLiveData<>(new HashMap<>());
    private final Map<Integer,Set<Integer>> openedIndexes = new HashMap<>();

    public ListViewModel(BodyPartRepository bodyPartRepository){
        repository = bodyPartRepository;
        bodyPartContainer = repository.getAllBodyParts();
    }

    public LiveData<List<BodyPart>> getBodyPartContainer() {
        return bodyPartContainer;
    }

    @Override
    public void openGroup(int index) {
        if (openedIndexesContainer.getValue() == null){
            return;
        }
        if (!openedIndexes.containsKey(index)) {
            openedIndexes.put(index, new HashSet<>());
        }
        openedIndexesContainer.getValue().put(index, openedIndexes.get(index));
        openedIndexesContainer.setValue(openedIndexesContainer.getValue());
    }

    @Override
    public void closeGroup(int index) {
        if (openedIndexesContainer.getValue() != null){
            openedIndexesContainer.getValue().remove(index);
            openedIndexesContainer.setValue(openedIndexesContainer.getValue());
        }
    }

    @Override
    public MutableLiveData<Map<Integer,Set<Integer>>> getOpened() {
        return openedIndexesContainer;
    }

    @Override
    public void openPart(int parentIndex, int partIndex) {
        if (openedIndexesContainer.getValue() == null){
            return;
        }
        Set<Integer> indexes = openedIndexes.get(parentIndex);
        if (indexes == null){
            return;
        }
        indexes.add(partIndex);
        openedIndexesContainer.setValue(new HashMap<>(openedIndexes));
    }

    @Override
    public void closePart(int parentIndex, int partIndex) {
        if (openedIndexesContainer.getValue() == null){
            return;
        }
        Set<Integer> indexes = openedIndexes.get(parentIndex);
        if (indexes == null){
            return;
        }
        indexes.remove(partIndex);
        openedIndexesContainer.setValue(new HashMap<>(openedIndexes));
    }
}
