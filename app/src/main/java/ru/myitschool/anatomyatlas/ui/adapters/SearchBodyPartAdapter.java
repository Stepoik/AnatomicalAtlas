package ru.myitschool.anatomyatlas.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.myitschool.anatomyatlas.R;
import ru.myitschool.anatomyatlas.data.models.BodyPart;

public class SearchBodyPartAdapter extends RecyclerView.Adapter<SearchBodyPartAdapter.SearchViewHolder> {
    private List<BodyPart> bodyPartList = new ArrayList<>();

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.body_part_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bodyPartName.setText(bodyPartList.get(position).getName());
        holder.bodyPartInfo.setText(bodyPartList.get(position).getInformation());
        holder.itemView.setOnClickListener(v -> {
            if (holder.bodyPartInfo.getVisibility() == View.VISIBLE) {
                holder.bodyPartInfo.setVisibility(View.GONE);
            }
            else{
                holder.bodyPartInfo.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bodyPartList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setBodyPartList(List<BodyPart> bodyParts){
        this.bodyPartList = new ArrayList<>(bodyParts);
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView bodyPartName;
        TextView bodyPartInfo;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            bodyPartName = itemView.findViewById(R.id.body_part_name);
            bodyPartInfo = itemView.findViewById(R.id.body_part_info);
        }
    }
}
