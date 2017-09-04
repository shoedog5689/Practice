package com.android.demo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hewei on 2017/8/17.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private final String TAG = MyRecyclerViewAdapter.class.getSimpleName();

    private List list = new ArrayList();
    private Context mContext;
    private LinearLayout mHeaderLayout;
    private LinearLayout mLoadingLayout;

    public enum ITEM_TYPE {
        NORMAL,
        LOADING
    }

    public MyRecyclerViewAdapter(List list, Context context) {
        Log.e(TAG, "list.size():" + list.size());
        this.list = list;
        this.mContext = context;
    }

    public void addHeader(View headView) {
        Log.e(TAG, "addHeader()");
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(headView.getContext());
        }
        mHeaderLayout.addView(headView);
        notifyItemInserted(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.NORMAL.ordinal()) {
            // create a new view
            View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        } else if (viewType == ITEM_TYPE.LOADING.ordinal()) {
            // set the view's size, margins, paddings and layout parameters
            LoadingViewHolder vh = new LoadingViewHolder(mLoadingLayout);
            return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).indexTv.setText("toolbar_settings" + position);
            ((MyViewHolder) holder).contentTv.setText("测试" + position);
        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).loadingTv.setText("看好了！！哒哒哒哒哒哒...");
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType, position:" + position + ", getItemCount:" + getItemCount());
        if (mLoadingLayout != null) {  //存在Loading View Item
            if (position == getItemCount() - 1) {
                return ITEM_TYPE.LOADING.ordinal();
            } else {
                return ITEM_TYPE.NORMAL.ordinal();
            }
        }
        return ITEM_TYPE.NORMAL.ordinal();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    @Override
    public void onClick(View view) {

    }

    public void addLoadingItem(View v) {
        Log.d(TAG, "addLoadingItem()");
        if (mLoadingLayout == null) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            mLoadingLayout = new LinearLayout(mContext);
            mLoadingLayout.setLayoutParams(lp);

            mLoadingLayout.addView(v, 0);  //在mLoadingLayout头部加入v
        }
        list.add(0);
        int insertPosition = getItemCount() - 1;  //pos从0开始，这里getItemCount()增加了1，所以要减去1
        Log.e(TAG, "count:" + getItemCount());
        if (insertPosition > 0) {
            notifyItemInserted(insertPosition);
        }
    }

    public void removeLoadingItem() {
        Log.d(TAG, "removeLoadingItem()");

        if (mLoadingLayout != null) {  //Loading Item存在
            int loadingPosition = getItemCount() - 1;
            if (loadingPosition > 0) {
                notifyItemRemoved(loadingPosition);
                mLoadingLayout = null;
            }
        }
    }

    public void updateData() {
        List tmpList = new ArrayList();
        for (int i=0; i<10; i++) {
            tmpList.add(i);
        }
        list.addAll(tmpList);

        notifyDataSetChanged();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView indexTv;
        public TextView contentTv;
        public CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            indexTv = (TextView) itemView.findViewById(R.id.index);
            contentTv = (TextView) itemView.findViewById(R.id.re_content);
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public TextView loadingTv;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            loadingTv = (TextView) itemView.findViewById(R.id.loading_tv);
        }
    }
}
