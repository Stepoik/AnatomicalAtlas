package ru.myitschool.anatomyatlas.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.myitschool.anatomyatlas.R;
import ru.myitschool.anatomyatlas.data.models.BodyPart;

public class BodyPartListAdapter extends RecyclerView.Adapter<BodyPartListAdapter.BodyPartViewHolder> {
    private List<BodyPart> bodyPartList = new ArrayList<>();
    private final Map<Integer, BodyPartViewHolder> viewHolderMap = new HashMap<>();
    private final ListOpener listOpener;
    private final int parentIndex;
    private final LifecycleOwner lifecycleOwner;

    public BodyPartListAdapter(ListOpener listOpener, int parentIndex, LifecycleOwner lifecycleOwner) {
        this.listOpener = listOpener;
        this.parentIndex = parentIndex;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public BodyPartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.body_part_item, parent, false);
        return new BodyPartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BodyPartViewHolder holder, int position) {
        viewHolderMap.put(position, holder);
        BodyPart bodyPart = bodyPartList.get(position);
        holder.bodyPartName.setText(bodyPart.getName());
        holder.bodyPartInformation.setText(bodyPart.getInformation());
        holder.rootView.setOnClickListener(v -> {
            if (!holder.isOpen) {
                listOpener.openPart(parentIndex, position);
            }
            else{
                listOpener.closePart(parentIndex, position);
                close(position);
            }
        });
        if (viewHolderMap.size() == bodyPartList.size()){
            listOpener.getOpened().observe(lifecycleOwner, integerSetMap -> {
                Set<Integer> opened = integerSetMap.get(parentIndex);
                if (opened == null){
                    return;
                }
                for (int i: opened){
                    open(i);
                }
            });
        }
    }
    public void open(int index){
        BodyPartViewHolder holder = viewHolderMap.get(index);
        if (holder == null){
            return;
        }
        holder.isOpen = true;
        holder.bodyPartInformation.setVisibility(View.VISIBLE);
    }
    public void close(int index){
        BodyPartViewHolder holder = viewHolderMap.get(index);
        if (holder == null){
            return;
        }
        holder.isOpen = false;
        holder.bodyPartInformation.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return bodyPartList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<BodyPart> bodyParts) {
        if (this.bodyPartList.size() == 0){
            this.bodyPartList = new ArrayList<>(bodyParts);
            notifyDataSetChanged();
        }
    }

    public class BodyPartViewHolder extends RecyclerView.ViewHolder {
        TextView bodyPartName;
        TextView bodyPartInformation;
        View rootView;
        boolean isOpen = false;
        public BodyPartViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            bodyPartName = itemView.findViewById(R.id.body_part_name);
            bodyPartInformation = itemView.findViewById(R.id.body_part_info);
        }
    }
}
