package com.example.whatsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    CircleImageView circleImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.profilefragment,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        circleImageView = view.findViewById(R.id.circlerImageView);
        final EditText profileIsim = view.findViewById(R.id.profileIsim);
        final EditText profileDurum = view.findViewById(R.id.profileDurum);
        final LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        Button buttonSave= view.findViewById(R.id.buttonSave);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getUid());
        /*reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String downloadUri = dataSnapshot.child("photo").getValue().toString();
                Glide.with(view.getContext()).load(downloadUri).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

       /* circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*if (Build.VERSION.SDK_INT>=23){
                    getPermission();
                }else{
                    changePhoto();
                }*//*
            }
        });
*/
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("isim").getValue().toString();
                String statu = dataSnapshot.child("durum").getValue().toString();
                profileIsim.setText(name);
                profileDurum.setText(statu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isim = profileIsim.getText().toString();
                String durum = profileDurum.getText().toString();
                reference.child("isim").setValue(isim);
                reference.child("durum").setValue(durum).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Snackbar snackbar = Snackbar.make(linearLayout,"Basariyla Kaydedildi.",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });
            }
        });
    }
  public void getPermission(){
        if (ContextCompat.checkSelfPermission(circleImageView.getContext() ,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3);
        }else{
            changePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==3 &&grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED ){
            changePhoto();
        }
    }
    public void changePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,4);
    }


 @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==4 && resultCode== Activity.RESULT_OK){
            Uri imageUri = data.getData();
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getUid());
            UploadTask uploadTask=storageReference.putFile(imageUri);
            Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadURL =task.getResult();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
                    reference.child("photo").setValue(downloadURL.toString());
                    Glide.with(circleImageView.getContext()).load(downloadURL.toString()).into(circleImageView);
                }
            });
        }
    }
}
