package com.main.projet_programmation_mobile.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.main.projet_programmation_mobile.R;

public class UserInfoActivity extends AppCompatActivity {

    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewPremium;
    private Button getPremiumButton;
    private Button logoutButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPremium = findViewById(R.id.textViewPremium);
        getPremiumButton = findViewById(R.id.getPremium_button);
        logoutButton = findViewById(R.id.logout_button);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String fetchedMail = document.getString("email");
                                String fetchedUsername = document.getString("username");
                                boolean isPremium = document.getBoolean("isPremium");

                                textViewUsername.setText("Username : " + fetchedUsername);
                                textViewEmail.setText("Email : " + fetchedMail);
                                textViewPremium.setText("Compte premium : " + (isPremium ? "Oui" : "Non"));

                                updateUI(isPremium);

                                /*if (!isPremium) {
                                    getPremiumButton.setVisibility(View.VISIBLE);*/
                                    getPremiumButton.setOnClickListener(v -> showPremiumPopup(fetchedMail, isPremium));
                                //}
                            }
                        } else {
                            Toast.makeText(UserInfoActivity.this, "Erreur de récupération des données utilisateur.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Home button
        ImageButton homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });

        // Logout button
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(UserInfoActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showPremiumPopup(String mail, boolean isPremium) {
        String title = isPremium ? "Résilier Premium" : "Passer Premium";
        String message = isPremium ? "Voulez-vous résilier votre abonnement Premium ?" : "Voulez-vous devenir membre Premium ?";

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Oui", (dialog, which) -> {
                    updatePremiumStatus(mail, !isPremium);
                    Intent intent = new Intent(UserInfoActivity.this, UserInfoActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void updatePremiumStatus(String mail, boolean isPremium) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                db.collection("users").document(user.getUid())
                                        .update("isPremium", isPremium)
                                        .addOnSuccessListener(aVoid -> {
                                            textViewPremium.setText("Compte premium : " + (isPremium ? "Oui" : "Non"));
                                            getPremiumButton.setText(isPremium ? "Résilier" : "Passer Premium");

                                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean("is_premium", isPremium);
                                            editor.apply();

                                            Toast.makeText(UserInfoActivity.this, isPremium ? "Vous êtes maintenant un membre premium" : "Votre abonnement premium a été résilié", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(UserInfoActivity.this, "Failed to update premium status", Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            Toast.makeText(UserInfoActivity.this, "Erreur de récupération des données utilisateur.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateUI(boolean isPremium) {
        if (isPremium) {
            getPremiumButton.setText("Résilier");
        } else {
            getPremiumButton.setText("Passer Premium");
        }
    }

}
