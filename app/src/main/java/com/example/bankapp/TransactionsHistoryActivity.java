package com.example.bankapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TransactionsHistoryActivity extends AppCompatActivity {

    private String loginStr;
    private boolean adminRights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        Bundle extras = getIntent().getExtras();
        if(!extras.isEmpty()) {
            loginStr = extras.getString("LOGIN");
            adminRights = extras.getBoolean("ADMIN");
        }
        makeList();
    }

    private void makeList() {
        StringBuilder list = new StringBuilder();
        SQLiteDatabase db = DatabaseAccess.openDefaultBase(this);
        final String query = "SELECT DISTINCT TransactionHistory.Sender, TransactionHistory.Receiver, TransactionHistory.Amount, TransactionHistory.Date, Card.Account FROM TransactionHistory INNER JOIN Card ON TransactionHistory.Sender = Card.Number OR TransactionHistory.Receiver = Card.Number WHERE Account = ? ORDER BY TransactionHistory.Date DESC";
        String[] args = {loginStr};
        Cursor historyQuery = db.rawQuery(query, args);
        if(!historyQuery.isAfterLast()) {
            historyQuery.moveToNext();
        }
        while(!historyQuery.isAfterLast()) {
            list.append(historyQuery.getString(2)).append(" ").append(getString(R.string.currency_name)).append("\n").append(historyQuery.getString(0)).append(" -> ").append(historyQuery.getString(1)).append("\n").append(getString(R.string.at_time)).append(" ").append(historyQuery.getString(3)).append("\n\n");
            historyQuery.moveToNext();
        }
        historyQuery.close();
        TextView historyText = findViewById(R.id.transaction_history_id);
        historyText.setText(list);
    }

}
