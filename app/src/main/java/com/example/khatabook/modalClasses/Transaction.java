package com.example.khatabook.modalClasses;

public class Transaction implements Comparable<Transaction>{
    private String senderName;
    private String senderAccountNumber;
    private double amount;
    private String receiverName;
    private String receiverAccountNumber;
    private String dateAndTime;

    public Transaction(String senderName, String senderAccountNumber, double amount, String receiverName, String receiverAccountNumber, String dateAndTime) {
        this.senderName = senderName;
        this.senderAccountNumber = senderAccountNumber;
        this.amount = amount;
        this.receiverName = receiverName;
        this.receiverAccountNumber = receiverAccountNumber;
        this.dateAndTime = dateAndTime;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    @Override
    public int compareTo(Transaction o) {
        return o.dateAndTime.compareTo(this.dateAndTime);
    }
}
