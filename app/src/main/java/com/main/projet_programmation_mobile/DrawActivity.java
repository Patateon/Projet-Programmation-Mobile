package com.main.projet_programmation_mobile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.slider.Slider;

public class DrawActivity extends AppCompatActivity {

    private DrawingView drawingView;
    private Button buttonColor;
    private Button buttonClear;

    private Button buttonToggleMode;

    private Button buttonThickness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_main);

        drawingView = findViewById(R.id.drawingView);
        buttonColor = findViewById(R.id.buttonColor);
        buttonClear = findViewById(R.id.buttonClear);
        buttonToggleMode = findViewById(R.id.buttonToggleMode);
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

