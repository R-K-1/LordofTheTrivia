package com.example.rkalonji.lordofthetrivia;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rkalonji on 06/26/2017.
 */

public class TriviaCategoriesCustomCursorRecyclerViewAdapter extends TriviaCategoriesCursorRecyclerViewAdapter {
    public TriviaCategoriesCustomCursorRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
/*        View v = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new CustomViewHolder(v);*/
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.trivia_categories_grid_fragment_item, parent, false);
        TriviaCategoryViewHolder triviaCategoryViewHolder = new TriviaCategoryViewHolder(itemView, parent.getContext());
        return triviaCategoryViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        TriviaCategoryViewHolder holder = (TriviaCategoryViewHolder) viewHolder;
        cursor.moveToPosition(cursor.getPosition());
        holder.setData(cursor);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
