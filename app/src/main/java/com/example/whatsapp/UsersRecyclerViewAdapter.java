package com.example.whatsapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.RecyclerViewHolder>{
    ArrayList<UserObject> list;

    public UsersRecyclerViewAdapter(ArrayList<UserObject> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.useritem,parent,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        holder.isim.setText(list.get(position).getIsim());
        holder.durum.setText(list.get(position).getDurum());
        Glide.with(holder.circleImageView.getContext()).load(list.get(position).getPhotoURL()).into(holder.circleImageView);
        holder.isim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.circleImageView.getContext(),ChatActivity.class);
                intent.putExtra("uid",list.get(position).getUID());
                holder.circleImageView.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView isim,durum;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.userItemCircleImageView);
            isim=itemView.findViewById(R.id.userItemName);
            durum=itemView.findViewById(R.id.userItemDurum);
        }
    }
}
