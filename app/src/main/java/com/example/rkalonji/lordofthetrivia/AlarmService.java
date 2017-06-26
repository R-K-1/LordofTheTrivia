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
        context = getApplicationContext();
        utils = new Utils();
        if (utils.networkUp(context)) {
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
        } else {
            Log.w(LOG_TAG, "Cannot retrieve updates not network connection");
        }
    }

    private void retrieveServerUpdate (FirebaseUser firebaseUser) {
        Log.d(LOG_TAG, "executing task after sign in");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        filesDirName = "LOTTR";
        filesDir = context.getDir(filesDirName, Context.MODE_PRIVATE);
        db = utils.returnWritableDatabase(getApplicationContext());
        triviasProvider = new TriviasProvider();
        final String[] categoriesToKeep = new String[1];

        firebaseDatabase.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

                    boolean saveImage = false;

                    Cursor c = db.rawQuery(triviasProvider.returnSelectOneItemStatement(
                            triviaCategory.getFirebaseId(), triviasProvider.CATEGORY_TABLE_NAME, true, true), null);
                    if (c != null && c.moveToFirst()) {
                        do {
                            if (triviaCategory.version != c.getInt(c.getColumnIndex(triviasProvider.VERSION))) {
                                saveImage = true;

                                utils.deleteImageFromInternalStorage(context, c.getString(c.getColumnIndex(triviasProvider.IMAGE_PATH)));
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
                System.out.println("The categories read failed: " + databaseError.getCode());
            }
        });

        firebaseDatabase.child("triviaSets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String query = "";
                ContentValues triviaSetValues = new ContentValues();

                // This will be used to find all categories that need to be deleted
                String idsTriviaSetsToKeep = "(";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TriviaSet triviaSet = snapshot.getValue(TriviaSet.class);

                    idsTriviaSetsToKeep += triviaSet.firebaseId + ",";

                    triviaSetValues.clear();
                    triviaSetValues.put(triviasProvider.CATEGORY_FIREBASE_ID, triviaSet.categoryFirebaseId);
                    triviaSetValues.put(triviasProvider.CATEGORY_VERSION, triviaSet.categoryVersion);
                    triviaSetValues.put(triviasProvider.IMAGE_PATH, triviaSet.imagePath);
                    triviaSetValues.put(triviasProvider.NAME, triviaSet.name);
                    triviaSetValues.put(triviasProvider.VERSION, triviaSet.version);
                    triviaSetValues.put(triviasProvider.FIREBASE_ID, triviaSet.firebaseId);

                    boolean saveImage = false;

                    Cursor c = db.rawQuery(triviasProvider.returnSelectOneItemStatement(
                            triviaSet.firebaseId, triviasProvider.TRIVIA_SET_TABLE_NAME, true, true), null);
                    if (c != null && c.moveToFirst()) {
                        do {
                            if (triviaSet.version != c.getInt(c.getColumnIndex(triviasProvider.VERSION))) {
                                saveImage = true;

                                utils.deleteImageFromInternalStorage(context, c.getString(c.getColumnIndex(triviasProvider.IMAGE_PATH)));
                                String whereClause = triviasProvider.FIREBASE_ID + "=" + triviaSet.firebaseId;
                                db.update(triviasProvider.TRIVIA_SET_TABLE_NAME, triviaSetValues, whereClause, null);
                            }

                        } while (c.moveToNext());
                    } else {
                        saveImage = true;
                        db.insert(triviasProvider.TRIVIA_SET_TABLE_NAME, "", triviaSetValues);
                    }

                    if (saveImage ) {
                        imagePathReference = firebaseStorage.child(triviaSet.imagePath);
                        utils.saveFileFromFirebase(context, imagePathReference, triviaSet.imagePath);
                    }
                }
                // removing the last comma and clausing the parenthesis
                idsTriviaSetsToKeep = idsTriviaSetsToKeep.substring(0, idsTriviaSetsToKeep.length() - 1) + ")";

                query = triviasProvider.returnSelectItemsToDeleteStatement(idsTriviaSetsToKeep,
                        triviasProvider.TRIVIA_SET_TABLE_NAME, true);
                Cursor c = db.rawQuery(query, null);
                if (c != null && c.moveToFirst()) {
                    do {
                        utils.deleteImageFromInternalStorage(context, c.getString(c.getColumnIndex(triviasProvider.IMAGE_PATH)));
                        String where = triviasProvider._ID + "=" + c.getInt(c.getColumnIndex(triviasProvider._ID));
                        db.delete(triviasProvider.TRIVIA_SET_TABLE_NAME, where, null);
                    } while (c.moveToNext());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The trivia sets read failed: " + databaseError.getCode());
            }
        });

        firebaseDatabase.child("questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String query = "";
                ContentValues questionValues = new ContentValues();

                // This will be used to find all categories that need to be deleted
                String idsQuestionsToKeep = "(";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);

                    idsQuestionsToKeep += question.firebaseId + ",";

                    questionValues.clear();
                    questionValues.put(triviasProvider.FIREBASE_ID, question.firebaseId);
                    questionValues.put(triviasProvider.VERSION, question.version);
                    questionValues.put(triviasProvider.TRIVIA_SET_FIREBASE_ID, question.triviaSetFirebaseId);
                    questionValues.put(triviasProvider.TEXT, question.text);

                    boolean saveImage = false;

                    Cursor c = db.rawQuery(triviasProvider.returnSelectOneItemStatement(
                            question.firebaseId, triviasProvider.QUESTION_TABLE_NAME, true, false), null);
                    if (c != null && c.moveToFirst()) {
                        do {
                            if (question.version != c.getInt(c.getColumnIndex(triviasProvider.VERSION))) {
                                String whereClause = triviasProvider.FIREBASE_ID + "=" + question.firebaseId;
                                db.update(triviasProvider.QUESTION_TABLE_NAME, questionValues, whereClause, null);
                            }

                        } while (c.moveToNext());
                    } else {
                        db.insert(triviasProvider.QUESTION_TABLE_NAME, "", questionValues);
                    }
                }
                // removing the last comma and clausing the parenthesis
                idsQuestionsToKeep = idsQuestionsToKeep.substring(0, idsQuestionsToKeep.length() - 1) + ")";

                query = triviasProvider.returnSelectItemsToDeleteStatement(idsQuestionsToKeep,
                        triviasProvider.QUESTION_TABLE_NAME, false);
                Cursor c = db.rawQuery(query, null);
                if (c != null && c.moveToFirst()) {
                    do {
                        String where = triviasProvider._ID + "=" + c.getInt(c.getColumnIndex(triviasProvider._ID));
                        db.delete(triviasProvider.QUESTION_TABLE_NAME, where, null);
                    } while (c.moveToNext());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The questions read failed: " + databaseError.getCode());
            }
        });

        firebaseDatabase.child("options").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String query = "";
                ContentValues optionValues = new ContentValues();

                // This will be used to find all categories that need to be deleted
                String optionsToKeep = "(";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Option option = snapshot.getValue(Option.class);

                    optionsToKeep += option.firebaseId + ",";

                    optionValues.clear();
                    optionValues.put(triviasProvider.FIREBASE_ID, option.firebaseId);
                    optionValues.put(triviasProvider.TEXT, option.text);
                    optionValues.put(triviasProvider.IS_ANSWER, option.isAnswer);
                    optionValues.put(triviasProvider.QUESTION_FIREBASE_ID, option.questionFirebaseId);
                    optionValues.put(triviasProvider.VERSION, option.version);

                    Cursor c = db.rawQuery(triviasProvider.returnSelectOneItemStatement(
                            option.firebaseId, triviasProvider.OPTION_TABLE_NAME, true, false), null);
                    if (c != null && c.moveToFirst()) {
                        do {
                            if (option.version != c.getInt(c.getColumnIndex(triviasProvider.VERSION))) {
                                String whereClause = triviasProvider.FIREBASE_ID + "=" + option.firebaseId;
                                db.update(triviasProvider.OPTION_TABLE_NAME, optionValues, whereClause, null);
                            }
                        } while (c.moveToNext());
                    } else {
                        db.insert(triviasProvider.OPTION_TABLE_NAME, "", optionValues);
                    }
                }
                // removing the last comma and clausing the parenthesis
                optionsToKeep = optionsToKeep.substring(0, optionsToKeep.length() - 1) + ")";

                query = triviasProvider.returnSelectItemsToDeleteStatement(optionsToKeep,
                        triviasProvider.OPTION_TABLE_NAME, false);
                Cursor c = db.rawQuery(query, null);
                if (c != null && c.moveToFirst()) {
                    do {
                        String where = triviasProvider._ID + "=" + c.getInt(c.getColumnIndex(triviasProvider._ID));
                        db.delete(triviasProvider.OPTION_TABLE_NAME, where, null);
                    } while (c.moveToNext());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The options read failed: " + databaseError.getCode());
            }
        });

        firebaseDatabase.child("scores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                db.delete(triviasProvider.BEST_SCORE_TABLE_NAME, null, null);

                ContentValues scoreValues = new ContentValues();

                // This will be used to find all categories that need to be deleted
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Score score = snapshot.getValue(Score.class);

                    scoreValues.clear();
                    scoreValues.put(triviasProvider.USERNAME, score.username);
                    scoreValues.put(triviasProvider.TRIVIA_SET_FIREBASE_ID, score.triviaSetFirebaseId);
                    scoreValues.put(triviasProvider.SCORE, score.score);

                    db.insert(triviasProvider.BEST_SCORE_TABLE_NAME, "", scoreValues);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The scores read failed: " + databaseError.getCode());
            }
        });
    }
}
