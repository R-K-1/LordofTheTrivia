package com.example.rkalonji.lordofthetrivia;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rkalonji on 05/10/2017.
 */

public class ExampleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.example_fragment, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

/*        return new CursorLoader(this,
                Uri.parse("content://com.github.browep.cursorloader.data")
                , new String[]{"col1"}, null, null, null);*/
        return new CursorLoader(getContext(), null, null, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursor.moveToFirst();
        // String text = (String) textView.getText();
        while (!cursor.isAfterLast()) {
            // text += "<br />" + cursor.getString(1);
            cursor.moveToNext();
        }

        // textView.setText(Html.fromHtml(text) );

    }

    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
