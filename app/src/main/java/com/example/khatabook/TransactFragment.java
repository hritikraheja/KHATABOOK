package com.example.khatabook;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khatabook.modalClasses.SelectSenderAdaptor;
import com.example.khatabook.modalClasses.User;
import com.example.khatabook.modalClasses.UsersDatabaseHandler;

import java.util.ArrayList;

public class TransactFragment extends Fragment {

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_transact_fragment,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        UsersDatabaseHandler db = new UsersDatabaseHandler(view.getContext());
        ArrayList<User> users = db.viewDatabase();
        recyclerView.setAdapter(new SelectSenderAdaptor(users));
        return view;
    }
}