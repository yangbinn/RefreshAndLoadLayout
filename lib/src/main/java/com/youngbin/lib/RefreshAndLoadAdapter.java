package com.youngbin.lib;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class RefreshAndLoadAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RefreshAndLoadAdapter";

    //默认类型
    protected static final int ITEM_NORMAL = 0;
    //底部view类型
    protected static final int ITEM_FOOTER = -1;

    //底部view holder
    private FooterViewHolder mFooterViewHolder;
    //适配数据
    protected List<T> mList;

    public RefreshAndLoadAdapter(List<T> list) {
        mList = list;
    }

    /**
     * 重新设置
     */
    public void setList(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    /**
     * 尾部添加
     */
    public void addList(List<T> list) {
        if (list != null && !list.isEmpty()) {
            if (mList == null) {
                mList = new ArrayList<>();
            }
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
            mFooterViewHolder = new FooterViewHolder(view);
            return mFooterViewHolder;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return ITEM_FOOTER;
        }
        return ITEM_NORMAL;
    }

    /**
     * 真实数量
     * @return count
     */
    public int getRealCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public int getItemCount() {
        int size = getRealCount();
        return size + 1;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        View view;
        ProgressBar progressBar;
        TextView textView;

        private FooterViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            progressBar = itemView.findViewById(R.id.item_footer_pb);
            textView = itemView.findViewById(R.id.item_footer_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mFooterClickListener != null) {
                        mFooterClickListener.onClick(view);
                    }
                }
            });
        }

        private void setState(SwipeRefreshAndLoadLayout.LoadState state) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            switch (state) {
                case NORMAL:
                    params.height = 0;
                    break;
                case LOADING:
                    params.height = 144;
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setText("加载更多");
                    break;
                case NO_MORE:
                    params.height = 144;
                    progressBar.setVisibility(View.GONE);
                    textView.setText("滑到底部");
                    break;
            }
            view.setLayoutParams(params);
        }
    }

    /**
     * 设置状态
     */
    public void setState(SwipeRefreshAndLoadLayout.LoadState state) {
        Log.i(TAG, "state=" + state);
        if (mFooterViewHolder != null) {
            mFooterViewHolder.setState(state);
        }
    }

    public interface OnFooterClickListener {
        void onClick(View view);
    }

    private OnFooterClickListener mFooterClickListener;

    public void setOnFooterClickListener(OnFooterClickListener listener) {
        mFooterClickListener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //底部view 适配 GridLayoutManager
        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (viewType == ITEM_FOOTER) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //底部view 适配 StaggeredGridLayoutManager
        int position = holder.getLayoutPosition();
        if (getItemViewType(position) == ITEM_FOOTER) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    public OnItemClickListener<T> mListener;

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        mListener = listener;
    }

    /**
     * item点击事件
     *
     * @param <T>
     */
    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T t);
    }

    public OnLongItemClickListener<T> mLongListener;

    public void setOnLongItemClickListener(OnLongItemClickListener<T> longListener) {
        mLongListener = longListener;
    }

    public interface OnLongItemClickListener<T> {
        boolean onLongItemClick(View view, int position, T t);
    }

}
