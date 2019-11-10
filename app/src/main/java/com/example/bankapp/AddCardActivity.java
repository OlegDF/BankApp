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

public class AddCardActivity extends AppCompatActivity {

    private String loginStr;
    private boolean adminRights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        Bundle extras = getIntent().getExtras();
        if(!extras.isEmpty()) {
            loginStr = extras.getString("LOGIN");
            adminRights = extras.getBoolean("ADMIN");
        }
        final EditText newCardNumber = findViewById(R.id.new_card_text_id);
        final EditText newCardAccount = findViewById(R.id.new_card_account_text_id);
        if(!adminRights) {
            newCardAccount.setVisibility(View.INVISIBLE);
        }
        Button newCardButton = findViewById(R.id.add_card_button_id);
        newCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNumberStr = newCardNumber.getText().toString();
                if(adminRights) {
                    String newLoginStr = newCardAccount.getText().toString();
                    checkCardAdmin(newLoginStr, newNumberStr);
                } else {
                    checkCard(newNumberStr);
                }
            }
        });
    }

    private void checkCard(String newNumberStr) {
        SQLiteDatabase db = DatabaseAccess.openDefaultBase(this);
        final String query = "SELECT Number FROM Card WHERE Number = ? AND Account IS NULL";
        String[] args = {newNumberStr};
        Cursor cardCheck = db.rawQuery(query, args);
        if(!cardCheck.isAfterLast()) {
            ContentValues cv = new ContentValues();
            cv.put("Account", loginStr);
            db.update("Card", cv, "Number = ?", args);
            cardCheck.close();
            db.close();
            Intent i = new Intent(AddCardActivity.this, TextboxActivity.class);
            i.putExtra("LOGIN", loginStr);
            i.putExtra("ADMIN", adminRights);
            i.putExtra("TEXT", getString(R.string.card_add_successful_text));
            startActivity(i);
        }
    }

    private void checkCardAdmin(String newLoginStr, String newNumberStr) {
        SQLiteDatabase db = DatabaseAccess.openDefaultBase(this);
        final String query = "SELECT Login FROM Account WHERE Login = ?";
        String[] args = {newLoginStr};
        Cursor accountCheck = db.rawQuery(query, args);
        if(accountCheck.isAfterLast() && !newLoginStr.isEmpty()) {
            accountCheck.close();
            return;
        }
        final String query2 = "SELECT Number FROM Card WHERE Number = ? AND Account IS NULL";
        args[0] = newNumberStr;
        Cursor cardCheck = db.rawQuery(query2, args);
        if(!cardCheck.isAfterLast()) {
            ContentValues cv = new ContentValues();
            cv.put("Account", newLoginStr);
            db.update("Card", cv, "Number = ?", args);
            cardCheck.close();
            accountCheck.close();
            db.close();
            Intent i = new Intent(AddCardActivity.this, TextboxActivity.class);
            i.putExtra("LOGIN", loginStr);
            i.putExtra("ADMIN", adminRights);
            i.putExtra("TEXT", getString(R.string.card_add_successful_text));
            startActivity(i);
        }
    }

}
