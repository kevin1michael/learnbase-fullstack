package com.uwaterloo.LearnBase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uwaterloo.LearnBase.login.UserUtility;
import com.uwaterloo.LearnBase.messages.MessagingActivity;

public class navigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Button messagesButton = (Button) findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMessaging();
            }
        });

        Button readingButton = (Button) findViewById(R.id.readingButton);
        readingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReading();
            }
        });
    }

    public static void start(Context context) {
        Intent startNavigationActivity = new Intent(context, navigation.class);
        context.startActivity(startNavigationActivity);
    }

    private void startMessaging() {
        MessagingActivity.start(this);
        finish();
    }

    private void startReading() {
        StoriesActivity.start(this);
        finish();
    }
}
