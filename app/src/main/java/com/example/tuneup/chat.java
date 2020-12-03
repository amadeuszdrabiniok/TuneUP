package com.example.tuneup;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class chat extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private EditText messageInput;
    private TextView messageOutput;
    private String messageString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageInput = findViewById(R.id.messageInput);
        messageOutput = findViewById(R.id.messageOutput);

        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("Message");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    messageString = snapshot.getValue().toString();
                    String[] stringMessageArray = messageString.split("=",2);
                    messageOutput.setText(stringMessageArray[1]);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void sendButton(View view) {

        Date date = new Date();
        databaseReference.child(Long.toString(date.getTime())).setValue(messageInput.getText().toString());
        messageInput.setText("");
    }
}