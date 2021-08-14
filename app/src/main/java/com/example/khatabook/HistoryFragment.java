package com.example.khatabook;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khatabook.modalClasses.Transaction;
import com.example.khatabook.modalClasses.TransactionsAdapter;
import com.example.khatabook.modalClasses.TransactionsDatabaseHandler;
import com.example.khatabook.modalClasses.User;
import com.example.khatabook.modalClasses.UsersDatabaseHandler;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    Spinner spinner;
    RecyclerView recyclerView;
    TextView noTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_history_fragment, container, false);
        spinner = view.findViewById(R.id.spinner);
        noTransaction = view.findViewById(R.id.noTransactions);
        final TransactionsDatabaseHandler databaseHandler = new TransactionsDatabaseHandler(view.getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new TransactionsAdapter(databaseHandler.viewDatabase()));
        UsersDatabaseHandler db = new UsersDatabaseHandler(view.getContext());
        ArrayList<User> users = db.viewDatabase();
        int numberOfUsers = users.size();
        String[] userNamesAndAccountNumbers = new String[numberOfUsers+1];
        userNamesAndAccountNumbers[0] = "All Transactions";
        int index = 1;
        for(User user : users){
            userNamesAndAccountNumbers[index] = String.format("%s (%s)",user.getName(), user.getAccountNumber());
            index++;
        }
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.support_simple_spinner_dropdown_item,userNamesAndAccountNumbers);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        ArrayList<Transaction> transactions = databaseHandler.viewDatabase();
                        if(transactions.size() == 0){
                            recyclerView.setVisibility(View.INVISIBLE);
                            noTransaction.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            noTransaction.setVisibility(View.INVISIBLE);
                        }
                        recyclerView.setAdapter(new TransactionsAdapter(transactions));
                        break;
                    default:
                        String receiverNameAndAccountNumber = spinnerAdapter.getItem(position);
                        int p = 0;
                        for(int i = 0; i < receiverNameAndAccountNumber.length(); i++){
                            if(receiverNameAndAccountNumber.charAt(i) == '('){
                                p = i;
                            }
                        }
                        String receiverAccountNumber = receiverNameAndAccountNumber.substring(p+1, receiverNameAndAccountNumber.length()-1);
                        transactions = databaseHandler.viewDatabase(receiverAccountNumber);
                        if(transactions.size() == 0){
                            recyclerView.setVisibility(View.INVISIBLE);
                            noTransaction.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            noTransaction.setVisibility(View.INVISIBLE);
                        }
                        recyclerView.setAdapter(new TransactionsAdapter(transactions,receiverAccountNumber));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}