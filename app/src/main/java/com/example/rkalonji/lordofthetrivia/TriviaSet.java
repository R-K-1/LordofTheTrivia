package com.example.rkalonji.lordofthetrivia;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.category;

/**
 * Created by rkalonji on 06/24/2017.
 */

public class TriviaSet {
    public int categoryId;
    public int categoryVersion;
    public int id;
    public String imagePath;
    public String name;
    public int version;

    public TriviaSet() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public TriviaSet(int categoryId, int categoryVersion, int id,
                        String imagePath, String name, int version) {
        this.categoryId = categoryId;
        this.categoryVersion = categoryVersion;
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.version = version;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("categoryId", categoryId);
        result.put("categoryVersion", categoryVersion);
        result.put("firebaseId", id);
        result.put("imagePath", imagePath);
        result.put("name", name);
        result.put("version", version);

        return result;
    }
}
