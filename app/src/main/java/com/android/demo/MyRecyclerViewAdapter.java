package com.android.demo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private List<HashMap> list = new ArrayList();
    private Context mContext;
    private LinearLayout mHeaderLayout;

    public MyRecyclerViewAdapter(List<HashMap> list, Context context) {
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).indexTv.setText("设置" + position);
        ((MyViewHolder)holder).contentTv.setText("测试" + position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView indexTv;
        public TextView contentTv;
        public CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            indexTv = itemView.findViewById(R.id.index);
            contentTv = itemView.findViewById(R.id.re_content);
        }
    }
}
