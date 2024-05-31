package com.main.projet_programmation_mobile.activity;

import android.content.Intent;
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

    private ImageButton buttonFill;
    private ImageButton buttonToggleMode;

    private ImageButton buttonColor;
    private ImageButton buttonSecondOptions;
    private LinearLayout toolbarSecondOptions;
    private ImageButton buttonSquareShape;
    private ImageButton buttonCircleShape;
    private ImageButton buttonTriangleShape;

    private ImageButton buttonSegmentShape;
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

        ImageButton buttonClear = findViewById(R.id.buttonClear);
        buttonToggleMode = findViewById(R.id.buttonToggleMode); // Initialisez buttonToggleMode ici
        buttonFill = findViewById(R.id.buttonFill);
        buttonColor = findViewById(R.id.buttonColor);
        Slider sliderThickness = findViewById(R.id.sliderThickness);

        buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPickerDialog();
                updateButtonStates();
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
                boolean newState = !buttonToggleMode.isSelected();
                buttonToggleMode.setSelected(newState);
                drawingView.toggleDrawingMode();
                updateButtonStates();
            }
        });

        buttonFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newState = !buttonFill.isSelected();
                buttonFill.setSelected(newState);
                drawingView.toggleFillMode();
                updateButtonStates();
            }
        });

        sliderThickness.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                drawingView.setThickness(value);
            }
        });


        buttonSecondOptions = findViewById(R.id.buttonSecondOptions);
        toolbarSecondOptions = findViewById(R.id.toolbarSecondOptions);


        buttonSecondOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolbarSecondOptions.getVisibility() == View.VISIBLE) {
                    toolbarSecondOptions.setVisibility(View.INVISIBLE);
                } else {
                    toolbarSecondOptions.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonSquareShape = findViewById(R.id.buttonSquareShape);
        buttonSquareShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.toggleSquareDrawingMode();
                updateButtonStates();
            }
        });

        buttonCircleShape = findViewById(R.id.buttonCircleShape);
        buttonCircleShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.toggleCircleDrawingMode();
                updateButtonStates();
            }
        });

        buttonTriangleShape = findViewById(R.id.buttonTriangleShape);
        buttonTriangleShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.toggleTriangleDrawingMode();
                updateButtonStates();
            }
        });

        buttonSegmentShape = findViewById(R.id.buttonSegmentShape);
        buttonSegmentShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.toggleSegmentDrawingMode();
                updateButtonStates();
            }
        });

        ImageButton homeButton = (ImageButton) findViewById(R.id.toolbarImageButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
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

    private void updateButtonStates() {
        buttonFill.setSelected(drawingView.isFilling());
        buttonToggleMode.setSelected(drawingView.isErasing());
        buttonColor.setSelected(drawingView.isDrawing());
        buttonSquareShape.setSelected(drawingView.isDrawingSquare());
        buttonCircleShape.setSelected(drawingView.isDrawingCircle());
        buttonTriangleShape.setSelected(drawingView.isDrawingTriangle());
        buttonSegmentShape.setSelected(drawingView.isDrawingSegment());
    }

}
