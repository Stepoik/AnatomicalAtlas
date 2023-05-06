package ru.myitschool.anatomyatlas.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.myitschool.anatomyatlas.R;
import ru.myitschool.anatomyatlas.data.models.BodyPart;

public class BodyPartGroupAdapter extends RecyclerView.Adapter<BodyPartGroupAdapter.GroupBodyPartViewHolder> {
    List<String> groups = new ArrayList<>();
    private final Map<String, List<BodyPart>> elements = new HashMap<>();
    private final Map<Integer, GroupBodyPartViewHolder> viewHolderMap = new HashMap<>();
    private final ListOpener listOpener;
    private final LifecycleOwner lifecycleOwner;

    public BodyPartGroupAdapter(ListOpener listOpener, LifecycleOwner lifecycleOwner){
        this.listOpener = listOpener;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public GroupBodyPartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.body_part_list_item, parent, false);
        return new GroupBodyPartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupBodyPartViewHolder holder, int position) {
        viewHolderMap.put(position, holder);
        holder.groupName.setText(groups.get(position));
        BodyPartAdapter bodyPartAdapter = new BodyPartAdapter(listOpener, position, lifecycleOwner);
        List<BodyPart> bodyParts = elements.get(groups.get(position));
        if (bodyParts != null) {
            bodyPartAdapter.setList(new ArrayList<>(bodyParts));
        }
        holder.bodyPartsRecycler.setAdapter(bodyPartAdapter);
        holder.itemLayout.setOnClickListener(v -> {
            if (holder.bodyPartsRecycler.getAdapter() != null) {
                if (!holder.isOpened) {
                    listOpener.openGroup(position);
                } else {
                    listOpener.closeGroup(position);
                }
            }
        });
        if (viewHolderMap.size() == groups.size()){
            this.listOpener.getOpened().observe(lifecycleOwner, integers -> {
                for (int key: viewHolderMap.keySet()){
                    GroupBodyPartViewHolder viewHolder = viewHolderMap.get(key);
                    if (viewHolder == null || viewHolder.bodyPartsRecycler.getAdapter() == null){
                        continue;
                    }
                    if (integers.containsKey(key)){
                        viewHolder.isOpened = true;
                        if (bodyParts == null){
                            continue;
                        }
                        viewHolder.bodyPartsRecycler.setVisibility(View.VISIBLE);
                    }
                    else{
                        viewHolder.isOpened = false;
                        viewHolder.bodyPartsRecycler.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<BodyPart> bodyPartList){
        for (BodyPart bodyPart: bodyPartList){
            if (elements.containsKey(bodyPart.getGroup())){
                elements.get(bodyPart.getGroup()).add(bodyPart);
            }
            else{
                ArrayList<BodyPart> arrayList = new ArrayList<>(Arrays.asList(bodyPart));
                elements.put(bodyPart.getGroup(), arrayList);
            }
        }
        groups.clear();
        groups.addAll(elements.keySet());
        notifyDataSetChanged();
    }

    public class GroupBodyPartViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        View itemLayout;
        RecyclerView bodyPartsRecycler;
        boolean isOpened = false;
        public GroupBodyPartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item_layout);
            groupName = itemView.findViewById(R.id.group_name);
            bodyPartsRecycler = itemView.findViewById(R.id.body_parts);
        }
    }
}
