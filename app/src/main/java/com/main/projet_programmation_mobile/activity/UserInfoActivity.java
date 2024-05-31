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

import com.google.firebase.firestore.FirebaseFirestore;
import com.main.projet_programmation_mobile.R;

public class UserInfoActivity extends AppCompatActivity {

    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewPremium;
    private Button getPremiumButton;
    private Button logoutButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPremium = findViewById(R.id.textViewPremium);
        getPremiumButton = findViewById(R.id.getPremium_button);
        logoutButton = findViewById(R.id.logout_button);

        db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("user_username", "N/A");
        String email = sharedPreferences.getString("user_email", "N/A");
        String is_premium = sharedPreferences.getString("is_premium", "Non");

        textViewUsername.setText("Username : " + username);
        textViewEmail.setText("Email : " + email);
        textViewPremium.setText("Compte premium : " + is_premium);

        // Show or hide the Get Premium button based on premium status
        if ("Non".equals(is_premium)) {
            getPremiumButton.setVisibility(View.VISIBLE);
            getPremiumButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPremiumPopup(username);
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
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(UserInfoActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showPremiumPopup(String username) {
        new AlertDialog.Builder(this)
                .setTitle("Devenir Premium")
                .setMessage("Voulez-vous devenir un membre premium")
                .setPositiveButton("Oui", (dialog, which) -> {
                    updatePremiumStatus(username);
                    textViewPremium.setText("Compte premium : Oui");
                    getPremiumButton.setVisibility(View.GONE);
                    Toast.makeText(UserInfoActivity.this, "Vous etes maintenant un membre Premium", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void updatePremiumStatus(String username) {
        // Assuming the username is a unique identifier
        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String userId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("users").document(userId)
                                .update("is_premium", "Oui")
                                .addOnSuccessListener(aVoid -> {
                                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("is_premium", "Oui");
                                    editor.apply();
                                })
                                .addOnFailureListener(e -> Toast.makeText(UserInfoActivity.this, "Failed to update premium status", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(UserInfoActivity.this, "Failed to find user", Toast.LENGTH_SHORT).show());
    }
}
