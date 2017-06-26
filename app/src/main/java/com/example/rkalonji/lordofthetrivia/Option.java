package com.example.rkalonji.lordofthetrivia;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkalonji on 06/23/2017.
 */
@IgnoreExtraProperties
public class Option {
    public int firebaseId;
    public boolean isAnswer;
    public int questionFirebaseId;
    public String text;
    public int version;

    public Option() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Option(int firebaseId, boolean isAnswer, int questionFirebaseId, String text, int version) {
        this.firebaseId = firebaseId;
        this.isAnswer = isAnswer;
        this.questionFirebaseId = questionFirebaseId;
        this.text = text;
        this.version = version;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firebaseId", firebaseId);
        result.put("isAnswer", isAnswer);
        result.put("questionFirebaseId", questionFirebaseId);
        result.put("text", text);
        result.put("version", version);

        return result;
    }
}
