package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.appcompat.widget.LinearLayoutCompat.VERTICAL;

public class ChatActivity extends AppCompatActivity {
    private EditText chatEditText;
    private Button chatButton;
    private RecyclerView chatRecyclerView;
    ChatAdapter chatAdapter;
    ArrayList<ChatObject> chatObjectArrayList= new ArrayList<>();
    Intent intent;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatEditText=findViewById(R.id.chatEditText);
        chatButton=findViewById(R.id.chatButton);
        chatRecyclerView=findViewById(R.id.chatRecyclerView);
        chatAdapter= new ChatAdapter(chatObjectArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setAdapter(chatAdapter);
        intent=getIntent();
        uid = intent.getStringExtra("uid");

        loadList();

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatEditText.getText().toString();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference senderReference = FirebaseDatabase.getInstance().getReference().child("chat").child(mAuth.getUid()).child(uid);
                DatabaseReference receiverReference = FirebaseDatabase.getInstance().getReference().child("chat").child(uid).child(mAuth.getUid());
                HashMap<String,Object> map = new HashMap<>();
                map.put("senderUid",mAuth.getUid());
                map.put("text",message);
                String key = senderReference.push().getKey();
                senderReference.child(key).updateChildren(map);
                receiverReference.child(key).updateChildren(map);

            }
        });
    }
    public void loadList(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("chat").child(mAuth.getUid()).child(uid);
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String message=dataSnapshot.child("text").getValue().toString();
                String senderUid = dataSnapshot.child("senderUid").getValue().toString();
                ChatObject chatObject = new ChatObject(senderUid,message);
                chatObjectArrayList.add(chatObject);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
