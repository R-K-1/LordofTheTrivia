package com.example.rkalonji.lordofthetrivia;

import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkalonji on 06/24/2017.
 */

public class Score implements Parcelable {
    public String username;
    public int score;
    public int triviaSetFirebaseId;

    public Score() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Score(String username, int score, int triviaSetFirebaseId) {
        this.username = username;
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

    protected Score(Parcel in) {
        username = in.readString();
        score = in.readInt();
        triviaSetFirebaseId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeInt(score);
        dest.writeInt(triviaSetFirebaseId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Score> CREATOR = new Parcelable.Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };
}
