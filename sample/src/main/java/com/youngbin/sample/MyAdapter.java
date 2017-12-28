package com.youngbin.sample;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youngbin.lib.RefreshAndLoadAdapter;

import java.util.List;

public class MyAdapter extends RefreshAndLoadAdapter<String> {

    private static final String TAG = "MyAdapter";

    private LayoutType mLayoutType;

    public MyAdapter(List<String> list, LayoutType layoutType) {
        super(list);
        mLayoutType = layoutType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM_NORMAL: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
                viewHolder = new StringViewHolder(view);
                break;
            }
        }
        return viewHolder != null ? viewHolder : super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StringViewHolder) {
            String text = mList.get(position);
            StringViewHolder viewHolder = (StringViewHolder) holder;
            viewHolder.textView.setText(text);

            ViewGroup.LayoutParams params = viewHolder.itemView.getLayoutParams();
            switch (mLayoutType) {
                case LINNEAR:
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ScreenUtil.dip2px(40);
                    break;
                case GRID:
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ScreenUtil.dip2px(80);
                    break;
                case STAGGERED:
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = (position % 3 + 1) * ScreenUtil.dip2px(40);
                    break;
            }
        }
    }

    public class StringViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public StringViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_text_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onItemClick(v, getLayoutPosition(), mList.get(getLayoutPosition()));
                    }
                }
            });
        }
    }
}
