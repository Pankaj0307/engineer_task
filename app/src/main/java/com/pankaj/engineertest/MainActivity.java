package com.pankaj.engineertest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.pankaj.engineertest.adapters.ListItemAdapter;
import com.pankaj.engineertest.interfaces.OnItemtClickedInterface;
import com.pankaj.engineertest.model.DataModel;
import com.pankaj.engineertest.viewmodel.DataViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemtClickedInterface {

    private RecyclerView recyclerView;
    private ListItemAdapter listItemAdapter;
    private ProgressBar progressBar;
    private DataModel dataModel;
    private DataViewModel dataViewModel;
    private int api_in_progress = 0;
    private List<DataModel.HitList> hitLists;
    private SwipeRefreshLayout swipeRefreshLayout;
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
        swipeRefreshLayout = findViewById(R.id.swipe);
        bindList(hitLists);
        dataViewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        dataViewModel.getApiData(MainActivity.this, pagenumber);


        dataViewModel.getLiveData().observe(this, new Observer<DataModel>() {
            @Override
            public void onChanged(DataModel dataModel) {
                swipeRefreshLayout.setRefreshing(false);
                if (dataModel != null && dataModel.getHits() != null) {
                    api_in_progress = 0;
                    swipeRefreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.VISIBLE);
                    hitLists = new ArrayList<>();
                    hitLists.addAll(dataModel.getHits());
                    progressBar.setVisibility(View.GONE);
                    bindList(hitLists);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (api_in_progress == 0) {
//                    checkCount(false);
                    swipeRefreshLayout.setRefreshing(false);
//                    recyclerView.setVisibility(View.GONE);
                    dataViewModel.clearModelData();
                    api_in_progress = 1;
                    dataViewModel.getApiData(MainActivity.this, pagenumber);
                }
            }
        });


    }

    private void loadMore() {
//        progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
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
                            swipeRefreshLayout.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                            loadMore();
                        }
                    }
                });
            }
        });

    }

    private void bindList(List<DataModel.HitList> data) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listItemAdapter = new ListItemAdapter(this, data);
        listItemAdapter.onItemtClickedInterface = this;
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

    @Override
    public void onItemClicked(int position) {
        if (hitLists.size() > position) {
//            hitLists.get(position).setDisabled((!hitsLists.get(position).isDisabled()));
            listItemAdapter.notifyDataSetChanged();
        }
    }

    public void checkCount(boolean doCount) {
        int count = 0;
        if (doCount) {
            for (int i = 0; i < hitLists.size(); i++) {
                if (hitLists.get(i).isCount()) {
                    count++;
                }
            }
            actionBar.setTitle("Selected items = " + count);
        }
    }
}