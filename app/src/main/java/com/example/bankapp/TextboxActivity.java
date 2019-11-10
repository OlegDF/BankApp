package com.example.bankapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TextboxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textbox);
        TextView textbox = findViewById(R.id.textbox_id);
        Button continueButton = findViewById(R.id.continue_button_id);
        final String loginStr, text;
        final boolean adminRights;
        Bundle extras = getIntent().getExtras();
        if(!extras.isEmpty()) {
            loginStr = extras.getString("LOGIN");
            adminRights = extras.getBoolean("ADMIN");
            text = extras.getString("TEXT");
        } else {
            loginStr = " ";
            text = " ";
            adminRights = false;
        }
        textbox.setText(text);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TextboxActivity.this, MainMenuActivity.class);
                i.putExtra("LOGIN", loginStr);
                i.putExtra("ADMIN", adminRights);
                startActivity(i);
            }
        });
    }
}
