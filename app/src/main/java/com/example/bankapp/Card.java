package com.example.bankapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Card {

    public static final int NUMBER_LENGTH = 16;

    private String number;
    private SQLiteDatabase db;

    private boolean isValid;

    public Card(String number, SQLiteDatabase db) {
        this.number = number;
        this.db = db;
        isValid = getBalance() != null;
    }

    public Card(String number, SQLiteDatabase db, boolean assumeValidity) {
        this.number = number;
        this.db = db;
        if(assumeValidity) {
            isValid = true;
        } else {
            isValid = getBalance() != null;
        }
    }

    public boolean transferMoney(Card otherCard, double amount, Context context) {
        Transaction transaction = new Transaction(this, otherCard, amount);
        return transaction.conduct(context);
    }

    public Double getBalance() {
        final String query = "SELECT Number, Balance FROM Card WHERE Number = ?";
        String[] args = {number};
        Cursor balanceCheck = db.rawQuery(query, args);
        if(!balanceCheck.isAfterLast()) {
            balanceCheck.moveToNext();
            Double res = balanceCheck.getDouble(1);
            balanceCheck.close();
            return res;
        } else {
            return null;
        }
    }

    public void changeBalance(double change) {
        Double balance = getBalance();
        if(balance != null) {
            balance += change;
            ContentValues cv = new ContentValues();
            cv.put("Balance", balance);
            final String[] args = {number};
            db.update("Card", cv, "Number = ?", args);
        }
    }

    public String getNumber() {
        return number;
    }

    public boolean isValid() {
        return isValid;
    }

}
