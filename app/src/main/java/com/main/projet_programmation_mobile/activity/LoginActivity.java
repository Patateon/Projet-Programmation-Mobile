package com.main.projet_programmation_mobile.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.main.projet_programmation_mobile.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextMail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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

                mAuth.signInWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    db.collection("users").document(user.getUid())
                                            .get()
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    DocumentSnapshot document = task1.getResult();
                                                    if (document.exists()) {
                                                        String fetchedMail = document.getString("email");
                                                        String fetchedUsername = document.getString("username");
                                                        boolean isPremium = document.getBoolean("isPremium");

                                                        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putBoolean("is_logged_in", true);
                                                        editor.putString("user_email", fetchedMail);
                                                        editor.putString("user_username", fetchedUsername);
                                                        editor.putBoolean("is_premium", isPremium);
                                                        editor.apply();

                                                        Toast.makeText(LoginActivity.this, "Connexion réussie pour l'utilisateur : " + fetchedMail, Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(LoginActivity.this, "Aucun compte avec l'adresse : " + mail, Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "Erreur de récupération des données utilisateur.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Combinaison mail / mot de passe incorrecte", Toast.LENGTH_SHORT).show();
                            }
                        });
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
