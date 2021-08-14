package com.example.khatabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import  androidx.appcompat.app.AppCompatActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.khatabook.modalClasses.User;
import com.example.khatabook.modalClasses.UsersDatabaseHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.frameLayout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#24526A")));
        actionBar.setTitle("MyPassbook");
        bnv = findViewById(R.id.bnv);
        bnv.setItemIconTintList(null);
        UsersDatabaseHandler db = new UsersDatabaseHandler(getApplicationContext());
        ArrayList<User> users = db.viewDatabase();
        if (users.size() == 0){
            db.addTenDefaultUsers();
        }
        loadFragment(new UsersFragment());
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId){
                    case R.id.usersItem:
                        loadFragment(new UsersFragment());
                        break;
                    case R.id.transactItem:
                        loadFragment(new TransactFragment());
                        break;
                    case R.id.historyItem:
                        loadFragment(new HistoryFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(frameLayout.getId(), fragment);
        ft.commit();
        //#242C5A
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}