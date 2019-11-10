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

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText loginText = findViewById(R.id.login_text_id);
        final EditText phoneText = findViewById(R.id.phone_text_id);
        final EditText passwordText = findViewById(R.id.password_text_id);
        final EditText passwordRepeatText = findViewById(R.id.password_repeat_text_id);
        final Button registerButton = findViewById(R.id.register_button_id);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginStr = loginText.getText().toString();
                String phoneStr = phoneText.getText().toString();
                String passwordStr = passwordText.getText().toString();
                String passwordRepeatStr = passwordRepeatText.getText().toString();
                if(checkAccountData(loginStr, passwordStr, passwordRepeatStr, phoneStr)) {
                    addAccount(loginStr, passwordStr, phoneStr);
                    Intent i = new Intent(RegisterActivity.this, MainMenuActivity.class);
                    i.putExtra("LOGIN", loginStr);
                    i.putExtra("ADMIN", false);
                    startActivity(i);
                }
            }
        });
    }

    private boolean checkAccountData(String loginStr, String passwordStr, String passwordRepeatStr, String phoneStr) {
        SQLiteDatabase db = DatabaseAccess.openDefaultBase(this);
        final String query = "SELECT Login FROM Account WHERE Login = ?";
        String[] args = {loginStr};
        Cursor loginCheck = db.rawQuery(query, args);
        return loginCheck.isAfterLast() && loginStr.length() > 0 && phoneStr.length() > 0 && passwordStr.length() > 0 && passwordStr.equals(passwordRepeatStr);
    }

    private void addAccount(String loginStr, String passwordStr, String phoneStr) {
        SQLiteDatabase db = DatabaseAccess.openDefaultBase(this);
        ContentValues values = new ContentValues();
        values.put("Login", loginStr);
        values.put("Password", passwordStr);
        values.put("PhoneNumber", phoneStr);
        values.put("AdminRights", 0);
        db.insert("Account", null, values);
    }

}
