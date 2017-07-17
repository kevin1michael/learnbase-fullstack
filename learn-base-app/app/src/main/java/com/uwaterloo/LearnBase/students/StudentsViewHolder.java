package com.uwaterloo.LearnBase.students;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.uwaterloo.LearnBase.R;

/**
 * Created by alireza on 7/15/2017.
 */

public  class StudentsViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
    private TextView usernameDisplay;
    public IMyViewHolderClicks mListener;

    public StudentsViewHolder(View itemView, IMyViewHolderClicks listener) {
        super(itemView);

        mListener = listener;
        usernameDisplay = (TextView) itemView.findViewById(R.id.student);
        usernameDisplay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        mListener.onStudentClickedHandler(v, usernameDisplay.getText().toString());

    }
    public void bind(String student) {
        usernameDisplay.setText(student);
    }
    public static interface IMyViewHolderClicks {
        public void onStudentClickedHandler(View caller,String student);
    }

}
