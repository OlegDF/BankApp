package com.example.bankapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

    private Card sender, receiver;
    private double amount;

    public Transaction(Card sender, Card receiver, double sum) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = sum;
    }

    public boolean conduct(Context context) {
        if(sender.getNumber().equals(receiver.getNumber())) {
            return false;
        }
        if(sender.getBalance() >= amount) {
            sender.changeBalance(-amount);
            receiver.changeBalance(amount);
            writeToHistory(context);
            return true;
        } else {
            return false;
        }
    }

    private void writeToHistory(Context context) {
        SQLiteDatabase db = DatabaseAccess.openDefaultBase(context);
        ContentValues values = new ContentValues();
        values.put("Sender", sender.getNumber());
        values.put("Receiver", receiver.getNumber());
        values.put("Amount", amount);
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        values.put("Date", dateFormat.format(currentDate));
        db.insert("TransactionHistory", null, values);
        db.close();
    }

}
