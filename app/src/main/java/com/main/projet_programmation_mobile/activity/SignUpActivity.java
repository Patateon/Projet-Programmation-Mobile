package com.main.projet_programmation_mobile.activity;

import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.main.projet_programmation_mobile.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextMail;
    private EditText editTextPassword;
    private EditText editTextUsername;
    private Button buttonSignUp;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextMail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUsername = findViewById(R.id.editTextUsername);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = editTextMail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty() || username.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Veuillez saisir un identifiant, un mot de passe et un pseudonyme", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(SignUpActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    // Stocker les informations supplémentaires dans Firestore
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("email", mail);
                                    userMap.put("username", username);
                                    userMap.put("isPremium", false); // Par défaut, l'utilisateur n'est pas premium

                                    db.collection("users").document(user.getUid())
                                            .set(userMap)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(SignUpActivity.this, "Inscription réussie pour l'utilisateur : " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignUpActivity.this, MainMenuActivity.class);
                                                startActivity(intent);
                                                finish();
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Échec de l'inscription. Veuillez réessayer.", Toast.LENGTH_SHORT).show());
                                }
                            } else {
                                Toast.makeText(SignUpActivity.this, "Échec de l'inscription. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                            }
                        });
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
