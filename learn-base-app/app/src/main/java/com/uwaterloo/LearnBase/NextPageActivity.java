package com.uwaterloo.LearnBase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uwaterloo.LearnBase.R;

public class NextPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_page);
    }

    public static void start(Context context) {
        Intent startNextPageActivity = new Intent(context, NextPageActivity.class);
        context.startActivity(startNextPageActivity);
    }
}
