package com.uwaterloo.LearnBase.students;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uwaterloo.LearnBase.R;
import com.uwaterloo.LearnBase.login.LoginActivity;
import com.uwaterloo.LearnBase.login.UserUtility;

import java.util.ArrayList;


public class StudentAdapter extends RecyclerView.Adapter<StudentsViewHolder> {
    private ArrayList<String> students = new ArrayList<>();

    @Override
    public StudentsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View studentView = inflater.inflate(R.layout.student_layout, parent, false);
        StudentsViewHolder vh = new StudentsViewHolder(studentView, new StudentsViewHolder.IMyViewHolderClicks() {
            public void onStudentClickedHandler(View caller,String student) {

                UserNameSelected(context, student);
            };
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(StudentsViewHolder holder, int position) {
        String student = students.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public void addStudent(String student) {
        students.add(student);
        notifyItemChanged(0);
    }

    public void UserNameSelected(Context context, String student) {
        UserUtility.setUsername(context, student);
        LoginActivity.start(context);
        ((StudentListActivity)context).finish();
    }

}
