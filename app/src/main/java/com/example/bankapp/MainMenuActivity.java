package com.example.bankapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    private String loginStr;
    private boolean adminRights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Bundle extras = getIntent().getExtras();
        if(!extras.isEmpty()) {
            loginStr = extras.getString("LOGIN");
            adminRights = extras.getBoolean("ADMIN");
        }
        final Button listCardsButton = findViewById(R.id.list_cards_button_id);
        listCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, CardsListActivity.class);
                i.putExtra("LOGIN", loginStr);
                i.putExtra("ADMIN", adminRights);
                startActivity(i);
            }
        });
        final Button showHistoryButton = findViewById(R.id.transactions_button_id);
        showHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, TransactionsHistoryActivity.class);
                i.putExtra("LOGIN", loginStr);
                i.putExtra("ADMIN", adminRights);
                startActivity(i);
            }
        });
        final Button makeTransactionButton = findViewById(R.id.new_transaction_button_id);
        makeTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, NewTransactionActivity.class);
                i.putExtra("LOGIN", loginStr);
                i.putExtra("ADMIN", adminRights);
                startActivity(i);
            }
        });
        final Button newCardButton = findViewById(R.id.add_card_button_id);
        newCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, AddCardActivity.class);
                i.putExtra("LOGIN", loginStr);
                i.putExtra("ADMIN", adminRights);
                startActivity(i);
            }
        });
        final Button createCardButton = findViewById(R.id.create_card_button_id);
        if(!adminRights) {
            createCardButton.setVisibility(View.INVISIBLE);
        }
        createCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, CreateNewCardActivity.class);
                i.putExtra("LOGIN", loginStr);
                i.putExtra("ADMIN", adminRights);
                startActivity(i);
            }
        });
        final Button logoutButton = findViewById(R.id.logout_button_id);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

}
