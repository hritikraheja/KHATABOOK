package com.example.khatabook.modalClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class TransactionsDatabaseHandler extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "khatabookTransactionsDatabase";
    private final static String TABLE_NAME = "transactionDetails";

    public TransactionsDatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "Create TABLE " + TABLE_NAME + "('Sender Name' TEXT, 'Sender Account Number' TEXT, 'AMOUNT' REAL, 'Receiver Name' TEXT, 'Receiver Account Number' TEXT,'DATE AND TIME' TEXT)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Transaction> viewDatabase(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                transactions.add(new Transaction(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        Collections.sort(transactions);
        return transactions;
    }

    public ArrayList<Transaction> viewDatabase(String accountNumber){
        ArrayList<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where `Sender Account Number` = '" + accountNumber + "' Or `Receiver Account Number` = '" + accountNumber + "'", null);
        if (cursor.moveToFirst()) {
            do {
                transactions.add(new Transaction(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        Collections.reverse(transactions);
        return transactions;
    }

    public void addNewTransaction(String senderName, String senderAccountNumber, double amount, String receiverName, String receiverAccountNumber, String dateAndTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlQuery = String.format("Insert Into %s(`Sender Name`, `Sender Account Number`, AMOUNT, `Receiver Name`, `Receiver Account Number`,`Date And Time`)" +
                " Values('%s', '%s', '%s', '%s', '%s','%s')", TABLE_NAME, senderName, senderAccountNumber, amount, receiverName, receiverAccountNumber, dateAndTime);
        db.execSQL(sqlQuery);
        db.close();
    }
}
