package com.example.khatabook;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khatabook.modalClasses.SelectReceiverAdapter;
import com.example.khatabook.modalClasses.User;
import com.example.khatabook.modalClasses.UsersDatabaseHandler;

import java.util.ArrayList;

public class ReceiverAndAmountActivity extends AppCompatActivity {

    ImageView backButton;
    RecyclerView recyclerView;
    TextView fromUserNameTV;
    TextView fromUserAccountNumberTV;
    public static String fromUserName;
    public static String fromUserAccountNumber;
    public static double fromUserAccountBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_and_amount_actitvity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#24526A")));
        actionBar.setTitle("MyPassbook");
        actionBar.setIcon(R.drawable.ic_khatabook_icon_splashscreen);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        User fromUser = (User) intent.getSerializableExtra("fromUser");
        fromUserName = fromUser.getName().toUpperCase();
        fromUserAccountNumber = fromUser.getAccountNumber();
        fromUserAccountBalance = fromUser.getAccountBalance();
        recyclerView = findViewById(R.id.recyclerView);
        fromUserNameTV = findViewById(R.id.fromUserNameTV);
        fromUserAccountNumberTV = findViewById(R.id.fromUserAccountNumberTV);
        backButton = findViewById(R.id.backButton);
        fromUserNameTV.setText(fromUserName);
        fromUserAccountNumberTV.setText(fromUserAccountNumber);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        UsersDatabaseHandler db = new UsersDatabaseHandler(getApplicationContext());
        ArrayList<User> allUsers = db.viewDatabase();
        allUsers.remove(fromUser);
        recyclerView.setAdapter(new SelectReceiverAdapter(allUsers));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}