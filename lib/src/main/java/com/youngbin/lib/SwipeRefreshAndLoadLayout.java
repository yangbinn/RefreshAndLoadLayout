package com.youngbin.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 继承官方SwipeRefreshLayout的可预加载布局，目前只适配了RecyclerView
 */
public class SwipeRefreshAndLoadLayout extends android.support.v4.widget.SwipeRefreshLayout implements ILoad.OnScrollAndClickListener {

    private static final String TAG = "RefreshAndLoadLayout";

    //预加载开关
    private boolean mEnableLoad;
    private ILoad mLoad;
    private OnLoadListener mLoadListener;

    private LoadState mLoadState;//加载状态

    public SwipeRefreshAndLoadLayout(Context context) {
        super(context);
        init();
    }

    public SwipeRefreshAndLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        reset();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount <= 0) {
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //获取子控件接口
            if (childView instanceof ILoad) {
                mLoad = (ILoad) childView;
                mLoad.setOnScrollListener(this);
            }
        }
    }

    /**
     * 预加载
     */
    private void preload() {
        if (!checkNotNull()) {
            Log.i(TAG, "有空变量");
            return;
        }
        boolean canPreload = mLoad.canPreload();
        if (canPreload) {
            if (isRefreshing() || mLoadState != LoadState.NORMAL) {
                //正在刷新或者非初始状态，忽略
                Log.i(TAG, "正在刷新或者非初始状态，忽略");
                return;
            }
            setLoading(LoadState.LOADING);
        }
    }

    /**
     * 非空判断
     */
    private boolean checkNotNull() {
        return mLoadListener != null || mLoad == null || mLoadState == null;
    }

    public boolean isLoading() {
        return mLoadState == LoadState.LOADING;
    }

    public void setLoading(LoadState loadState) {
        if (mLoadState == loadState) {
            //状态一致，不重复操作
            Log.i(TAG, "状态一致， 不需要执行");
            return;
        }
        Log.i(TAG, "加载更多,loadState=" + loadState);
        mLoadState = loadState;
        mLoad.setState(loadState);
        if (loadState == LoadState.LOADING) {
            mLoadListener.onLoad();
        } else if (loadState == LoadState.NORMAL) {
            preload();
        }
    }

    public void setEnableToLoad(boolean enable) {
        if (!enable) {
            setLoading(LoadState.NORMAL);
        }
        mEnableLoad = enable;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        mLoad.setState(mLoadState = LoadState.NORMAL);
    }

    public void stopRefreshAndLoad() {
        reset();
        setRefreshing(false);
        setLoading(LoadState.NORMAL);
    }

    private void reset() {
        mEnableLoad = true;
        mLoadState = LoadState.NORMAL;
    }

    @Override
    public void onScroll(int dx, int dy) {
        if (!mEnableLoad) {
            return;
        }
        preload();
    }

    @Override
    public void onClick(View view) {
        setLoading(LoadState.LOADING);
    }

    public enum LoadState {
        NORMAL, LOADING, NO_MORE
    }

    public interface OnLoadListener {
        void onLoad();
    }

    public void setOnLoadListener(OnLoadListener listener) {
        mLoadListener = listener;
    }
}
