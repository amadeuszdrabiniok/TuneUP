package com.example.tuneup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class GoogleProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "TAG";
    Button save;
    EditText phone, name;
    FirebaseAuth fAuth;
    String userID, email;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_profile);

        spinner  = findViewById(R.id.googleCategorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        save = findViewById(R.id.save1);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        try {
            email = user.getEmail();
        } catch (Exception e) {
            e.printStackTrace();
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nname = name.getText().toString();
                final String nphone = phone.getText().toString();
                final String category = spinner.getSelectedItem().toString();

                userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("fName",nname);
                user.put("email",email);
                user.put("phone",nphone);
                user.put("category",category);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
                startActivity(new Intent(getApplicationContext(),Dashboard.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        save.setEnabled(true);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        save.setEnabled(false);
    }
}