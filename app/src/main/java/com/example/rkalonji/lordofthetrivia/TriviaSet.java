package com.example.rkalonji.lordofthetrivia;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkalonji on 06/24/2017.
 */

public class TriviaSet {
    public int categoryFirebaseId;
    public int categoryVersion;
    public int id;
    public String imagePath;
    public String name;
    public int version;
    public int firebaseId;

    public TriviaSet() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public TriviaSet(int categoryFirebaseId, int categoryVersion, int firebaseId,
                        String imagePath, String name, int version) {
        this.categoryFirebaseId = categoryFirebaseId;
        this.categoryVersion = categoryVersion;
        this.firebaseId = firebaseId;
        this.imagePath = imagePath;
        this.name = name;
        this.version = version;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("categoryFirebaseId", categoryFirebaseId);
        result.put("categoryVersion", categoryVersion);
        result.put("firebaseId", firebaseId);
        result.put("imagePath", imagePath);
        result.put("name", name);
        result.put("version", version);

        return result;
    }
}
