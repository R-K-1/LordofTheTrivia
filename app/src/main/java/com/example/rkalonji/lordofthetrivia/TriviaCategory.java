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
    public String imagePath;

    public TriviaCategory() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public TriviaCategory(int id, String title, String imagePath) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firebaseId", id);
        result.put("title", title);
        result.put("imagePath", imagePath);

        return result;
    }
}
