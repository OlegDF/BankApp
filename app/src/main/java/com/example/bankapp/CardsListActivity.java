package com.example.bankapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CardsListActivity extends AppCompatActivity {

    private String loginStr;
    private boolean adminRights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_list);
        Bundle extras = getIntent().getExtras();
        if(!extras.isEmpty()) {
            loginStr = extras.getString("LOGIN");
            adminRights = extras.getBoolean("ADMIN");
        }
        makeList();
    }

    private void makeList() {
        Account account = new Account(loginStr, this);
        StringBuilder list = new StringBuilder();
        for(Card card: account.getCards()) {
            list.append(getString(R.string.card_number)).append(" ").append(card.getNumber()).append("\n").append(getString(R.string.balance)).append(" ").append(card.getBalance().toString()).append(" ").append(getString(R.string.currency_name)).append("\n\n");
        }
        TextView cardsListText = findViewById(R.id.cards_list_id);
        cardsListText.setText(list);
        account.closeDB();
    }

}
