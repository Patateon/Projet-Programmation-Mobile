package com.main.projet_programmation_mobile.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.main.projet_programmation_mobile.R;

public class UserInfoActivity extends AppCompatActivity {

    private TextView textViewUsername;
    private TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("user_username", "N/A");
        String email = sharedPreferences.getString("user_email", "N/A");

        textViewUsername.setText("Username: " + username);
        textViewEmail.setText("Email: " + email);
    }
}
