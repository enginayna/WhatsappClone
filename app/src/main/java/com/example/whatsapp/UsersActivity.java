package com.example.whatsapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class UsersActivity extends AppCompatActivity {

RecyclerView usersRecyclerView;
UsersRecyclerViewAdapter adapter;
ArrayList<UserObject> list;
Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        usersRecyclerView=findViewById(R.id.usersRecyclerView);
        list=new ArrayList<>();
        adapter= new UsersRecyclerViewAdapter(list);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(UsersActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);//LinearlayoutManager.VERTICAL olacakti
        usersRecyclerView.setLayoutManager(linearLayoutManager);
        usersRecyclerView.setAdapter(adapter);
        if (Build.VERSION.SDK_INT>=23){
            getPermission();
        }else{
            loadlist();
        }
    }
    public void getPermission(){
        if (ContextCompat.checkSelfPermission(UsersActivity.this,Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            //checkSelfPermission() izin verilip verilmedigini kontrol eder. context ve permission bilgisi alir. permission granted izin verildi anlami tasir
            ActivityCompat.requestPermissions(UsersActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
            //requstPermissions izin verilmediyse uygulama icin izin ister. Ve sonucu request code ile onRequestRPErmissionResult metoduna iletir.
        }
        else{
            loadlist();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //istenilen izin isteginin sonucunu request code ile alir. gelen sonuca gore isleme cevirebiliriz.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1&& grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            loadlist();
        }
    }
    public void loadlist(){
        Cursor cursor= getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                ,null,null,null,null,null);
        //getContentResolver icerik saglayicisindan verilere ulasmak icin kulladigimiz method
        //Cursor guery ile telefon numaralarina ulassir
        while (cursor.moveToNext()){
            //imlecin tum numaralar uzerinde dolasmasini saglar.
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //rehberdeki telefon numaralarini aliriz
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //Rehberdeki isimleri aliriz
            final String mNumber = number.replaceAll(" ", "");
            //regex ile degismesini istedigimiz ifadeyi belirtir replacement ile de hangi hali almassi gerektigini belirtiriz.
            //bu sekilde telefon numarasi icinde bosluk varsa eger onlari kaldirmis oluruz.
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
            //database referansi aliyoruz.Daha onceden olusturudugumuz users childini gosteren bir referans elde etmis oluyoruz
            reference.addChildEventListener(new ChildEventListener() {
                //Database child degisikliklerinde bu metodu kullaniyoruz
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //Child a ekleme yapmak istedigimiz de bu methodu kullaniyoruz
                    if(dataSnapshot.child("phone").getValue().toString().equals(mNumber)){
                        UserObject userObject = new UserObject(dataSnapshot.child("isim").getValue().toString()
                                ,dataSnapshot.child("photo").getValue().toString(),dataSnapshot.child("durum").getValue().toString(),dataSnapshot.getKey());
                        //Simdi rehberdeki telefon numaralarina ulastik sonra bu numaralari databasede bulunan numaralar ile karsilastirdik
                        //eger databasede kayitli numaralar ile rehberdeki numaralardan eslesen olursa bunlari recyclerview ile listeliyoruz.
                        list.add(userObject);
                        adapter.notifyDataSetChanged();
                    }
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
}
