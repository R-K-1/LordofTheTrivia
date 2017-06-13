package com.example.rkalonji.lordofthetrivia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by rkalonji on 06/08/2017.
 */

public class TriviaSetRecyclerViewAdapter extends RecyclerView
        .Adapter<TriviaSetRecyclerViewAdapter.QuestionsAndOptionsHolder> {
    private static String LOG_TAG = "TriviaRecyclerAdapter";
    private ArrayList<Question> mDataset;
    private static MyClickListener myClickListener;
    private Context mContext;
    private ArrayList<ToggleButton> mButtons = new ArrayList<ToggleButton>();

    public static class QuestionsAndOptionsHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        LinearLayout triviaSetQuestionOptionsLayout;

        public QuestionsAndOptionsHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.trivia_set_test_textView1);
            triviaSetQuestionOptionsLayout = (LinearLayout) itemView.findViewById(R.id.trivia_set_question_options_layout);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public TriviaSetRecyclerViewAdapter(ArrayList<Question> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public QuestionsAndOptionsHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trivia_set_question_layout, parent, false);

        QuestionsAndOptionsHolder questionsAndOptionsHolder = new QuestionsAndOptionsHolder(view);
        return questionsAndOptionsHolder;
    }

    @Override
    public void onBindViewHolder(QuestionsAndOptionsHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getmQuestionText());
        // questionsOptions is a pipe and slash delimited string containing option information
        // in the following format
        // optiontext/isAnswer/optionId/questionId|
        String options = mDataset.get(position).getmQuestionOptions();
        String[] optionsArray = options.split("\\|");
        holder.triviaSetQuestionOptionsLayout.setOrientation(LinearLayout.VERTICAL);
        for (String option:optionsArray) {
            String[] optionArray = option.split("\\/");
            final ToggleButton btnTag = new ToggleButton(mContext);
            btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            // Option text doesn't change no matter the isChecked Status
            btnTag.setText(optionArray[0]);
            btnTag.setTextOn(optionArray[0]);
            btnTag.setTextOff(optionArray[0]);
            // Set optionId as buttonId
            btnTag.setId(Integer.parseInt(optionArray[2]));
            // To help validating answers and counting score tags are used to keep track of
            // questionId and whether or not option is correct answer
            // Set isAnswer as tag 1
            btnTag.setTag(R.string.is_answer,optionArray[1]);
            // Set questionId as tag 2
            btnTag.setTag(R.string.question_id,optionArray[3]);

            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((ToggleButton) v).isChecked()) {
                        btnTag.setBackgroundResource(R.color.colorPrimary);
                    } else {
                        btnTag.setBackgroundResource(R.color.gray);
                    }
                }
            });
            mButtons.add(btnTag);
            holder.triviaSetQuestionOptionsLayout.addView(btnTag);
        }
    }

    public ArrayList<ToggleButton> getOptions () {
        return mButtons;
    }

    public void addItem(Question dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
