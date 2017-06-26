package com.example.rkalonji.lordofthetrivia;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by rkalonji on 06/26/2017.
 */

public class ScoresGridAdapter extends SimpleCursorAdapter {
    private Utils utils;

    public ScoresGridAdapter(Context context, int layout) {
        super(context, layout, null, new String[]{TriviasProvider.NAME}, new int[]{android.R.id.text1}, 0);
        utils = new Utils();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int triviaId = cursor.getInt(cursor.getColumnIndex(TriviasProvider.TRIVIA_SET_FIREBASE_ID));
        TextView triviaIdView = (TextView) view.findViewById(android.R.id.text1);
        triviaIdView.setText(triviaId);

        String username = cursor.getString(cursor.getColumnIndex(TriviasProvider.USERNAME));
        TextView usernameView = (TextView) view.findViewById(android.R.id.text2);
        usernameView.setText(username);

        int score = cursor.getInt(cursor.getColumnIndex(TriviasProvider.SCORE));
        TextView scoreView = (TextView) view.findViewById(R.id.score);
        scoreView.setText(score);
    }
}
