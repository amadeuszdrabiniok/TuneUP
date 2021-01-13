package com.example.tuneup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


public class Dash extends Fragment {



    public Dash() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        ImageButton button = view.findViewById(R.id.noti);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dash_to_notification);
            }


        });

        ImageButton button2 = view.findViewById(R.id.settings);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dash_to_settings);
            }


        });

        Button buttonBand = view.findViewById(R.id.bandCategoryBtt);
        buttonBand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dash_to_bandCategory);
            }


        });
        Button buttonBandMember = view.findViewById(R.id.bandmemberCategoryBtt);
        buttonBandMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dash_to_bandMemberCategory);
            }


        });
        Button buttonSessionMusician = view.findViewById(R.id.sessionmusicianCategoryBtt);
        buttonSessionMusician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dash_to_sessionMusicianCategory);
            }


        });
        Button buttonStudio = view.findViewById(R.id.studioCategoryBtt);
        buttonStudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dash_to_studioCategory);
            }


        });

        Button buttonTeacher = view.findViewById(R.id.teacherCategoryBtn);
        buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dash_to_teacherCategory);
            }


        });

        Button buttonSongwriter = view.findViewById(R.id.songwriterCategoryBtt);
        buttonSongwriter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dash_to_songwriterCategory);
            }


        });

        Button buttonProducer = view.findViewById(R.id.producerCategoryBtt);
        buttonProducer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dash_to_producerCategory);
            }


        });

        Button button4 = view.findViewById(R.id.ProfileBtt);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_dash_to_profile);
            }


        });

    }
}