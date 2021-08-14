package com.example.khatabook;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.khatabook.modalClasses.UsersAdaptor;
import com.example.khatabook.modalClasses.User;
import com.example.khatabook.modalClasses.UsersDatabaseHandler;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    Button addUserButton;
    Dialog addUserDialog;
    Button deleteUserButton;
    Dialog deleteUserDialog;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_users_fragment, container, false);
        addUserButton = view.findViewById(R.id.addUserButton);
        deleteUserButton = view.findViewById(R.id.deleteUserButton);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        UsersDatabaseHandler dbHandler = new UsersDatabaseHandler(view.getContext());
        ArrayList<User> users = dbHandler.viewDatabase();
        recyclerView.setAdapter(new UsersAdaptor(users));


        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserDialog = new Dialog(view.getContext());
                addUserDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                addUserDialog.setContentView(R.layout.layout_for_add_user_dialog);
                addUserDialog.setCancelable(false);
                addUserDialog.getWindow().getAttributes().windowAnimations = R.style.slide_down_dialog_animation;
                addUserDialog.show();
                ImageView closeButton = addUserDialog.findViewById(R.id.closeButton);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addUserDialog.cancel();
                        Toast.makeText(addUserButton.getContext(), "SESSION ABORTED : No User Added ", Toast.LENGTH_SHORT).show();
                    }
                });
                Button submitButton = addUserDialog.findViewById(R.id.submitButton);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText nameEditText = addUserDialog.findViewById(R.id.nameEditText);
                        EditText mobileNumberEditText = addUserDialog.findViewById(R.id.mobileNumberEditText);
                        EditText emailEditText = addUserDialog.findViewById(R.id.emailEditText);
                        EditText balanceEditText = addUserDialog.findViewById(R.id.balanceEditText);
                        EditText passwordEditText = addUserDialog.findViewById(R.id.passwordEditText);
                        String name = nameEditText.getText().toString();
                        String mobileNumber = mobileNumberEditText.getText().toString();
                        String email = emailEditText.getText().toString();
                        String balance = balanceEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        UsersDatabaseHandler databaseHandler = new UsersDatabaseHandler(addUserDialog.getContext());
                        if (name.isEmpty() || mobileNumber.isEmpty() || email.isEmpty() || balance.isEmpty() || password.isEmpty()){
                            Toast.makeText(addUserDialog.getContext(), "ALL DETAILS ARE REQUIRED", Toast.LENGTH_LONG).show();
                        } else if (mobileNumber.length() != 10 || containsAlpha(mobileNumber)){
                            Toast.makeText(addUserDialog.getContext(), "Fill A Valid 10 Digit Mobile Number", Toast.LENGTH_LONG).show();
                        } else if (password.length() < 6) {
                            Toast.makeText(addUserDialog.getContext(), "Enter A Strong Password More Than 6 Digits", Toast.LENGTH_LONG).show();
                        } else {
                            try{
                                double accountBalance = Double.parseDouble(balance);
                                databaseHandler.addNewUser(name,email,mobileNumber, accountBalance, password);
                                Toast.makeText(addUserDialog.getContext(), "User Added Successfully", Toast.LENGTH_LONG).show();
                                addUserDialog.cancel();
                                ArrayList<User> users = databaseHandler.viewDatabase();
                                recyclerView.setAdapter(new UsersAdaptor(users));
                            } catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(addUserDialog.getContext(), "Enter A Valid Initial Account Balance", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserDialog = new Dialog(view.getContext());
                deleteUserDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                deleteUserDialog.setContentView(R.layout.layout_for_delete_user_dialog);
                deleteUserDialog.setCancelable(false);
                deleteUserDialog.getWindow().getAttributes().windowAnimations = R.style.slide_down_dialog_animation;
                deleteUserDialog.show();
                final EditText nameEditText = deleteUserDialog.findViewById(R.id.nameEditText);
                final EditText accountNumberEditText = deleteUserDialog.findViewById(R.id.accountNumberEditText);
                final EditText passwordEditText = deleteUserDialog.findViewById(R.id.passwordEditText);
                final CheckBox checkBox = deleteUserDialog.findViewById(R.id.checkBox);
                final ImageView eyeButton = deleteUserDialog.findViewById(R.id.eye);
                eyeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                        eyeButton.setImageDrawable(null);
                    }
                });
                ImageView closeButton = deleteUserDialog.findViewById(R.id.closeButton);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteUserDialog.cancel();
                        Toast.makeText(deleteUserButton.getContext(), "SESSION ABORTED : No User Deleted ", Toast.LENGTH_SHORT).show();
                    }
                });
                Button deleteButton = deleteUserDialog.findViewById(R.id.deleteUserButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = nameEditText.getText().toString();
                        String accountNumber = accountNumberEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        if(!checkBox.isChecked()){
                            Toast.makeText(v.getContext(), "Tick The CheckBox", Toast.LENGTH_LONG).show();
                        } else if (false){
                            Toast.makeText(v.getContext(), "Fill All The Details", Toast.LENGTH_LONG).show();
                        } else {
                            UsersDatabaseHandler dbHandler = new UsersDatabaseHandler(v.getContext());
                            int isDeleted = dbHandler.deleteUser(name,accountNumber,password);
                            if(isDeleted == 1){
                                ArrayList<User> users = dbHandler.viewDatabase();
                                recyclerView.setAdapter(new UsersAdaptor(users));
                                Toast.makeText(v.getContext(), "User Deleted Successfully", Toast.LENGTH_LONG).show();
                                deleteUserDialog.cancel();
                            } else {
                                Toast.makeText(v.getContext(), "Records Not Matched", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
        return view;
    }

    private static boolean containsAlpha(String str) {
        try {
            Double.parseDouble(str);
            return false;
        } catch(NumberFormatException e){
            return true;
        }
    }
}