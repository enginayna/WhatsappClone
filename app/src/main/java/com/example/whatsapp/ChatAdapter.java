package com.example.whatsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.RecyclerViewHolder> {
    ArrayList<ChatObject> chatObjectArrayList;

    public ChatAdapter(ArrayList<ChatObject> chatObjectArrayList) {
        this.chatObjectArrayList = chatObjectArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==1){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.senderitem,parent,false);
            RecyclerViewHolder viewHolder = new RecyclerViewHolder(v);
            return viewHolder;
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiveritem,parent,false);
            RecyclerViewHolder viewHolder= new RecyclerViewHolder(v);
            return viewHolder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.tvMessage.setText(chatObjectArrayList.get(position).getTextChat());
    }

    @Override
    public int getItemCount() {
        return chatObjectArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage=itemView.findViewById(R.id.tvMessage);

        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (chatObjectArrayList.get(position).equals(mAuth.getUid())){
            return 1 ;
        }else{
            return 2;
        }
    }
}
