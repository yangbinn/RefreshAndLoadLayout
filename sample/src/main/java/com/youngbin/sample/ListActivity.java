package com.youngbin.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.youngbin.lib.RefreshAndLoadAdapter;
import com.youngbin.lib.SwipeRefreshAndLoadLayout;
import com.youngbin.lib.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements RefreshAndLoadAdapter.OnItemClickListener<String> {

    private static final String TAG = "ListActivity";

    private SwipeRefreshAndLoadLayout mRefreshLayout;
    private XRecyclerView mRecyclerView;

    private MyAdapter mAdapter;
    private LayoutType mLayoutType;

    public static void startActivity(Context context, LayoutType type) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mRefreshLayout = findViewById(R.id.list_refresh_layout);
        mRecyclerView = findViewById(R.id.list_recycler);

        mRefreshLayout.setOnRefreshListener(mRefreshListener);
        mRefreshLayout.setOnLoadListener(mLoadListener);
        mRefreshLayout.setColorSchemeResources(R.color.colorBlue);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mLayoutType = (LayoutType) intent.getSerializableExtra("type");
        if (mLayoutType == null) {
            mLayoutType = LayoutType.LINNEAR;
        }

        RecyclerView.LayoutManager manager = null;

        switch (mLayoutType) {
            case LINNEAR:
                manager = new LinearLayoutManager(this);
                break;
            case GRID:
                manager = new GridLayoutManager(this, 2);
                break;
            case STAGGERED:
                manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
        }
        mRecyclerView.setLayoutManager(manager);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("第" + (i + 1) + "个");
        }
        setAdapter(list);
    }

    private SwipeRefreshAndLoadLayout.OnRefreshListener mRefreshListener = new SwipeRefreshAndLoadLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < 30; i++) {
                        list.add("第" + (i + 1) + "个");
                    }
                    setAdapter(list);
                }
            }, 2000);

        }
    };

    private SwipeRefreshAndLoadLayout.OnLoadListener mLoadListener = new SwipeRefreshAndLoadLayout.OnLoadListener() {
        @Override
        public void onLoad() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<String> list = new ArrayList<>();
                    if (mAdapter.getItemCount() <= 33) {
                        list.add("新增数据");
                    }
                    addAdapter(list);
                }
            }, 2000);

        }
    };

    public void setAdapter(List<String> list) {
        mRefreshLayout.setRefreshing(false);
        if (mAdapter == null) {
            mAdapter = new MyAdapter(list, mLayoutType);
            mAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setList(list);
        }
    }

    public void addAdapter(List<String> list) {
        if (list == null || list.isEmpty()) {
            Log.i(TAG, "没有更多");
            mRefreshLayout.setLoading(SwipeRefreshAndLoadLayout.LoadState.NO_MORE);
        } else {
            mRefreshLayout.setLoading(SwipeRefreshAndLoadLayout.LoadState.NORMAL);
        }
        if (mAdapter == null) {
            setAdapter(list);
        } else {
            mAdapter.addList(list);
        }
    }

    @Override
    public void onItemClick(View view, int position, String string) {
        Toast.makeText(this, "下标=" + position, Toast.LENGTH_SHORT).show();
    }
}
