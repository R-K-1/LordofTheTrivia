package com.example.rkalonji.lordofthetrivia;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.widget.TextView;

public class TestActivity extends FragmentActivity implements
        LoaderCallbacks<Cursor> {

    TextView resTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resTextView = (TextView) findViewById(R.id.res);
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();
        ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, "Test")
                .withValue(RawContacts.ACCOUNT_NAME, "CTE").build());

        ops.add(ContentProviderOperation
                .newInsert(Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, "test name")
                .withValue(Phone.NUMBER, 1234567890).build());

        try {
            //Use below statement to insert a new contact
            //getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            // TODO: handle exception
        }
        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        // TODO Auto-generated method stub
        Uri CONTENT_URI = ContactsContract.RawContacts.CONTENT_URI;
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        // TODO Auto-generated method stub
        arg1.moveToFirst();
        StringBuilder res = new StringBuilder();
        while(!arg1.isAfterLast())
        {
            res.append("\n" + arg1.getString(21) + "-" + arg1.getString(22));

            arg1.moveToNext();
        }
        resTextView.setText(res);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
        // If the Cursor is being placed in a CursorAdapter, you should use the
        // swapCursor(null) method to remove any references it has to the
        // Loader's data.
    }

}