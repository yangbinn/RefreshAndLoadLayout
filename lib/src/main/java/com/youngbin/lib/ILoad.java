package com.youngbin.lib;


import android.view.View;

public interface ILoad {

    /**
     * 能否预加载
     * @return true can
     */
    boolean canPreload();

    /**
     * 底部view设置状态
     * @param loadState {@link com.youngbin.lib.SwipeRefreshAndLoadLayout.LoadState}
     */
    void setState(SwipeRefreshAndLoadLayout.LoadState loadState);

    void setOnScrollListener(OnScrollAndClickListener listener);

    interface OnScrollAndClickListener{

        void onScroll(int dx, int dy);

        void onClick(View view);
    }

}



