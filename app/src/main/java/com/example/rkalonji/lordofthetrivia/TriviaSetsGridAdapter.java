package com.example.rkalonji.lordofthetrivia;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Rkalonji on 05/21/2017.
 */

public class TriviaSetsGridAdapter extends SimpleCursorAdapter {
    public TriviaSetsGridAdapter(Context context, int layout) {
        super(context, layout, null, new String[]{TriviasProvider.NAME}, new int[]{android.R.id.text1}, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String content = cursor.getString(cursor.getColumnIndex(TriviasProvider.NAME));
        TextView titleText = (TextView) view.findViewById(android.R.id.text1);
        String triviaFirebaseId = cursor.getString(cursor.getColumnIndex(TriviasProvider.TRIVIA_SET_FIREBASE_ID));
        titleText.setText(content);
    }
}
