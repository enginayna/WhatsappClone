package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
private Button buttonControl;
FirebaseAuth mAuth;
FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonControl=findViewById(R.id.buttonControl);
        mAuth=FirebaseAuth.getInstance();
        //FirebaseAuth sinifinin ornegini alir istedigimiz yere ulasiriz.
        user = mAuth.getCurrentUser();
        //Gecerli kullanici varsa doner yoksa null degerini dondurur
        buttonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user!=null){
                    //user null degilse daha onceden giris yapilmis demektir direk wp sayfasina yonderirir
                   // mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this,WhatsApp.class);
                    startActivity(intent);
                }else{
                    //Ilk defa giris yapanlar Login activity sayfasina yonlendirilir
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}
