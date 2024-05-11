package com.main.projet_programmation_mobile.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.main.projet_programmation_mobile.R;
import com.main.projet_programmation_mobile.databases.DatabaseCanvasHelper;
import com.main.projet_programmation_mobile.databases.DatabaseCanvasManager;

public class MainMenuActivity  extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ScrollView scrollView;
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

        byte[] test = new byte[0];
        databaseCanvasManager.insert("test", test);

//        SearchView searchView = findViewById(R.id.searchView);
//        searchView.setOnQueryTextListener(this);
        scrollView = findViewById(R.id.scrollView);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Perform search query in the database
        Cursor cursor = databaseCanvasManager.fetch(newText);

        if (cursor.moveToFirst()){
            scrollView.removeAllViews();
            while (cursor.moveToNext()){
                Button button = new Button(this);
                int columnIndex = cursor.getColumnIndex(DatabaseCanvasHelper.name);
                button.setText(cursor.getString(columnIndex));
                scrollView.addView(button);
            }
        }
        
        return true;
    }
}