package com.example.rkalonji.lordofthetrivia;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkalonji on 06/24/2017.
 */

public class Score {
    public String username;
    public int score;
    public int triviaSetFirebaseId;

    public Score() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Score(String name, int score, int triviaSetFirebaseId, String username) {
        this.username = name;
        this.score = score;
        this.triviaSetFirebaseId = triviaSetFirebaseId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("score", score);
        result.put("triviaSetFirebaseId", triviaSetFirebaseId);

        return result;
    }
}
