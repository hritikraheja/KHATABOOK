package com.example.khatabook.modalClasses;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khatabook.R;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>{

    ArrayList<Transaction> transactions;
    String accountNumber;

    public TransactionsAdapter(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        this.accountNumber = "";
    }

    public TransactionsAdapter(ArrayList<Transaction> transactions, String accountNumber) {
        this.transactions = transactions;
        this.accountNumber = accountNumber;
    }

    @NonNull
    @Override
    public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_for_transaction_history_card_view, parent, false);
        return new TransactionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsViewHolder holder, int position) {
        String senderDetails = String.format("%s\n%s", transactions.get(position).getSenderName(),transactions.get(position).getSenderAccountNumber());
        String receiverDetails = String.format("%s\n%s", transactions.get(position).getReceiverName(),transactions.get(position).getReceiverAccountNumber());
        holder.fromUserDetails.setText(senderDetails);
        holder.amountTextView.setText("₹ " + transactions.get(position).getAmount());
        holder.toUserDetails.setText(receiverDetails);
        holder.timeTextView.setText(transactions.get(position).getDateAndTime());
        if (accountNumber != "") {
            if(holder.fromUserDetails.getText().toString().contains(accountNumber)){
                holder.fromUserDetails.setTextColor(Color.MAGENTA);
            } else if(holder.toUserDetails.getText().toString().contains(accountNumber)){
                holder.toUserDetails.setTextColor(Color.MAGENTA);
            }
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class TransactionsViewHolder extends RecyclerView.ViewHolder{

        TextView fromUserDetails;
        TextView toUserDetails;
        TextView amountTextView;
        TextView timeTextView;


        public TransactionsViewHolder(@NonNull View itemView) {
            super(itemView);
            fromUserDetails = itemView.findViewById(R.id.fromUserDetails);
            toUserDetails = itemView.findViewById(R.id.toUserDetails);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }

}
