package com.uwaterloo.LearnBase.students;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.uwaterloo.LearnBase.R;
import com.uwaterloo.LearnBase.login.LoginActivity;

public class StudentListActivity extends AppCompatActivity {
    private RecyclerView studentsList;
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        studentsList = (RecyclerView) findViewById(R.id.students_list);
        studentsList.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false));
        studentAdapter = new StudentAdapter();
        studentsList.setAdapter(studentAdapter);
        studentAdapter.addStudent("Alireza");
        studentAdapter.addStudent("Ibad");
        studentAdapter.addStudent("Kevin");
        studentAdapter.addStudent("Yoyo");
        scrollToMostRecentStudent();

    }
    private void scrollToMostRecentStudent() {
        int mostRecentStudentIndex = studentAdapter.getItemCount() - 1;
        studentsList.smoothScrollToPosition(mostRecentStudentIndex);
    }

}
