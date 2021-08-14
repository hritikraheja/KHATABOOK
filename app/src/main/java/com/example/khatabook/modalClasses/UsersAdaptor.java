package com.example.khatabook.modalClasses;

import android.app.Dialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khatabook.R;

import java.util.ArrayList;

public class UsersAdaptor extends RecyclerView.Adapter<UsersAdaptor.Holder> {

    private ArrayList<User> allUsers;

    public UsersAdaptor(ArrayList<User> allUsers) {
        this.allUsers = allUsers;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_details_card_view, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.nameTextView.setText(allUsers.get(position).getName());
        holder.accountNumberTextView.setText(allUsers.get(position).getAccountNumber());
        holder.emailTextView.setText(allUsers.get(position).getEmail());
        holder.mobileNumberTextView.setText(allUsers.get(position).getPhoneNumber());
        holder.viewAccountBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog viewBalanceDialog = new Dialog(v.getContext());
                viewBalanceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                viewBalanceDialog.setContentView(R.layout.layout_for_view_account_balance_dialog);
                viewBalanceDialog.setCancelable(true);
                viewBalanceDialog.getWindow().getAttributes().windowAnimations = R.style.slide_down_dialog_animation;
                viewBalanceDialog.show();
                TextView nameTextView = viewBalanceDialog.findViewById(R.id.nameTextView);
                TextView accountNumberTextView = viewBalanceDialog.findViewById(R.id.accountNumberTextView);
                TextView emailTextView = viewBalanceDialog.findViewById(R.id.emailTextView);
                TextView mobileNumberTextView = viewBalanceDialog.findViewById(R.id.mobileNumberTextView);
                final String name = allUsers.get(position).getName();
                final String accountNumber = allUsers.get(position).getAccountNumber();
                String email = allUsers.get(position).getEmail();
                String mobileNumber = allUsers.get(position).getPhoneNumber();
                nameTextView.setText("Name : " + name);
                accountNumberTextView.setText("Account Number : " + accountNumber);
                emailTextView.setText("Email : " + email);
                mobileNumberTextView.setText("Mobile Number : " + mobileNumber);
                final EditText passwordEditText = viewBalanceDialog.findViewById(R.id.passwordEditText);
                final Button checkButton = viewBalanceDialog.findViewById(R.id.checkButton);
                checkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password = passwordEditText.getText().toString();
                        UsersDatabaseHandler db = new UsersDatabaseHandler(v.getContext());
                        Double accountBalance = db.getAccountBalance(name, password);
                        if (accountBalance == -1d){
                            Toast.makeText(v.getContext(), "INCORRECT PASSWORD", Toast.LENGTH_SHORT).show();
                        } else {
                            passwordEditText.setText("₹ " + accountBalance);
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                            passwordEditText.setFocusable(false);
                            checkButton.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        TextView accountNumberTextView;
        TextView emailTextView;
        TextView mobileNumberTextView;
        Button viewAccountBalanceButton;

        public Holder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            accountNumberTextView = itemView.findViewById(R.id.accountNumberTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            mobileNumberTextView = itemView.findViewById(R.id.mobileNumberTextView);
            viewAccountBalanceButton = itemView.findViewById(R.id.viewBalanceButton);
        }
    }
}
