package com.uwaterloo.LearnBase.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;

import com.uwaterloo.LearnBase.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.uwaterloo.LearnBase.StoriesActivity;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String usernameSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        TextView userTextView = (TextView) findViewById(R.id.textViewUser);
        userTextView.setText(UserUtility.getUsername(this));

        ImageButton ImageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        ImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Password string is hardcoded and should come from database
                loginUser("111111");
            }
        });

        ImageButton ImageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Password string is hardcoded and should come from database
                loginUser("222222");
            }
        });
        ImageButton ImageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Password string is hardcoded and should come from database
                loginUser("333333");
            }
        });
        ImageButton ImageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        ImageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Password string is hardcoded and should come from database
                loginUser("444444");
            }
        });
        ImageButton ImageButton5 = (ImageButton) findViewById(R.id.imageButton5);
        ImageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Password string is hardcoded and should come from database
                loginUser("555555");
            }
        });
        ImageButton ImageButton6 = (ImageButton) findViewById(R.id.imageButton6);
        ImageButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Password string is hardcoded and should come from database
                loginUser("666666");
            }
        });
        ImageButton ImageButton7 = (ImageButton) findViewById(R.id.imageButton7);
        ImageButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Password string is hardcoded and should come from database
                loginUser("777777");
            }
        });
        ImageButton ImageButton8 = (ImageButton) findViewById(R.id.imageButton8);
        ImageButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Password string is hardcoded and should come from database
                loginUser("888888");
            }
        });
        ImageButton ImageButton9 = (ImageButton) findViewById(R.id.imageButton9);
        ImageButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Password string is hardcoded and should come from database
                loginUser("999999");
            }
        });

        return;
    }

    private void loginUser(String password) {
        String username = UserUtility.getUsername(this)+"@learnbase.com";
        mAuth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete( Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startReadingActivity();
                                }
                            }
                        });

    }

    public static void start(Context context) {
        Intent startLoginActivity = new Intent(context, LoginActivity.class);
        context.startActivity(startLoginActivity);
    }
    private void startReadingActivity() {
        StoriesActivity.start(this);
        finish();
    }


}
