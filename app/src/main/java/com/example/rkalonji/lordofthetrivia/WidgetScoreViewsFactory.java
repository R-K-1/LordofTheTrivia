package com.example.rkalonji.lordofthetrivia;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkalonji on 06/28/2017.
 */

public class WidgetScoreViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    List<Score> mScoresCollecticon = new ArrayList<Score>();

    Context mContext = null;

    public WidgetScoreViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mScoresCollecticon.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews mView = new RemoteViews(mContext.getPackageName(), R.layout.widget_listview_item);
        Score score = mScoresCollecticon.get(position);
        mView.setTextViewText(R.id.widget_score_username, score.username);
        mView.setTextViewText(R.id.widget_score_score, String.valueOf(score.score));

        final Intent fillInIntent = new Intent();
        fillInIntent.setAction(WidgetProvider.ACTION_VIEW_SCORE);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(WidgetProvider.EXTRA_SELECTED_SCORE,
                score);
        fillInIntent.putExtras(bundle);
        mView.setOnClickFillInIntent(R.id.widget_list_item_linear_layout, fillInIntent);
        return mView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    private void initData() {
        mScoresCollecticon.clear();

        Cursor c = mContext.getApplicationContext().getContentResolver().query(TriviasProvider.SCORES_BASE_URI,
                null, null, null, null);

        if (c != null && c.moveToFirst()) {
            do {
                mScoresCollecticon.add(new Score(
                        c.getString(c.getColumnIndex(TriviasProvider.USERNAME)),
                        c.getInt(c.getColumnIndex(TriviasProvider.SCORE)),
                        c.getInt(c.getColumnIndex(TriviasProvider.TRIVIA_SET_FIREBASE_ID))
                ));
            } while (c.moveToNext());
        }
    }

    @Override
    public void onDestroy() { }
}
