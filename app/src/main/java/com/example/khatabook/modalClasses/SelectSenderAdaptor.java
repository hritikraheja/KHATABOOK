package com.example.khatabook.modalClasses;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khatabook.R;
import com.example.khatabook.ReceiverAndAmountActivity;

import java.util.ArrayList;

public class SelectSenderAdaptor extends RecyclerView.Adapter<SelectSenderAdaptor.SelectSenderHolder> {

    ArrayList<User> allUsers;

    public SelectSenderAdaptor(ArrayList<User> allUsers) {
        this.allUsers = allUsers;
    }

    @NonNull
    @Override
    public SelectSenderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.select_sender_card_view, parent, false);
        return new SelectSenderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectSenderHolder holder, final int position) {
        holder.nameTextView.setText(allUsers.get(position).getName());
        holder.accountNumberTextView.setText(allUsers.get(position).getAccountNumber());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User from = allUsers.get(position);
                Intent intent = new Intent(v.getContext(), ReceiverAndAmountActivity.class);
                intent.putExtra("fromUser",from);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    class SelectSenderHolder extends RecyclerView.ViewHolder{
        ConstraintLayout card;
        TextView nameTextView;
        TextView accountNumberTextView;


        public SelectSenderHolder(@NonNull View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            accountNumberTextView = view.findViewById(R.id.accountNumberTextView);
            card = view.findViewById(R.id.card);
        }
    }
}
