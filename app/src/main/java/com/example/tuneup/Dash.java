package com.example.tuneup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.tuneup.Adapter.UserAdapter;
import com.example.tuneup.Model.Userm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;


public class Dash extends Fragment {

    public Dash() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Userm> mUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        readUsers();
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

    private void readUsers() {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    String userId = snapshot1.getKey();
                    final Userm user = snapshot1.getValue(Userm.class);

                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(userId);
                    documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                            if(documentSnapshot.exists()){
                                user.setUsername(documentSnapshot.getString("fName"));
                                user.setId(documentSnapshot.getId());


                            }
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}