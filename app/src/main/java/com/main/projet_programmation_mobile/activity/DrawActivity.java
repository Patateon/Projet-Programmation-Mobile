package com.main.projet_programmation_mobile.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.slider.Slider;
import com.main.projet_programmation_mobile.R;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawActivity extends AppCompatActivity {

    private DrawingView drawingView;
    private ImageButton buttonToggleOptions;
    private int currentColor = Color.BLACK; // Définissez une couleur par défaut
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_main);

        // Handle the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawingView = findViewById(R.id.drawingView);
        buttonToggleOptions = findViewById(R.id.buttonToggleOptions);
        LinearLayout optionsLayout = findViewById(R.id.optionsLayout);

        buttonToggleOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionsLayout.getVisibility() == View.VISIBLE) {
                    optionsLayout.setVisibility(View.INVISIBLE);
                } else {
                    optionsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        ImageButton buttonColor = findViewById(R.id.buttonColor);
        ImageButton buttonClear = findViewById(R.id.buttonClear);
        ImageButton buttonToggleMode = findViewById(R.id.buttonToggleMode);
        Slider sliderThickness = findViewById(R.id.sliderThickness);

        buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPickerDialog();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code pour effacer le dessin
                drawingView.clearDrawing();
            }
        });

        buttonToggleMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.toggleDrawingMode();
            }
        });

        sliderThickness.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                drawingView.setThickness(value);
            }
        });
    }

    private void openColorPickerDialog() {
        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(this, currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                currentColor = color;
                drawingView.setColor(currentColor);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
        });
        colorPickerDialog.show();
    }


}
