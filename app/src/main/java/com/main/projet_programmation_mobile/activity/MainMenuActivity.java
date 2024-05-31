package com.main.projet_programmation_mobile.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.main.projet_programmation_mobile.R;
import com.main.projet_programmation_mobile.databases.DatabaseCanvasManager;
import com.main.projet_programmation_mobile.helper.DatabaseCanvasHelper;

import java.sql.SQLDataException;

public class MainMenuActivity  extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private LinearLayout linearLayout;
    private DatabaseCanvasManager databaseCanvasManager;
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

        try {
            databaseCanvasManager = new DatabaseCanvasManager(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            databaseCanvasManager.open();
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        linearLayout = findViewById(R.id.linearLayout_canvas);

        // Load a button for each canvas in the database
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
    protected void onDestroy() {
        databaseCanvasManager.close();
        super.onDestroy();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Perform search query in the database
        Cursor cursor = databaseCanvasManager.fetch(newText);

//        if (cursor != null){
//            if (cursor.moveToFirst()){
//                scrollView.removeAllViews();
//                while (cursor.moveToNext()){
//                    Button button = new Button(this);
//                    int columnIndex = cursor.getColumnIndex(DatabaseCanvasHelper.name);
//                    button.setText(cursor.getString(columnIndex));
//                    scrollView.addView(button);
//                }
//            }
//        }
        
        return true;
    }

    public void loadCanvas(){
        Cursor cursor = databaseCanvasManager.fetch();
        if (cursor.moveToFirst()){
            do{
                int columnIndex = cursor.getColumnIndex(DatabaseCanvasHelper.name);
                if (columnIndex != -1) {
                    createButton(cursor.getString(columnIndex));
                }
            }while (cursor.moveToNext());
        }
    }

    public void createButton(String text){

        // Create button
        Button button = new Button(this);
        button.setText(text);

        // Customize the button appearance
        @SuppressLint("UseCompatLoadingForColorStateLists") ColorStateList colorStateList = getResources().getColorStateList(R.color.blue_button);
        button.setBackgroundTintList(colorStateList);
        button.setTextColor(getResources().getColor(R.color.black));

        // Add button function
        button.setOnClickListener(v -> {
            // Open draw activity
            startDrawing();
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

    public void startDrawing(){
        // Open draw activity
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }
}