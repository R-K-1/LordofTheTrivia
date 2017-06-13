package com.example.rkalonji.lordofthetrivia;

/**
 * Created by rkalonji on 06/08/2017.
 */

public class Question {
    private String mQuestionText;
    private String mQuestionOptions;

    Question (String questionText, String questionOptions){
        mQuestionText = questionText;
        mQuestionOptions = questionOptions;
    }

    public String getmQuestionText() {
        return mQuestionText;
    }

    public void setmQuestionText(String questionText) {
        this.mQuestionText = questionText;
    }

    public String getmQuestionOptions() {
        return mQuestionOptions;
    }

    public void setmQuestionOptions(String questionOptions) {
        this.mQuestionOptions = questionOptions;
    }
}
