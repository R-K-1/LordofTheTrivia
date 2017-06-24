package com.example.rkalonji.lordofthetrivia;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkalonji on 06/08/2017.
 */

public class Question {
    public int firebaseId;
    public String text;
    public int triviaSetFirebaseId;
    public String mQuestionOptions;

    public Question() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    // This constructor is used whenever questions are retrieved from Firebase
    public Question(int firebaseId, String text, int triviaSetFirebaseId) {
        this.firebaseId = firebaseId;
        this.text = text;
        this.triviaSetFirebaseId = triviaSetFirebaseId;
    }

    // this constructor is uses whenever questions are retrieved from SQLite
    Question (String questionText, String questionOptions){
        text = questionText;
        mQuestionOptions = questionOptions;
    }

    public String getText() {
        return text;
    }

    public void setText(String questionText) {
        this.text = questionText;
    }

    public String getmQuestionOptions() {
        return mQuestionOptions;
    }

    public void setmQuestionOptions(String questionOptions) {
        this.mQuestionOptions = questionOptions;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firebaseId", firebaseId);
        result.put("text", text);
        result.put("triviaSetFirebaseId", triviaSetFirebaseId);

        return result;
    }
}
