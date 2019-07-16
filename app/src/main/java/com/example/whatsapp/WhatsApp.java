package com.example.whatsapp;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.core.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import android.widget.TextView;

public class WhatsApp extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app);
        viewPager=findViewById(R.id.container);
        tabLayout=findViewById(R.id.tabs);
        setup(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(R.id.tabItem2);
    }
    public void setup(ViewPager viewPager){
        PagerAdapter pagerAdapter= new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new ProfileFragment(),"Profil");
        pagerAdapter.addFragment(new MessagesFragment(),"Mesajlar");
        viewPager.setAdapter(pagerAdapter);
    }

}
