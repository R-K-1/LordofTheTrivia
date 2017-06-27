package com.example.rkalonji.lordofthetrivia;

import android.content.Context;
import android.database.Cursor;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView

/**
 * Created by rkalonji on 06/26/2017.
 */

;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class TriviaCategoryViewHolder extends RecyclerView.ViewHolder {

    protected CardView cardView;
    protected TextView name;
    protected ImageView imageView;
    private Utils utils;
    private Context mContext;
    private int firebaseId;

    public TriviaCategoryViewHolder(View v, Context context) {
        super(v);
        cardView =  (CardView) v.findViewById(R.id.trivia_category_card_view);
        name =  (TextView) v.findViewById(android.R.id.text1);
        imageView = (ImageView)  v.findViewById(R.id.trivia_category_image);
        mContext = context;
    }

    public void setData(Cursor c) {
        utils = new Utils();
        String content = c.getString(c.getColumnIndex(TriviasProvider.NAME));
        name.setText(content);

        firebaseId = c.getInt(c.getColumnIndex(TriviasProvider.FIREBASE_ID));

        String imagePath = c.getString(c.getColumnIndex(TriviasProvider.IMAGE_PATH));
        File imageFile = utils.getFileFromInternalStorage(mContext, imagePath);
        Picasso.with(mContext).load(imageFile).into(imageView);
    }
}
