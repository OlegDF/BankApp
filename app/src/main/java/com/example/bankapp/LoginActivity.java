package com.example.bankapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkDatabase();
        final Button loginButton = findViewById(R.id.login_button_id);
        final Button registerButton = findViewById(R.id.register_button_id);
        final EditText loginText = findViewById(R.id.login_text_id);
        final EditText passwordText = findViewById(R.id.password_text_id);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MainMenuActivity.class);
                String loginStr = loginText.getText().toString();
                String passwordStr = passwordText.getText().toString();
                AccountType type = checkLoginValidity(loginStr, passwordStr);
                if(type != AccountType.WRONG) {
                    i.putExtra("LOGIN", loginStr);
                    i.putExtra("ADMIN", type == AccountType.ADMIN);
                    startActivity(i);
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void checkDatabase() {
        SQLiteDatabase checkDB;
        try {
            checkDB = DatabaseAccess.openDefaultBase(this);
            checkDB.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
            DatabaseAccess.setDefaultDatabase(this);
        } catch (Exception e) {
            e.printStackTrace();
            DatabaseAccess.setDefaultDatabase(this);
        }
    }

    private AccountType checkLoginValidity(String login, String password) {
        SQLiteDatabase db = DatabaseAccess.openDefaultBase(this);
        final String query = "SELECT Login, Password, AdminRights FROM Account WHERE Login = ? AND Password = ?";
        String[] args = {login, password};
        Cursor loginCheck = db.rawQuery(query, args);
        AccountType res;
        if(loginCheck.isAfterLast()) {
            res = AccountType.WRONG;
        } else {
            loginCheck.moveToNext();
            if (loginCheck.getInt(2) != 0) {
                res = AccountType.ADMIN;
            } else {
                res = AccountType.REGULAR;
            }
        }
        loginCheck.close();
        db.close();
        return res;
    }

}
