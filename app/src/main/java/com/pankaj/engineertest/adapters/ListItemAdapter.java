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
import com.pankaj.engineertest.model.DataModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private Context context;
    private List<DataModel.HitList> hitList;
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

    }

    @Override
    public int getItemCount() {
        return hitList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_main;
        TextView tv_head, tv_createdOn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_main = itemView.findViewById(R.id.ll_main);
            tv_head = itemView.findViewById(R.id.tv_head);
            tv_createdOn = itemView.findViewById(R.id.tv_created);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
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

        return "Created at : " + changedDate;
    }
}
