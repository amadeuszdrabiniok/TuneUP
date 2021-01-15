package com.example.tuneup;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.tuneup.Model.Userm;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class profile extends Fragment {

    private static final int IMAGE_REQUEST=1;
    private static final int AUDIO_REQUEST=2;
    private Uri imageUri, tuneUri;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    public static final String TAG = "TAG";
    CircleImageView profile_image;
    FirebaseFirestore fStore;
    TextView result;
    Button save, google;
    ImageButton selectFile1, selectFile2, selectFile3;
    EditText mDesc;
    TextView fullName,email,phone;
    FirebaseStorage storage;
    FirebaseDatabase dataBase;
    StorageReference storageReference;
    String userId;
    FirebaseAuth fAuth;
    FirebaseUser user, fuser;
    DatabaseReference reference;
    String mUri;
    String category;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_image=view.findViewById(R.id.profile_image);
        fuser=FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userm mser = snapshot.getValue(Userm.class);
                if (mser != null) {
                    if(mser.getImageURL().equals("imageURL")){
                        profile_image.setImageResource(R.drawable.bez_nazwy_2);
                    }else
                        Glide.with(getContext()).load(mser.getImageURL()).into(profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDesc = view.findViewById(R.id.Desc);
        phone = view.findViewById(R.id.phoneInput);
        fullName = view.findViewById(R.id.nameInput);
        email = view.findViewById(R.id.emailInput);
        profile_image=view.findViewById(R.id.profile_image);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();

        user = fAuth.getCurrentUser();
        fuser=FirebaseAuth.getInstance().getCurrentUser();

        dataBase = FirebaseDatabase.getInstance();
        save = view.findViewById(R.id.save);

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    phone.setText(documentSnapshot.getString("phone"));
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));
                    mDesc.setText(documentSnapshot.getString("Desc"));
                    category=documentSnapshot.getString("category");
                }
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

        profile_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openImage();
            }
        });

        selectFile1 = view.findViewById(R.id.imageButton2);
        selectFile2 = view.findViewById(R.id.imageButton3);
        selectFile3 = view.findViewById(R.id.imageButton4);


        selectFile1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectTune();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Desc = mDesc.getText().toString();
                final String memail = email.getText().toString().trim();
                final String mfullName = fullName.getText().toString();
                final String mphone    = phone.getText().toString();
                DocumentReference documentReference = fStore.collection("users").document(userId);
                Map<String,Object> user = new HashMap<>();
                user.put("fName",mfullName);
                user.put("email",memail);
                user.put("phone",mphone);
                user.put("Desc",Desc);
                user.put("category", category);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: user Profile up to date "+ userId);
                    }
                });

                if (tuneUri != null) {
                    uploadTune(tuneUri);
                }
            }

        });
    }

    private void uploadTune(Uri tuneUri) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();


        final String fileName = System.currentTimeMillis()+"";
        storageReference.child("Tunes").child(fileName).putFile(tuneUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                DatabaseReference databaseReference= dataBase.getReference();
                reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getContext(), "File successfully uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "File not successfully uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "File not successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                int currProgress = (int)(100 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                progressDialog.setProgress(currProgress);
            }
        });
    }

    private void selectTune() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, AUDIO_REQUEST);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == AUDIO_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            selectTune();
//        } else {
//            Toast.makeText(profile.this.getContext(), "Please provide permission to read files.", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd =new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"_"+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        mUri = downloadUri.toString();


                        reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        reference.updateChildren(map);



                    }else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

        }else {
            Toast.makeText(getContext(), "No Image selected", Toast.LENGTH_SHORT).show();

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null){
            imageUri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in Progress", Toast.LENGTH_SHORT).show();

            }else{
                uploadImage();
            }
        } else if (requestCode == AUDIO_REQUEST && resultCode == RESULT_OK && data != null) {
            tuneUri = data.getData();
        }
    }

}