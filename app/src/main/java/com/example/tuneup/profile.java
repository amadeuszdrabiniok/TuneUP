package com.example.tuneup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;



public class profile extends Fragment {

    String name, email, phone;
    TextView result;

    EditText nameInput;
    EditText emailInput;
    EditText phoneInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);


    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameInput = (EditText) view.findViewById(R.id.nameInput);
        emailInput = (EditText) view.findViewById(R.id.emailInput);
        phoneInput = (EditText) view.findViewById(R.id.phoneInput);
        result = (TextView) view.findViewById(R.id.result);





        Button button4 = view.findViewById(R.id.save);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString();
                email = emailInput.getText().toString();
                phone = phoneInput.getText().toString();
                result.setText(name + " " + email+ " " + phone);
            }

        });


        final NavController navController = Navigation.findNavController(view);

        ImageButton button3 = view.findViewById(R.id.backbutton);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_profile_to_dash);
            }


        });


    }
}