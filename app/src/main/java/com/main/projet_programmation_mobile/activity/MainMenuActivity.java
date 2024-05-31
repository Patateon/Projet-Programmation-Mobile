package com.main.projet_programmation_mobile.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.projet_programmation_mobile.R;

public class MainMenuActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private LinearLayout linearLayout;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mainmenu_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        linearLayout = findViewById(R.id.linearLayout_canvas);

        // Load a button for each canvas in the Firestore
        loadCanvas();

        // Handle login button
        ImageButton loginButton = (ImageButton) findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);
            if (isLoggedIn) {
                Intent intent = new Intent(MainMenuActivity.this, UserInfoActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Handle create button
        Button createButton = (Button) findViewById(R.id.create_button);
        createButton.setOnClickListener(v -> {
            // Open draw activity
            Intent intent = new Intent(this, DrawActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Perform search query in the Firestore
        searchCanvas(newText);
        return true;
    }

    public void loadCanvas() {
        db.collection("images").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            String image = document.getString("image");
                            if (name != null && image != null) {
                                createButton(name, image);
                            }
                        }
                    } else {
                        Toast.makeText(this, "Erreur lors du chargement des images.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void searchCanvas(String query) {
        db.collection("images").whereEqualTo("name", query).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        linearLayout.removeAllViews();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            String image = document.getString("image");
                            if (name != null && image != null) {
                                createButton(name, image);
                            }
                        }
                    } else {
                        Toast.makeText(this, "Erreur lors de la recherche des images.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void createButton(String text, String image) {
        // Create button
        Button button = new Button(this);
        button.setText(text);

        // Customize the button appearance
        @SuppressLint("UseCompatLoadingForColorStateLists") ColorStateList colorStateList = getResources().getColorStateList(R.color.blue_button);
        button.setBackgroundTintList(colorStateList);
        button.setTextColor(getResources().getColor(R.color.black));

        // Add button function
        button.setOnClickListener(v -> {
            // Open draw activity with image data
            Intent intent = new Intent(this, DrawActivity.class);
            intent.putExtra("drawingName", text);
            intent.putExtra("encodedImage", image);
            startActivity(intent);
        });

        // Set button constraints
        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        button.setLayoutParams(layoutParams1);
        button.setId(View.generateViewId());

        // Encapsulate in a constraint layout
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.addView(button);

        // Set constraints inside the constraint layout
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(button.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.connect(button.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(button.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(button.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);

        constraintSet.constrainWidth(button.getId(), 0);
        constraintSet.constrainHeight(button.getId(), ConstraintLayout.LayoutParams.WRAP_CONTENT);

        constraintSet.setDimensionRatio(button.getId(), "6:1");

        constraintSet.applyTo(constraintLayout);

        // Add to the scroll view linear layout
        linearLayout.addView(constraintLayout);
    }
}
