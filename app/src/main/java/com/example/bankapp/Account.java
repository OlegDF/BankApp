package com.example.bankapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private SQLiteDatabase db;

    private List<Card> cards;
    private String login, phoneNumber;
    private boolean adminRights;

    public Account(String login, Context context) {
        this.login = login;
        db = DatabaseAccess.openDefaultBase(context);
        String query = "SELECT PhoneNumber, AdminRights FROM Account WHERE Login = ?";
        String[] args = {login};
        Cursor accountDataGetter = db.rawQuery(query, args);
        accountDataGetter.moveToNext();
        phoneNumber = accountDataGetter.getString(0);
        adminRights = accountDataGetter.getInt(1) != 0;
        query = "SELECT Number, Balance, Account FROM Card WHERE Account = ?";
        accountDataGetter.close();
        args[0] = login;
        Cursor cardsCheck = db.rawQuery(query, args);
        cards = new ArrayList<>();
        cardsCheck.moveToNext();
        while (!cardsCheck.isAfterLast()) {
            cards.add(new Card(cardsCheck.getString(0), db, true));
            cardsCheck.moveToNext();
        }
        cardsCheck.close();
    }

    public void closeDB() {
        db.close();
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getLogin() {
        return login;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getCardCount() {
        return cards.size();
    }

    public boolean getAdminRights() {
        return adminRights;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
