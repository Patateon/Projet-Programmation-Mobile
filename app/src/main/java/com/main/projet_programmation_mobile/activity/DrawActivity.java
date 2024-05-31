package com.main.projet_programmation_mobile.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.Slider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.main.projet_programmation_mobile.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawActivity extends AppCompatActivity {

    private DrawingView drawingView;
    private ImageButton buttonToggleOptions;

    private ImageButton buttonFill;
    private ImageButton buttonToggleMode;

    private ImageButton buttonSave;
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

        buttonSave = findViewById(R.id.saveButton);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaveDialog();
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

    private void updateButtonStates() {
        buttonFill.setSelected(drawingView.isFilling());
        buttonToggleMode.setSelected(drawingView.isErasing());
        buttonColor.setSelected(drawingView.isDrawing());
        buttonSquareShape.setSelected(drawingView.isDrawingSquare());
        buttonCircleShape.setSelected(drawingView.isDrawingCircle());
        buttonTriangleShape.setSelected(drawingView.isDrawingTriangle());
        buttonSegmentShape.setSelected(drawingView.isDrawingSegment());
    }

    private void saveDrawingToFirestore(String drawingName) {
        DrawingView drawingView = findViewById(R.id.drawingView);
        Bitmap bitmap = drawingView.getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Map<String, Object> drawing = new HashMap<>();
        drawing.put("name", drawingName);
        drawing.put("image", encodedImage);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("drawings").document(drawingName)
                .set(drawing)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Dessin sauvegardé avec succès
                        Toast.makeText(DrawActivity.this, "Drawing saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Erreur lors de la sauvegarde du dessin
                        Toast.makeText(DrawActivity.this, "Failed to save drawing", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Drawing");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String drawingName = input.getText().toString();
                saveDrawingToFirestore(drawingName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



}
