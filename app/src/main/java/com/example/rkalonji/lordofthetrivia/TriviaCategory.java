package com.example.rkalonji.lordofthetrivia;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkalonji on 06/24/2017.
 */

public class TriviaCategory {
    public int id;
    public String title;

    public TriviaCategory() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public TriviaCategory(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firebaseId", id);
        result.put("title", title);

        return result;
    }
}
