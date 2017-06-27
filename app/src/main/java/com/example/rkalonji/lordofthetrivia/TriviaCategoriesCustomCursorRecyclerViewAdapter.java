package com.example.rkalonji.lordofthetrivia;

import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new fragment and specify the fragment to show based on nav item clicked
                TriviaSetsGridFragment triviaSetsGridFragment = new TriviaSetsGridFragment();
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                // fragmentManager.beginTransaction().replace(R.firebaseId.flContent, fragment).commit();
                fragmentManager.beginTransaction().replace(R.id.flContent, triviaSetsGridFragment).commit();
            }
        });
    }

    private void switchToTriviaSetsFragments() {

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
