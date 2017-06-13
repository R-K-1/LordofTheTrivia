package com.example.rkalonji.lordofthetrivia;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.duration;

/**
 * Created by rkalonji on 05/29/2017.
 */

public class TriviaSetDetailsFragment extends Fragment {

    private Utils utils;

    private RecyclerView mRecyclerView;
    // private RecyclerView.Adapter mAdapter;
    private TriviaSetRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "TriviaDetailsFragment";
    private static String mTriviaSetFirebaseId = "";
    private Map<String, Integer> questionsStatus;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.trivia_set_details, container, false);

        utils = new Utils();
        utils.loadAddBanner(rootView, R.id.trivia_set_details_ad_view);

        // Initialize HashMap that will hold questions status to help calculating scores
        // The three possible status are  0: untouched ; 1: touched false; 2: true
        // touched and false is necessary to correct question with multiple correct answers
        questionsStatus = new HashMap<String, Integer>();

        final Button submitTriviaButton = (Button) rootView.findViewById(R.id.submit_trivia);
        submitTriviaButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                for (ToggleButton option : mAdapter.getOptions()) {
                    if (option.isChecked()) {
                        if (option.getTag(R.string.is_answer).equals("1")) {
                            option.setBackgroundResource(R.color.green);

                            Integer questionStatus =
                                    (Integer) questionsStatus.get(option.getTag(R.string.question_id));
                            switch (questionStatus) {
                                case 0:
                                    questionsStatus.put((String) option.getTag(R.string.question_id), 2);
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    break;
                                default:
                                    questionsStatus.put((String) option.getTag(R.string.question_id), 2);
                                    break;
                            }

                        } else {
                            option.setBackgroundResource(R.color.red);
                            questionsStatus.put((String) option.getTag(R.string.question_id), 1);
                        }
                    }
                }

                // count questions answered correctly
                double numberOfQuestions = questionsStatus.size();
                double numberOfCorrectAnswers = 0;

                for (Map.Entry<String, Integer> questionStatus : questionsStatus.entrySet()) {
                    if (questionStatus.getValue().equals(2)) {
                        numberOfCorrectAnswers += 1;
                    }
                }
                double percentage = (numberOfCorrectAnswers / numberOfQuestions) * 100;
                Toast.makeText(getContext(), String.valueOf(percentage), Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    private void updateQuestionStatus (ToggleButton option) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.trivia_set_details_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        new getQuestionsFromDB().execute(mTriviaSetFirebaseId);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setGlobalTriviaSetFirebaseId(String triviaSetFirebaseId) {
        mTriviaSetFirebaseId = triviaSetFirebaseId;
    }

    public class getQuestionsFromDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(String content) {
            ArrayList<Question> questions = new ArrayList<Question>();//Creating arraylist
            Uri questionsUri = Uri.parse(TriviasProvider.GET_QUESTIONS_URI + content);
            Cursor questionsCursor = getContext().getContentResolver().query(
                    questionsUri, null, null, null, null);

            if (questionsCursor != null && questionsCursor.moveToFirst()) {
                do {
                    String questionId = questionsCursor.getString(questionsCursor.getColumnIndex(TriviasProvider._ID));
                    questionsStatus.put(questionId, 0);
                    Uri optionsUri = Uri.parse(TriviasProvider.GET_OPTIONS_URI
                            + questionId);
                    Cursor optionsCursor = getContext().getContentResolver().query(
                            optionsUri, null, null, null, null);
                    String options = "";
                    if (optionsCursor != null && optionsCursor.moveToFirst()) {
                        do {
                            options += optionsCursor.getString(optionsCursor.getColumnIndex(TriviasProvider.TEXT));
                            options += "/";
                            options += optionsCursor.getString(optionsCursor.getColumnIndex(TriviasProvider.IS_ANSWER));
                            options += "/";
                            options += optionsCursor.getString(optionsCursor.getColumnIndex(TriviasProvider._ID));
                            options += "/";
                            options += questionId;
                            options += "|";
                        } while (optionsCursor.moveToNext());
                    }
                    Question question = new Question(questionsCursor.getString(questionsCursor.getColumnIndex(TriviasProvider.TEXT)),
                            options);
                    questions.add(question);
                } while (questionsCursor.moveToNext());
            }

            mAdapter = new TriviaSetRecyclerViewAdapter(questions, getContext());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

}
