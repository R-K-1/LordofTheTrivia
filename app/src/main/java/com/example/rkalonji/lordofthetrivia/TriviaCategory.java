package com.example.rkalonji.lordofthetrivia;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkalonji on 06/24/2017.
 */

public class TriviaCategory {
    public int firebaseId;
    public String name;
    public String imagePath;
    public int version;

    public TriviaCategory() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public TriviaCategory(int firebaseId, String title, String imagePath, int version) {
        this.firebaseId = firebaseId;
        this.name = title;
        this.imagePath = imagePath;
        this.version = version;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firebaseId", firebaseId);
        result.put("name", name);
        result.put("imagePath", imagePath);
        result.put("version", version);

        return result;
    }

    public int getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(int firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
