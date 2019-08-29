package com.pankaj.engineertest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pankaj.engineertest.MainActivity;
import com.pankaj.engineertest.R;
import com.pankaj.engineertest.interfaces.OnItemtClickedInterface;
import com.pankaj.engineertest.model.DataModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private Context context;
    private List<DataModel.HitList> hitList;
    public OnItemtClickedInterface onItemtClickedInterface;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false;
    private boolean enableDisableSwitch;
    MainActivity activity;


    public ListItemAdapter(Context context, List<DataModel.HitList> hitList) {
        this.context = context;
        this.hitList = hitList;
        activity = (MainActivity) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (position >= getItemCount() - 1 && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        final DataModel.HitList list = hitList.get(position);
        holder.tv_head.setText(list.getTitle());
        holder.tv_createdOn.setText(changeDateFormat(list.getCreated_at()));

        holder.switchItem.setChecked(false);

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemtClickedInterface.onItemClicked(position);
            }
        });

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToggle(holder, position);
            }
        });

        /*if (holder.switchItem.isActivated()) {
            switchToggle(holder, position);
        }*/

    }

    @Override
    public int getItemCount() {
        return hitList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_main;
        TextView tv_head, tv_createdOn;
        Switch switchItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_main = itemView.findViewById(R.id.ll_main);
            tv_head = itemView.findViewById(R.id.tv_head);
            tv_createdOn = itemView.findViewById(R.id.tv_created);
            switchItem = itemView.findViewById(R.id.switchItem);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    private void switchToggle(ViewHolder holder, int position) {
        if (enableDisableSwitch) {
            holder.switchItem.setChecked(true);
            hitList.get(position).setCount(true);
            activity.checkCount(true);
            enableDisableSwitch = false;
        } else {
            holder.switchItem.setChecked(false);
            hitList.get(position).setCount(false);
            activity.checkCount(true);
            enableDisableSwitch = true;
        }
    }

    private String changeDateFormat(String date) {
        String changedDate = "";
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

            Date date1 = input.parse(date);
            changedDate = output.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return changedDate;
    }


}
