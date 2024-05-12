package com.main.projet_programmation_mobile;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.main.projet_programmation_mobile.databases.DatabaseUserManager;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextMail;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation des vues
        editTextMail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération des données saisies par l'utilisateur
                String mail = editTextMail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                DatabaseUserManager dbManager = null;

                try {
                    dbManager.open();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                Cursor cursor = dbManager.fetch(mail);

                String fetchedUsername = "";
                String fetchedMail = "";
                String fetchedPassword = "";

                boolean mailExist = false;

                if (cursor != null && cursor.moveToFirst()) {
                    fetchedUsername = cursor.getString(1);
                    fetchedMail = cursor.getString(2);
                    fetchedPassword = cursor.getString(3);
                    mailExist = true;

                    cursor.close();
                } else {
                    Toast.makeText(LoginActivity.this, "Aucun compte avec l'adresse : " + mail, Toast.LENGTH_SHORT).show();
                }

                // Vérification des données de connexion

                if (!mail.isEmpty() && !password.isEmpty() && mailExist) {
                    if(mail == fetchedMail && password == fetchedPassword){
                        //Connexion
                    }else{
                        Toast.makeText(LoginActivity.this, "Combinaison mail / mot de passe incorrecte : ", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(LoginActivity.this, "Connexion réussie pour l'utilisateur : " + mail, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Veuillez saisir un identifiant et un mot de passe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //A completer une fois la vue SignUpActivity
                //Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                //startActivity(intent);
            }
        });

    }
}

