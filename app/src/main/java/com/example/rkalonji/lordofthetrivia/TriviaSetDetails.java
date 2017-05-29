package com.example.rkalonji.lordofthetrivia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

/**
 * Created by rkalonji on 05/29/2017.
 */

public class TriviaSetDetails extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.trivia_set_details, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.questions_container);
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
        }


    }

}
