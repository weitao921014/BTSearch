package com.wei.btsearch.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;
import com.wei.btsearch.configurations.AppConfiguration;

/**
 * Created by wei on 17-4-2.
 */
public class SwipeLoadLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    public interface OnLoadListener {
        public void onLoad();
    }

    Context context;
    private int mTouchSlop;
    private ListView mListView;
    private OnLoadListener mOnLoadListener;

    private int mYDown;
    private int mLastY;
    private boolean isLoading = false;

    public SwipeLoadLayout(Context context) {
        this(context, null);
    }

    public SwipeLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mListView == null) {
            getListView();
        }
    }

    private void getListView() {
        int childs = getChildCount();
        if (childs > 0) {
            View childView = getChildAt(0);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                mListView.setOnScrollListener(this);
                if (AppConfiguration.DEBUG) {
                    System.out.println("find listview");
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }

        if (isLoading) {
            return true;
        } else
            return super.dispatchTouchEvent(event);
    }


    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }

    private boolean isBottom() {

        if (mListView != null && mListView.getAdapter() != null) {
            return mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
        }
        return false;
    }


    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    private void loadData() {
        if (mOnLoadListener != null) {

            if (AppConfiguration.DEBUG) {
                System.out.println("loadData");
            }

            setLoading(true);
            mOnLoadListener.onLoad();
        }
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }


    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    public void requestLoadData() {
        loadData();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (canLoad()) {
            loadData();
        }
    }

//    /**
//     * 设置刷新
//     */
//    public static void setRefreshing(SwipeRefreshLayout refreshLayout, boolean refreshing, boolean notify) {
//        Class<? extends SwipeRefreshLayout> refreshLayoutClass = refreshLayout
//                .getClass();
//        if (refreshLayoutClass != null) {
//
//            try {
//                Method setRefreshing = refreshLayoutClass.getDeclaredMethod(
//                        "setRefreshing", boolean.class, boolean.class);
//                setRefreshing.setAccessible(true);
//                setRefreshing.invoke(refreshLayout, refreshing, notify);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//

}
