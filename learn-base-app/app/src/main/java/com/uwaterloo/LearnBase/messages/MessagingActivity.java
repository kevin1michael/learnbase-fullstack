package com.uwaterloo.LearnBase.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uwaterloo.LearnBase.R;
import com.uwaterloo.LearnBase.login.UserUtility;

public class MessagingActivity extends AppCompatActivity implements ChildEventListener{

    public static final String MESSAGE_FIREBASE_KEY = "messages";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference messagesReference = database.getReference(MESSAGE_FIREBASE_KEY);

    private RecyclerView messagesList;
    private MessageAdapter messageAdapter;

    private EditText messageEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        messagesList = (RecyclerView) findViewById(R.id.messages_list);
        messagesList.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false));

        messageEntry = (EditText) findViewById(R.id.message_entry);
        messageEntry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean enterWasPressed = event == null
                                || (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN);
                if (enterWasPressed) {
                    sendMessage(null);
                    return true;
                }

                return false;
            }
        });

        messageAdapter = new MessageAdapter();
        messagesList.setAdapter(messageAdapter);
        messagesReference.addChildEventListener(this);
    }

    public void sendMessage(View view) {

        pushMessage2LearnbaseFirebase();

        resetMessageEntry();

        hideKeyboard();
    }

    private void pushMessage2LearnbaseFirebase() {
        String messageContent = messageEntry.getText().toString();
        String username = UserUtility.getUsername(this);
        long currentTime = System.currentTimeMillis();
        Message messageToSend = new Message(username, messageContent, currentTime);
        messagesReference.push().setValue(messageToSend);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(messageEntry.getWindowToken(), 0);
    }


    private void resetMessageEntry() {
        messageEntry.setText("");
    }


    private void scrollToMostRecentMessage() {
        int mostRecentMessageIndex = messageAdapter.getItemCount() - 1;
        messagesList.smoothScrollToPosition(mostRecentMessageIndex);
    }


    public static void start(Context context) {
        Intent startMessagingActivity = new Intent(context, MessagingActivity.class);
        context.startActivity(startMessagingActivity);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Message rxMesg = dataSnapshot.getValue(Message.class);
        messageAdapter.addMessage(rxMesg);
        scrollToMostRecentMessage();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}