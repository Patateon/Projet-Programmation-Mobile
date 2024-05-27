package com.main.projet_programmation_mobile.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.main.projet_programmation_mobile.R;
import com.main.projet_programmation_mobile.databases.DatabaseUserManager;
import com.main.projet_programmation_mobile.databases.DatabaseUserHelper;

import java.sql.SQLDataException;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextMail;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextMail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = editTextMail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Veuillez saisir un identifiant et un mot de passe", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseUserManager dbManager = new DatabaseUserManager(LoginActivity.this);
                try {
                    dbManager.open();

                    Cursor cursor = dbManager.fetch(mail);

                    if (cursor != null && cursor.moveToFirst()) {
                        @SuppressLint("Range")
                        String fetchedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseUserHelper.password));
                        @SuppressLint("Range")
                        String fetchedMail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseUserHelper.mail));
                        @SuppressLint("Range")
                        String fetchedUsername = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseUserHelper.username));

                        if (password.equals(fetchedPassword)) {
                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("is_logged_in", true);
                            editor.putString("user_email", fetchedMail);
                            editor.putString("user_username", fetchedUsername);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Connexion réussie pour l'utilisateur : " + fetchedMail, Toast.LENGTH_SHORT).show();
                            cursor.close();
                            dbManager.close();

                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Combinaison mail / mot de passe incorrecte", Toast.LENGTH_SHORT).show();
                            cursor.close();
                            dbManager.close();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Aucun compte avec l'adresse : " + mail, Toast.LENGTH_SHORT).show();
                        if (cursor != null) {
                            cursor.close();
                        }
                        dbManager.close();
                    }
                } catch (SQLDataException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Gestion du clic sur le bouton d'inscription
        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers la vue d'inscription
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // bouton home
        ImageButton homeButton = (ImageButton) findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        });
    }
}