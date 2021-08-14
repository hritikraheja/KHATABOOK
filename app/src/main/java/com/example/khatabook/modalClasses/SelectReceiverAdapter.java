package com.example.khatabook.modalClasses;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khatabook.MainActivity;
import com.example.khatabook.R;
import com.example.khatabook.ReceiverAndAmountActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SelectReceiverAdapter extends RecyclerView.Adapter<SelectReceiverAdapter.SelectReceiverHolder>{

    ArrayList<User> users;

    public SelectReceiverAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public SelectReceiverHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_for_select_receiver_card_view,parent,false);
        return new SelectReceiverHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectReceiverHolder holder, final int position) {
        holder.toUserNameTV.setText(users.get(position).getName());
        holder.toUserAccountNumberTV.setText(users.get(position).getAccountNumber());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog successDialog = new Dialog(v.getContext());
                successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                successDialog.setCancelable(false);
                successDialog.setContentView(R.layout.layout_for_transaction_successful_dialog);
                Button continueButtonInSuccessDialog = successDialog.findViewById(R.id.continueButton);
                continueButtonInSuccessDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                    }
                });

                final Dialog failureDialog = new Dialog(v.getContext());
                failureDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                failureDialog.setCancelable(false);
                failureDialog.setContentView(R.layout.layout_for_transaction_failed_dialog);
                Button continueButtonInFailureDialog = failureDialog.findViewById(R.id.continueButton);
                continueButtonInFailureDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        failureDialog.cancel();
                    }
                });

                Animation blinkAnimation = AnimationUtils.loadAnimation(v.getContext(),R.anim.blink);
                final Dialog sendMoneyDialog = new Dialog(v.getContext());
                sendMoneyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                sendMoneyDialog.setContentView(R.layout.layout_for_send_money_dialog);
                final TextView fromUserNameTV = sendMoneyDialog.findViewById(R.id.fromUserNameTV);
                final TextView fromUserAccountNumberTV = sendMoneyDialog.findViewById(R.id.fromUserAccountNumberTV);
                final TextView toUserNameTV = sendMoneyDialog.findViewById(R.id.toUserNameTV);
                final TextView toUserAccountNumberTV = sendMoneyDialog.findViewById(R.id.toUserAccountNumberTV);
                fromUserNameTV.setText(ReceiverAndAmountActivity.fromUserName);
                fromUserAccountNumberTV.setText(ReceiverAndAmountActivity.fromUserAccountNumber);
                toUserNameTV.setText(users.get(position).getName());
                toUserAccountNumberTV.setText(users.get(position).getAccountNumber());
                final double accountBalance = ReceiverAndAmountActivity.fromUserAccountBalance;
                final EditText amountEditText = sendMoneyDialog.findViewById(R.id.amountEditText);
                final EditText passwordEditText = sendMoneyDialog.findViewById(R.id.passwordEditText);
                final UsersDatabaseHandler usersDatabaseHandler = new UsersDatabaseHandler(v.getContext());
                final ProgressBar progressBar = sendMoneyDialog.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.INVISIBLE);
                Button sendMoneyButton = sendMoneyDialog.findViewById(R.id.sendMoneyButton);
                sendMoneyDialog.getWindow().getAttributes().windowAnimations = R.style.slide_down_dialog_animation;
                successDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                failureDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                sendMoneyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amountString = amountEditText.getText().toString();
                        String password = passwordEditText.getText().toString();
                        if(amountString.isEmpty() || password.isEmpty()){
                            Toast.makeText(v.getContext(), "Fill Amount And Password", Toast.LENGTH_SHORT).show();
                        } else if(Double.parseDouble(amountString) == 0) {
                            Toast.makeText(v.getContext(), "Amount Should Not Be Zero", Toast.LENGTH_SHORT).show();
                        } else if (usersDatabaseHandler.checkPassword(ReceiverAndAmountActivity.fromUserAccountNumber,password)){
                            double amount = Double.parseDouble(amountString);
                            if(amount <= accountBalance){
                                TransactionsDatabaseHandler tDatabase = new TransactionsDatabaseHandler(v.getContext());
                                usersDatabaseHandler.updateDatabase(ReceiverAndAmountActivity.fromUserAccountNumber,
                                        Double.parseDouble(amountString),
                                        users.get(position).getAccountNumber());
                                String dateAndTime = Calendar.getInstance().getTime().toString();
                                tDatabase.addNewTransaction(ReceiverAndAmountActivity.fromUserName,
                                        ReceiverAndAmountActivity.fromUserAccountNumber,
                                        amount,
                                        users.get(position).getName(),
                                        users.get(position).getAccountNumber(),
                                        dateAndTime);
                                sendMoneyButton.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.VISIBLE);
                                CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        InputMethodManager keyboard = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        keyboard.showSoftInput(v, 0);
                                        sendMoneyDialog.cancel();
                                        successDialog.show();
                                    }
                                };
                                countDownTimer.start();
                            } else {
                                sendMoneyButton.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.VISIBLE);
                                CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        failureDialog.show();
                                        amountEditText.setText("");
                                        passwordEditText.setText("");
                                        progressBar.setVisibility(View.INVISIBLE);
                                        sendMoneyButton.setVisibility(View.VISIBLE);
                                    }
                                };
                                countDownTimer.start();
                            }
                        } else {
                            Toast.makeText(v.getContext(), "INCORRECT PASSWORD", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                sendMoneyDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class SelectReceiverHolder extends RecyclerView.ViewHolder{

        TextView toUserNameTV;
        TextView toUserAccountNumberTV;
        ConstraintLayout card;

        public SelectReceiverHolder(@NonNull View itemView) {
            super(itemView);
            toUserNameTV = itemView.findViewById(R.id.toUserNameTV);
            toUserAccountNumberTV = itemView.findViewById(R.id.toUserAccountNumberTV);
            card = itemView.findViewById(R.id.card);
        }
    }
}
