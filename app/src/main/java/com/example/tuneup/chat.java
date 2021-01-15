package com.example.tuneup;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class chat extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private EditText messageInput;
    private TextView messageOutput;
    private String messageString;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        list = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<String>();
        messageInput = findViewById(R.id.messageInput);
        messageOutput = findViewById(R.id.messageOutput);

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);

        list.setAdapter(adapter);

        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("Message");


            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    messageString = snapshot.getValue().toString();
                    messageString = messageString.substring(1,messageString.length()-1);

                    String[] stringMessageArray = messageString.split(", ");
                    Arrays.sort(stringMessageArray);
                    String[] stringFinal = new String[stringMessageArray.length*2];

                    adapter.clear();

                    try {
                        for (int i = 0; i < stringMessageArray.length; i++) {
                            String[] stringKeyValue = stringMessageArray[i].split("=", 2);
                            stringFinal[2 * i] = sdf1.format(Long.parseLong(stringKeyValue[0]));
                            stringFinal[2 * i + 1] = stringKeyValue[1];
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }


                    int temp = 1, temp2 = 0;
                    String[] displayString = new String[stringFinal.length/2];
                    for (int i = 0; i < stringFinal.length; i++){
                        if (temp%2==0){
                            displayString[temp2] = stringFinal[i-1]+" "+stringFinal[i];
                            arrayList.add(displayString[temp2]);
                            temp2++;
                        }
                        temp++;
                    }

                    adapter.notifyDataSetChanged();
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