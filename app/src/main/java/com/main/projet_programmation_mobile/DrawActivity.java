package com.main.projet_programmation_mobile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

public class DrawActivity extends AppCompatActivity {

    private DrawingView drawingView;
    private Button buttonToggleOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_main);

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

        Button buttonColor = findViewById(R.id.buttonColor);
        Button buttonClear = findViewById(R.id.buttonClear);
        Button buttonToggleMode = findViewById(R.id.buttonToggleMode);
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
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                drawingView.setThickness(value);
            }
        });
    }

    private void openColorPickerDialog() {
        final AlertDialog.Builder colorPickerDialog = new AlertDialog.Builder(this);
        colorPickerDialog.setTitle("Choisir une couleur");
        final String[] colors = {"Noir", "Rouge", "Vert", "Bleu", "Jaune", "Cyan", "Magenta", "Gris"}; // Liste des couleurs disponibles
        final int[] colorValues = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.GRAY}; // Valeurs de couleur correspondantes

        colorPickerDialog.setItems(colors, (dialog, which) -> {
            int selectedColor = colorValues[which];
            drawingView.setColor(selectedColor);
        });

        colorPickerDialog.show();
    }

    private void openThicknessPickerDialog() {
        final AlertDialog.Builder thicknessPickerDialog = new AlertDialog.Builder(this);
        thicknessPickerDialog.setTitle("Choisir une épaisseur");

        final String[] thicknessOptions = {"1", "3", "5", "8", "10"}; // Options d'épaisseur disponibles
        final float[] thicknessValues = {1f, 3f, 5f, 8f, 10f}; // Valeurs d'épaisseur correspondantes

        thicknessPickerDialog.setItems(thicknessOptions, (dialog, which) -> {
            float selectedThickness = thicknessValues[which];
            drawingView.setThickness(selectedThickness);
        });

        thicknessPickerDialog.show();
    }
}
