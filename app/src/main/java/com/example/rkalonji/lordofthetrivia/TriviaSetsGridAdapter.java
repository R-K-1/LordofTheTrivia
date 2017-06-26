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
 * Created by Rkalonji on 05/21/2017.
 */

public class TriviaSetsGridAdapter extends SimpleCursorAdapter {

    private Utils utils;

    public TriviaSetsGridAdapter(Context context, int layout) {
        super(context, layout, null, new String[]{TriviasProvider.NAME}, new int[]{android.R.id.text1}, 0);
        utils = new Utils();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String content = cursor.getString(cursor.getColumnIndex(TriviasProvider.NAME));
        TextView titleText = (TextView) view.findViewById(android.R.id.text1);
        String triviaFirebaseId = cursor.getString(cursor.getColumnIndex(TriviasProvider.FIREBASE_ID));
        titleText.setText(content);

        ImageView imageView = (ImageView) view.findViewById(R.id.trivia_set_image);
        String imagePath = cursor.getString(cursor.getColumnIndex(TriviasProvider.IMAGE_PATH));
        File imageFile = utils.getFileFromInternalStorage(context, imagePath);
        Picasso.with(context).load(imageFile).into(imageView);
    }
}
