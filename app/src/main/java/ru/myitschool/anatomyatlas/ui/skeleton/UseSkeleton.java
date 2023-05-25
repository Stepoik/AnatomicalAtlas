package ru.myitschool.anatomyatlas.ui.skeleton;

import android.view.ViewGroup;

import java.util.List;

public interface UseSkeleton {
    void onLoadChildFragment(List<ViewGroup> views);
    void onChangeSeekBar(int progress);
}
