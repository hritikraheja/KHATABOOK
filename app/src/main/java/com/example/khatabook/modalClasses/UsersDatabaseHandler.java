package com.example.khatabook.modalClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class UsersDatabaseHandler extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "khatabookDatabase";
    private final static String TABLE_NAME = "userDetails";

    public UsersDatabaseHandler(Context context) {
        super(context,DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "Create TABLE " + TABLE_NAME + "(NAME TEXT, 'ACCOUNT NUMBER' TEXT PRIMARY KEY," +
                "EMAIL TEXT, 'PHONE NUMBER' TEXT , 'ACCOUNT BALANCE' REAL, PASSWORD TEXT)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean checkPassword(String accountNumber, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From " + TABLE_NAME + " Where `Account Number` = '" + accountNumber + "' And `Password` = '" + password + "'", null);
        return cursor.getCount()==1;
    }

    private String generateAccountNumber(){
        ArrayList<User> users = viewDatabase();
        int numberOfUsers = users.size();
        if (users.size() == 0){
            return "14698001";
        }
        int accountNumberOfLastUser = Integer.parseInt(users.get(numberOfUsers - 1).getAccountNumber());
        return String.valueOf(++accountNumberOfLastUser);
    }

    public long addNewUser(String name, String email, String phoneNumber, double accountBalance, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("ACCOUNT NUMBER", generateAccountNumber());
        values.put("EMAIL", email);
        values.put("PHONE NUMBER", phoneNumber);
        values.put("ACCOUNT BALANCE", accountBalance);
        values.put("PASSWORD", password);
//        long isInserted = db.insert(TABLE_NAME, null, values);
        String sqlQuery = String.format("Insert Into %s(Name, `Account Number`, Email, `Phone Number`, `Account Balance`, Password)" +
                " Values('%s', '%s', '%s', '%s', '%s', '%s')", TABLE_NAME, name,generateAccountNumber(),email, phoneNumber,accountBalance,password);
        db.execSQL(sqlQuery);
        db.close();
        return 1;
    }

    public int deleteUser(String name,String accountNumber, String password){
        SQLiteDatabase wDatabase = this.getWritableDatabase();
        SQLiteDatabase rDatabase = this.getReadableDatabase();
        String sqlQuery1 = String.format("Select * From %s Where (Name='%s' and `Account Number`= '%s') and Password='%s'", TABLE_NAME, name, accountNumber, password);
        Cursor cursor = rDatabase.rawQuery(sqlQuery1, null);
        if(cursor.getCount() == 1){
            String sqlQuery = String.format("Delete From %s Where Name='%s' and `Account Number`='%s' and Password='%s'", TABLE_NAME, name,accountNumber,password);
            wDatabase.execSQL(sqlQuery);
            rDatabase.close();
            wDatabase.close();
            return 1;
        } else {
            return 0;
        }
    }

    public ArrayList<User> viewDatabase(){
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                users.add(new User(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public double getAccountBalance(String name, String password) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT `ACCOUNT BALANCE` FROM " + TABLE_NAME + " WHERE NAME='" + name + "' and PASSWORD='"+password+"'", null);
        if (cursor.getCount() == 0) {
            database.close();
            return -1d;
        } else {
            cursor.moveToFirst();
            double accountBalance = cursor.getDouble(0);
            database.close();
            return accountBalance;
        }
    }

    private void updateAmount(String accountNumber, double newBalance){
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlQuery = String.format("Update %s Set `Account Balance` = %.2f where `Account Number` = '%s'", TABLE_NAME, newBalance, accountNumber);
        db.execSQL(sqlQuery);
    }

    public void addTenDefaultUsers(){
        addNewUser("Sumit Chaudhary", "chaudarysahab@gmail.com", "9839349922", 32540.0, "sumitchaudhary");
        addNewUser("Owais Grover", "groverowais23@gmail.com", "8057153421", 19200.0, "owaisgrover");
        addNewUser("Mohan Singh", "mohansingh@gmail.com", "9634234353", 23444.0, "mohansingh");
        addNewUser("Rohit Kumar", "rohit232@yahoo.com", "7306608243", 45012.0, "rohitkumar");
        addNewUser("Jai Garg", "gargjai@hotmail.com", "6398923923", 42678.0, "jaigarg");
        addNewUser("Harshit Varshney", "varshneyharshit@gmail.com", "7482383201", 56500.0, "harshitvarshney");
        addNewUser("Ishan Sharma", "devil24@yahoo.com", "6982243454", 18450.0, "ishansharma");
        addNewUser("Piyush Bhatnagar", "pbhatnagar22@gmail.com", "8057399922", 37080.0, "piyushbhatnagar");
        addNewUser("Karan Baghel", "karan.baghel@gmail.com", "8967563489", 51021.0, "karanbaghel");
        addNewUser("Vansh Rajput", "rajputvansh@hotmail.com", "6723454587", 39360.0, "vanshrajput");
    }

    public void updateDatabase(String fromAccountNumber, double amount, String toAccountNumber){
        SQLiteDatabase rDatabase = this.getReadableDatabase();
        Cursor fromCursor = rDatabase.rawQuery("Select `Account Balance` From " + TABLE_NAME + " Where `Account Number` = " + fromAccountNumber, null);
        Cursor toCursor = rDatabase.rawQuery("Select `Account Balance` From " + TABLE_NAME + " Where `Account Number` = " + toAccountNumber, null);
        fromCursor.moveToFirst();
        toCursor.moveToFirst();
        double fromUserAccountBalance = fromCursor.getDouble(0);
        double toUserAccountBalance = toCursor.getDouble(0);
        updateAmount(fromAccountNumber, (double) (fromUserAccountBalance - amount));
        updateAmount(toAccountNumber, (double) (toUserAccountBalance + amount));
    }

}