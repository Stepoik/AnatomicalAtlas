package ru.myitschool.anatomyatlas.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.myitschool.anatomyatlas.R;
import ru.myitschool.anatomyatlas.data.models.BodyPart;

public class BodyPartGroupListAdapter extends RecyclerView.Adapter<BodyPartGroupListAdapter.GroupBodyPartViewHolder> {
    List<String> groups = new ArrayList<>();
    private final Map<String, List<BodyPart>> elements = new HashMap<>();
    private final Map<Integer, GroupBodyPartViewHolder> viewHolderMap = new HashMap<>();
    private final ListOpener listOpener;
    private final LifecycleOwner lifecycleOwner;

    public BodyPartGroupListAdapter(ListOpener listOpener, LifecycleOwner lifecycleOwner){
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
        BodyPartListAdapter bodyPartAdapter = new BodyPartListAdapter(listOpener, position, lifecycleOwner);
        List<BodyPart> bodyParts = elements.get(groups.get(position));
        if (bodyParts != null) {
            bodyPartAdapter.setList(new ArrayList<>(bodyParts));
        }
        holder.bodyPartsRecycler.setAdapter(bodyPartAdapter);
        holder.itemLayout.setOnClickListener(v -> {
            if (holder.bodyPartsRecycler.getAdapter() != null) {
                int id;
                if (!holder.isOpened) {
                    listOpener.openGroup(position);
                    id = R.anim.open_arrow_anim;
                } else {
                    listOpener.closeGroup(position);
                    id = R.anim.close_arrow_anim;
                }
                Animation rotate = AnimationUtils.loadAnimation(holder.openArrow.getContext(), id);
                holder.openArrow.startAnimation(rotate);
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
        ImageView openArrow;
        boolean isOpened = false;
        public GroupBodyPartViewHolder(@NonNull View itemView) {
            super(itemView);
            openArrow = itemView.findViewById(R.id.open_arrow);
            itemLayout = itemView.findViewById(R.id.item_layout);
            groupName = itemView.findViewById(R.id.group_name);
            bodyPartsRecycler = itemView.findViewById(R.id.body_parts);
        }
    }
}
