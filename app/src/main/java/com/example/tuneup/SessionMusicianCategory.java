package com.example.tuneup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;


public class SessionMusicianCategory extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Userm> mUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sessionmusician_category,container,false);

        recyclerView = view.findViewById(R.id.recycler_view_sessionmusician);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

        readUsers();
        return view;
    }

    private void readUsers() {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
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

                                if (documentSnapshot.getString("category").equals("Session musician")){
                                    mUsers.add(user);
                                }
                            }
                        }
                    });

                }
                userAdapter = new UserAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}