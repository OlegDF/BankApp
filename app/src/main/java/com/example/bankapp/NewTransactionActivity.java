package com.example.bankapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewTransactionActivity extends AppCompatActivity {

    private String loginStr;
    private boolean adminRights;

    private Spinner senderNumberInput;
    private TextView balanceText;
    private EditText receiverNumberInput, sumInput;
    private Button transactionButton;
    private Account account;
    int senderNumberId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);
        Bundle extras = getIntent().getExtras();
        if(!extras.isEmpty()) {
            loginStr = extras.getString("LOGIN");
            adminRights = extras.getBoolean("ADMIN");
        }
        initialiseButtons();
    }

    private void initialiseButtons() {
        balanceText = findViewById(R.id.balance_text_id);
        senderNumberInput = findViewById(R.id.sender_text_id);
        receiverNumberInput = findViewById(R.id.receiver_text_id);
        sumInput = findViewById(R.id.transaction_sum_text_id);
        transactionButton = findViewById(R.id.new_transaction_button_id);
        account = new Account(loginStr, this);
        List<String> numbersList = new ArrayList<>();
        for(Card card: account.getCards()) {
            numbersList.add(card.getNumber());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, numbersList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        senderNumberInput.setAdapter(adapter);
        senderNumberInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                senderNumberId = position;
                balanceText.setText(account.getCards().get(senderNumberId).getBalance() + " " + getString(R.string.currency_name));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                senderNumberId = -1;
            }
        });
        transactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeTransaction();
            }
        });
    }

    private void makeTransaction() {
        if(senderNumberId < 0) {
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(sumInput.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Card senderCard = account.getCards().get(senderNumberId);
        Card receiverCard = new Card(receiverNumberInput.getText().toString(), account.getDb());
        if(!receiverCard.isValid()) {
            return;
        }
        if(senderCard.transferMoney(receiverCard, amount, this)) {
            completeTransaction();
        }
    }

    private void completeTransaction() {
        account.closeDB();
        Intent i = new Intent(NewTransactionActivity.this, TextboxActivity.class);
        i.putExtra("LOGIN", loginStr);
        i.putExtra("ADMIN", adminRights);
        i.putExtra("TEXT", getString(R.string.transaction_successful_text));
        startActivity(i);
    }

}
