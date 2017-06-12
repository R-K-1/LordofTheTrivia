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

import java.util.ArrayList;

/**
 * Created by rkalonji on 05/29/2017.
 */

public class TriviaSetDetailsFragment extends Fragment {

    private Utils utils;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "TriviaDetailsFragment";
    private static String mTriviaSetFirebaseId = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.trivia_set_details, container, false);

        utils = new Utils();
        utils.loadAddBanner(rootView, R.id.trivia_set_details_ad_view);

        return rootView;
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
/*        ((TriviaSetRecyclerViewAdapater) mAdapter).setOnItemClickListener(new TriviaSetRecyclerViewAdapater
                .MyClickListener() {
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });*/
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
                    Uri optionsUri = Uri.parse(TriviasProvider.GET_OPTIONS_URI
                            + questionsCursor.getString(questionsCursor.getColumnIndex(TriviasProvider._ID)));
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
                            options += "|";
                        } while (optionsCursor.moveToNext());
                    }
                    Question question = new Question(questionsCursor.getString(questionsCursor.getColumnIndex(TriviasProvider.TEXT)),
                            options);
                    questions.add(question);
                } while (questionsCursor.moveToNext());
            }

            mAdapter = new TriviaSetRecyclerViewAdapater(questions, getContext());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

}
