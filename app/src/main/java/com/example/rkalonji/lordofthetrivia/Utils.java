package com.example.rkalonji.lordofthetrivia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import static android.R.attr.y;

/**
 * Created by rkalonji on 05/31/2017.
 */

public class Utils {

    private String filesDirName = "LOTTR";


    public void loadAddBanner (View v, int resourceId) {
        AdView mAdView = (AdView) v.findViewById(resourceId);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    public SQLiteDatabase returnWritableDatabase (Context context) {
        TriviasProvider.DatabaseHelper x = new TriviasProvider.DatabaseHelper(context);
        return x.getWritableDatabase();
    }

    public void saveFileFromFirebase(Context context, StorageReference filePathReference,
                                     String filePath) {
        File filesDir = context.getDir(filesDirName, Context.MODE_PRIVATE);
        File file = new File(filesDir, filePath);
        filePathReference.getFile(file).addOnSuccessListener(
                new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public File getFileFromInternalStorage (Context context, String filePath) {
        File directory = context.getDir(filesDirName, Context.MODE_PRIVATE);
        File file = new File(directory, filePath);

        return file;
    }

    public void deleteImageFromInternalStorage (Context context, String filePath) {
        File directory = context.getDir(filesDirName, Context.MODE_PRIVATE);
        File file = new File(directory, filePath);
        if (file.delete()) {
            Log.i("file", "deleted " + filePath);
        } else {
            Log.i("file", "could not delete " + filePath);
        }
    }

    public boolean networkUp(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
