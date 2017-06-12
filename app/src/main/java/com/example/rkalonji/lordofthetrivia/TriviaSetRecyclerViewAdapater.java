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

public class TriviaSetRecyclerViewAdapater extends RecyclerView
        .Adapter<TriviaSetRecyclerViewAdapater
        .DataObjectHolder> {
    private static String LOG_TAG = "TriviaRecyclerAdapter";
    private ArrayList<Question> mDataset;
    private static MyClickListener myClickListener;
    private Context mContext;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        LinearLayout triviaSetQuestionOptionsLayout;

        public DataObjectHolder(View itemView) {
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

    public TriviaSetRecyclerViewAdapater(ArrayList<Question> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trivia_set_question_layout, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getmText1());
        String options = mDataset.get(position).getmText2();
        String[] optionsArray = options.split("\\|");
        holder.triviaSetQuestionOptionsLayout.setOrientation(LinearLayout.VERTICAL);
        for (String option:optionsArray) {
            String[] optionArray = option.split("\\/");
            final ToggleButton btnTag = new ToggleButton(mContext);
            btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            btnTag.setText(optionArray[0]);
            btnTag.setTextOn(optionArray[0]);
            btnTag.setTextOff(optionArray[0]);
            btnTag.setId(Integer.parseInt(optionArray[2]));
            btnTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((ToggleButton) v).isChecked()) {
                        btnTag.setBackgroundResource(R.color.colorPrimaryDark);
                    } else {
                        btnTag.setBackgroundResource(R.color.gray);
                    }
                }
            });
            holder.triviaSetQuestionOptionsLayout.addView(btnTag);
        }
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
