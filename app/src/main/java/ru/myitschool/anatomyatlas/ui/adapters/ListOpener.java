package ru.myitschool.anatomyatlas.ui.adapters;

import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ListOpener {
    void openGroup(int index);
    void closeGroup(int index);
    MutableLiveData<Map<Integer, Set<Integer>>> getOpened();
    void openPart(int parentIndex, int partIndex);
    void closePart(int parentIndex, int partIndex);
}
