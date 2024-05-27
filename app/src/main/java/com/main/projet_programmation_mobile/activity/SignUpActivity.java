package com.main.projet_programmation_mobile.activity;

import android.content.Intent;
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

import java.sql.SQLDataException;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextMail;
    private EditText editTextPassword;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        // Initialisation des vues
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextMail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // Gestion du clic sur le bouton d'inscription
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération des données saisies par l'utilisateur
                String username = editTextUsername.getText().toString().trim();
                String mail = editTextMail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseUserManager dbManager = new DatabaseUserManager(SignUpActivity.this);
                try {
                    dbManager.open();
                } catch (SQLDataException e) {
                    throw new RuntimeException(e);
                }

                Cursor cursor = dbManager.fetch(mail);

                if (cursor != null && cursor.moveToFirst()) {
                    Toast.makeText(SignUpActivity.this, "L'adresse "+ mail + " est déjà utilisée", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    dbManager.close();
                    return;
                }

                dbManager.insert(username, mail, password);
                dbManager.close();

                Toast.makeText(SignUpActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();

                // Redirection vers la page de connexion après l'inscription réussie
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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
