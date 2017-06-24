package com.example.rkalonji.lordofthetrivia;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;

/**
 * Created by rkalonji on 06/24/2017.
 */

public class Score {
    public String name;
    public int score;
    public int triviaSetFirebaseId;

    public Score() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Score(String name, int score, int triviaSetFirebaseId) {
        this.name = name;
        this.score = score;
        this.triviaSetFirebaseId = triviaSetFirebaseId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("score", score);
        result.put("triviaSetFirebaseId", triviaSetFirebaseId);

        return result;
    }
}
