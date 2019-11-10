package com.example.bankapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateNewCardActivity extends AppCompatActivity {

    private String loginStr;
    private boolean adminRights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_card);
        Bundle extras = getIntent().getExtras();
        if(!extras.isEmpty()) {
            loginStr = extras.getString("LOGIN");
            adminRights = extras.getBoolean("ADMIN");
        }
        final EditText newCardNumber = findViewById(R.id.new_card_text_id);
        Button newCardButton = findViewById(R.id.add_card_button_id);
        newCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNumberStr = newCardNumber.getText().toString();
                checkCard(newNumberStr);
            }
        });
    }

    private void checkCard(String newNumberStr) {
        if(newNumberStr.length() != Card.NUMBER_LENGTH) {
            return;
        }
        SQLiteDatabase db = DatabaseAccess.openDefaultBase(this);
        final String query = "SELECT Number FROM Card WHERE Number = ?";
        String[] args = {newNumberStr};
        Cursor loginCheck = db.rawQuery(query, args);
        if(loginCheck.isAfterLast()) {
            ContentValues values = new ContentValues();
            values.put("Number", newNumberStr);
            values.put("Balance", 0.0);
            values.put("Account", (String)null);
            db.insert("Card", null, values);
            loginCheck.close();
            db.close();
            Intent i = new Intent(CreateNewCardActivity.this, TextboxActivity.class);
            i.putExtra("LOGIN", loginStr);
            i.putExtra("ADMIN", adminRights);
            i.putExtra("TEXT", getString(R.string.card_created_successful_text));
            startActivity(i);
        }
    }

}
