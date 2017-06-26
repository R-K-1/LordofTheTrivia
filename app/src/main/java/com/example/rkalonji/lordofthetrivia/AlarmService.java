package com.example.rkalonji.lordofthetrivia;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Rkalonji on 06/14/2017.
 */

public class AlarmService extends IntentService {
    private Context context;
    private ContextWrapper contextWrapper;
    private static String LOG_TAG = "ServerUpdateJob";
    private FirebaseAuth mAuth;
    private DatabaseReference firebaseDatabase;
    private StorageReference firebaseStorage;
    private StorageReference imagePathReference;
    private String filesDirName;
    private File filesDir;
    private Utils utils;
    private SQLiteDatabase db;
    private TriviasProvider triviasProvider;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Executor executor = Executors.newSingleThreadExecutor();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(executor, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            retrieveServerUpdate(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInAnonymously:failure", task.getException());
                        }
                    }
                });
    }

    private void retrieveServerUpdate (FirebaseUser firebaseUser) {
        Log.d(LOG_TAG, "executing task after sign in");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        context = getApplicationContext();
        filesDirName = "LOTTR";
        filesDir = context.getDir(filesDirName, Context.MODE_PRIVATE);
        utils = new Utils();
        db = utils.returnWritableDatabase(getApplicationContext());
        triviasProvider = new TriviasProvider();
        final String[] categoriesToKeep = new String[1];

        firebaseDatabase.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<TriviaCategory> options = new ArrayList<TriviaCategory>();
                String query = "";
                ContentValues triviaCategoryValues = new ContentValues();

                // This will be used to find all categories that need to be deleted
                String idsCategoriesFromFirebase = "(";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TriviaCategory triviaCategory = snapshot.getValue(TriviaCategory.class);

                    idsCategoriesFromFirebase += triviaCategory.firebaseId + ",";

                    triviaCategoryValues.clear();
                    triviaCategoryValues.put(triviasProvider.FIREBASE_ID, triviaCategory.firebaseId);
                    triviaCategoryValues.put(triviasProvider.IMAGE_PATH, triviaCategory.imagePath);
                    triviaCategoryValues.put(triviasProvider.NAME, triviaCategory.name);
                    triviaCategoryValues.put(triviasProvider.VERSION, triviaCategory.version);

                    options.add(triviaCategory);

                    boolean saveImage = false;

                    Cursor c = db.rawQuery(triviasProvider.returnSelectOneItemStatement(
                            triviaCategory.getFirebaseId(), triviasProvider.CATEGORY_TABLE_NAME, true, true), null);
                    if (c != null && c.moveToFirst()) {
                        do {
                            if (triviaCategory.version != c.getInt(c.getColumnIndex(triviasProvider.VERSION))) {
                                saveImage = true;
                                query = triviasProvider.returnUpdateCategoryStatement(triviaCategory.firebaseId,
                                        triviaCategory.imagePath, triviaCategory.name, triviaCategory.version);

                                utils.deleteImageFromInternalStorage(context, c.getString(c.getColumnIndex(triviasProvider.IMAGE_PATH)));
                                // db.rawQuery(query, null);
                                String whereClause = triviasProvider.FIREBASE_ID + "=" + triviaCategory.firebaseId;
                                db.update(triviasProvider.CATEGORY_TABLE_NAME, triviaCategoryValues, whereClause, null);
                            }

                        } while (c.moveToNext());
                    } else {
                        saveImage = true;
                        db.insert(triviasProvider.CATEGORY_TABLE_NAME, "", triviaCategoryValues);
                    }

                    if (saveImage ) {
                        imagePathReference = firebaseStorage.child(triviaCategory.imagePath);
                        utils.saveFileFromFirebase(context, imagePathReference, triviaCategory.imagePath);
                    }
                }
                // removing the last comma and clausing the parenthesis
                idsCategoriesFromFirebase = idsCategoriesFromFirebase.substring(0, idsCategoriesFromFirebase.length() - 1) + ")";

                query = triviasProvider.returnSelectItemsToDeleteStatement(idsCategoriesFromFirebase,
                        triviasProvider.CATEGORY_TABLE_NAME, true);
                Cursor c = db.rawQuery(query, null);
                if (c != null && c.moveToFirst()) {
                    do {
                        utils.deleteImageFromInternalStorage(context, c.getString(c.getColumnIndex(triviasProvider.IMAGE_PATH)));
                        String where = triviasProvider._ID + "=" + c.getInt(c.getColumnIndex(triviasProvider._ID));
                        db.delete(triviasProvider.CATEGORY_TABLE_NAME, where, null);
                    } while (c.moveToNext());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        firebaseDatabase.child("triviaSets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<TriviaSet> triviaSets = new ArrayList<TriviaSet>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TriviaSet triviaSet = snapshot.getValue(TriviaSet.class);
                    triviaSets.add(triviaSet);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        firebaseDatabase.child("questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Question> questions = new ArrayList<Question>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    questions.add(question);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        firebaseDatabase.child("options").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Option> options = new ArrayList<Option>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Option option = snapshot.getValue(Option.class);
                    options.add(option);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        firebaseDatabase.child("scores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Score> scores = new ArrayList<Score>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Score score = snapshot.getValue(Score.class);
                    scores.add(score);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void deleteItems (String query) {
        db.rawQuery(query, null);
    }
}
