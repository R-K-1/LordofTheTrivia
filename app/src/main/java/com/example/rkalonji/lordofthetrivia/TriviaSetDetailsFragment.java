package com.example.rkalonji.lordofthetrivia;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.math.BigInteger;
import java.util.ArrayList;

import static android.R.attr.max;

/**
 * Created by rkalonji on 05/29/2017.
 */

public class TriviaSetDetailsFragment extends Fragment {

    private Utils utils;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "TriviaDetailsFragment";
    private static String globalTriviaSetFirebaseId = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.trivia_set_details, container, false);

        utils = new Utils();
        utils.loadAddBanner(rootView, R.id.adViewTriviaSetDetails);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        new getQuestionsFromDB().execute(globalTriviaSetFirebaseId);
/*        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);*/

/*        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.questions_container);
        layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"

        for (int i = 0; i < 3; i++) {
            LinearLayout row = new LinearLayout(getContext());
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 4; j++) {
                Button btnTag = new Button(getContext());
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                btnTag.setText("Button " + (j + 1 + (i * 4)));
                btnTag.setId(j + 1 + (i * 4));
                row.addView(btnTag);
            }

            layout.addView(row);
        }*/


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
        globalTriviaSetFirebaseId = triviaSetFirebaseId;
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
            Cursor c = getContext().getContentResolver().query(
                    questionsUri, null, null, null, null);

            if (c != null && c.moveToFirst()) {
                do {
                    Question question = new Question(c.getString(c.getColumnIndex(TriviasProvider.TEXT)),
                            c.getString(c.getColumnIndex(TriviasProvider.TEXT)));
                    questions.add(question);
                } while (c.moveToNext());
            }

            mAdapter = new TriviaSetRecyclerViewAdapater(questions);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void setQuestionsOptions() {
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.trivia_set_question_options_layout);
        layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"

        for (int i = 0; i < 3; i++) {
            LinearLayout row = new LinearLayout(getContext());
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            Button btnTag = new Button(getContext());
            btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            btnTag.setText("Options " + i);
            btnTag.setId(1 + (int)(Math.random() * 100));
            // row.addView(btnTag);
            layout.addView(btnTag);
        }

    }

}
