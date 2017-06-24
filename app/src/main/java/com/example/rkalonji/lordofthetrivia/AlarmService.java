package com.example.rkalonji.lordofthetrivia;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Rkalonji on 06/14/2017.
 */

public class AlarmService extends IntentService {
    private Context context;
    private static String LOG_TAG = "ServerUpdateJob";
    private FirebaseAuth mAuth;
    private DatabaseReference firebaseDatabase;

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

        firebaseDatabase.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<TriviaCategory> options = new ArrayList<TriviaCategory>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TriviaCategory triviaCategory = snapshot.getValue(TriviaCategory.class);
                    options.add(triviaCategory);
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
}
