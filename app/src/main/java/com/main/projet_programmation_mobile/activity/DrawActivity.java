package com.main.projet_programmation_mobile.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Intent;
import android.content.SharedPreferences;
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
                // Code pour basculer entre les modes dessin et effacement
                drawingView.toggleDrawingMode();
                updateButtonStates();
            }
        });

        buttonFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code pour basculer entre les modes de remplissage et de non-remplissage
                drawingView.toggleFillMode();
                updateButtonStates();
            }
        });

        sliderThickness.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                // Code pour modifier l'épaisseur du pinceau
                drawingView.setThickness(value);
            }
        });

        // Second Options
        toolbarSecondOptions = findViewById(R.id.toolbarSecondOptions);
        buttonSecondOptions = findViewById(R.id.buttonSecondOptions);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isPremium = sharedPreferences.getBoolean("is_premium", false);

        buttonSecondOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPremium){
                    Toast.makeText(DrawActivity.this, "Vous n'etes pas membre premium", Toast.LENGTH_SHORT).show();
                }else {
                    if (toolbarSecondOptions.getVisibility() == View.VISIBLE) {
                        toolbarSecondOptions.setVisibility(View.INVISIBLE);
                    } else {
                        toolbarSecondOptions.setVisibility(View.VISIBLE);
                    }
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
                saveImage();
            }
        });

        // Check if there's image data passed through intent
        String encodedImage = getIntent().getStringExtra("encodedImage");
        System.out.println(encodedImage);
        if (encodedImage != null && !encodedImage.isEmpty()) {
            // Décoder l'image encodée en base64 et l'afficher dans DrawingView
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            drawingView.setBitmap(decodedBitmap);
        }
      
        ImageButton homeButton = (ImageButton) findViewById(R.id.toolbarImageButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        });
    }

    private void openColorPickerDialog() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // Color selected
                currentColor = color;
                drawingView.setColor(currentColor);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // Cancel action
            }
        });
        colorPicker.show();
    }

    private void saveImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nommer l'image");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String imageName = input.getText().toString();

                if (!imageName.isEmpty()) {
                    // Save the image with the given name
                    Bitmap bitmap = drawingView.getBitmap();
                    String imageData = encodeToBase64(bitmap);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> imageMap = new HashMap<>();
                    imageMap.put("name", imageName);
                    imageMap.put("image", imageData);

                    db.collection("images").document(imageName)
                            .set(imageMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(DrawActivity.this, "Image enregistrée avec succès", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DrawActivity.this, "Erreur lors de l'enregistrement de l'image", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
