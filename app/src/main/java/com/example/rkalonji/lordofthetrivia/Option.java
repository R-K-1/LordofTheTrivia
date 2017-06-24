package com.example.rkalonji.lordofthetrivia;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.author;

/**
 * Created by rkalonji on 06/23/2017.
 */
@IgnoreExtraProperties
public class Option {
    public int id;
    public boolean isAnswer;
    public String questionId;
    public String text;

    public Option() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Option(int id, boolean isAnswer, String text, String questionId) {
        this.id = id;
        this.isAnswer = isAnswer;
        this.questionId = questionId;
        this.text = text;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("isAnswer", isAnswer);
        result.put("questionId", questionId);
        result.put("text", text);

        return result;
    }
}
