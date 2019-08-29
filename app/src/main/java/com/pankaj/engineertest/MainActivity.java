package com.pankaj.engineertest;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pankaj.engineertest.adapters.ListItemAdapter;
import com.pankaj.engineertest.model.DataModel;
import com.pankaj.engineertest.viewmodel.DataViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListItemAdapter listItemAdapter;
    private ProgressBar progressBar;
    private DataModel dataModel;
    private DataViewModel dataViewModel;
    private int api_in_progress = 0;
    private List<DataModel.HitList> hitLists;
    int pagenumber = 1;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        hitLists = new ArrayList<>();

        initViews();


    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        bindList(hitLists);
        dataViewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        dataViewModel.getApiData(MainActivity.this, pagenumber);


        dataViewModel.getLiveData().observe(this, new Observer<DataModel>() {
            @Override
            public void onChanged(DataModel dataModel) {
                if (dataModel != null && dataModel.getHits() != null) {
                    api_in_progress = 0;
                    recyclerView.setVisibility(View.VISIBLE);
                    hitLists = new ArrayList<>();
                    hitLists.addAll(dataModel.getHits());
                    progressBar.setVisibility(View.GONE);
                    bindList(hitLists);
                }
            }
        });
    }

    private void loadMore() {
//        progressBar.setVisibility(View.VISIBLE);
        api_in_progress = 1;
        pagenumber++;
        dataViewModel.getApiData(MainActivity.this, pagenumber);

        listItemAdapter.setLoadMoreListener(new ListItemAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hitLists.size() > 0) {
                            progressBar.setVisibility(View.GONE);
                            loadMore();
                        }
                    }
                });
            }
        });

    }

    private void bindList(List<DataModel.HitList> data) {
        actionBar.setTitle("Total displaying posts = " + data.size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listItemAdapter = new ListItemAdapter(this, data);
        recyclerView.setAdapter(listItemAdapter);

        listItemAdapter.setLoadMoreListener(new ListItemAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hitLists.size() > 0) {
                            progressBar.setVisibility(View.VISIBLE);
                            loadMore();
                        }
                    }
                });
            }
        });
    }
}