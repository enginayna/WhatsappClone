package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
private EditText etPhoneNumber;
private EditText etPhoneCode;
private Button buttonLogin;
FirebaseAuth mAuth;
PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
String verificationId;//gelen kodu bu stringe bagliyoruz
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etPhoneNumber=findViewById(R.id.etPhoneNumber);
        etPhoneCode=findViewById(R.id.etPhoneCode);
        buttonLogin=findViewById(R.id.buttonLogin);
        mAuth= FirebaseAuth.getInstance();
        mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            //telefon numarasini alip onaylanip kod gonderme aramasini bu sekilde yapiyoruz
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //onVerificationCompleted metodu otomatik sms i yakalayyip onaylama oldugu zaman calisir.
                mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Giris islemin basarili olup olmadigini bu method ile deneyimliyoruz
                        if (task.isSuccessful()){
                            //Girisin basarili oldugu kosulda calisacak
                            final FirebaseUser user=mAuth.getCurrentUser();
                            //Giris basarili oldugu icin user i getCurrentUser methotu ile alacagiz. Cunku her user icin farkli Uid kodlari olusuyor buna ihtiyacimiz var
                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                            //REaltime Database ile users govdesi altinda tum kullanicilari kaydedebilecegimiz bir child olusturuyoruz.
                            //Olusan child a her istedigimizde ulasabilelim diye user.getUid methodu ile aldigimiz benzersiz user kodunu atiyoruz.
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        //Anlık görüntü boş olmayan bir değer içeriyorsa true değerini döndürür. Doluysa daha onceden giris yapmistir
                                        // bu yuzden Whatsapp sayfasina yondermemiz gerekir
                                        Intent intent = new Intent(LoginActivity.this,WhatsApp.class);
                                        startActivity(intent);
                                    }else{
                                        //ilk defa giris yapan biri icin standart giis bilgilerini uygular database e kaydederiz
                                        HashMap<String,Object> map = new HashMap<>();
                                        map.put("isim","");
                                        map.put("durum","Selam. Buralarda yeniyim...");
                                        map.put("photo","");
                                        map.put("phone",user.getPhoneNumber());
                                        reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Intent intent = new Intent(LoginActivity.this,WhatsApp.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                });
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e("Verification ERROR", e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId=s;
                buttonLogin.setText("Onayla");
            }
        };
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificationId!=null){
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,etPhoneCode.getText().toString());
                    mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                final FirebaseUser user=mAuth.getCurrentUser();
                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            Intent intent = new Intent(LoginActivity.this,WhatsApp.class);
                                            startActivity(intent);
                                        }else{
                                            HashMap<String,Object> map = new HashMap<>();
                                            map.put("isim","");
                                            map.put("durum","Selam. Buralarda yeniyim...");
                                            map.put("photo","");
                                            map.put("phone",user.getPhoneNumber());
                                            reference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Intent intent = new Intent(LoginActivity.this,WhatsApp.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                    });
                }else{
                    phoneNumberVerifying();
                }
            }
        });

    }
    public void phoneNumberVerifying(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(etPhoneNumber.getText().toString(),60, TimeUnit.SECONDS,LoginActivity.this,mCallBack);
    }
}
