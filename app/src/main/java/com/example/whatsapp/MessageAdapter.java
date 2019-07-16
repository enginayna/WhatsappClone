package com.example.whatsapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.RecyclerViewHolder> {
    ArrayList<MessageObject> messageObjectArrayList;

    public MessageAdapter(ArrayList<MessageObject> messageObjectArrayList) {
        this.messageObjectArrayList = messageObjectArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messageitem,parent,false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        Glide.with(holder.circleImageView.getContext()).load(messageObjectArrayList.get(position).getPhoto()).into(holder.circleImageView);
        holder.messageName.setText(messageObjectArrayList.get(position).getIsim());
        //islem
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("chat").child(mAuth.getUid()).child(messageObjectArrayList.get(position).getUid());
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String lastMessage = dataSnapshot.child("text").getValue().toString();
                holder.messageLastMessage.setText(lastMessage);
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
        holder.messageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.circleImageView.getContext(),ChatActivity.class);
                intent.putExtra("uid",messageObjectArrayList.get(position).getUid());
                holder.circleImageView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageObjectArrayList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView messageName, messageLastMessage;
        CircleImageView circleImageView;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.circlerImageView);
            messageName=itemView.findViewById(R.id.messageName);
            messageLastMessage=itemView.findViewById(R.id.messageLastMessage);
        }
    }
}
