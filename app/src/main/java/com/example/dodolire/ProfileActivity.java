package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText crecheNameEditText;
    private EditText emailEditText;
    private Button saveButton;
    private TextView logoutButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 1) Récupération des vues
        crecheNameEditText = findViewById(R.id.profile_creche_name);
        emailEditText      = findViewById(R.id.profile_email);
        saveButton         = findViewById(R.id.save_button);
        logoutButton       = findViewById(R.id.logout_button);
        backButton         = findViewById(R.id.back_button);

        // 2) Chargement des données existantes
        loadProfileData();

        // 3) Bouton retour simple
        backButton.setOnClickListener(v -> finish());

        // 4) Sauvegarde des champs « nom de la crèche » et « email »
        saveButton.setOnClickListener(v -> {
            saveProfileData();
            Toast.makeText(ProfileActivity.this, "Profil enregistré", Toast.LENGTH_SHORT).show();
        });

        // 5) Déconnexion : on vide les prefs et on repart sur LoginActivity
        logoutButton.setOnClickListener(v -> {
            // Vide complètement les SharedPreferences liées à l'utilisateur
            SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Lance LoginActivity en vidant la pile pour empêcher le retour
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    /** Charge le nom de la crèche et l'email depuis les prefs */
    private void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        String crecheName = prefs.getString("creche_name", "");
        String email      = prefs.getString("email", "");
        crecheNameEditText.setText(crecheName);
        emailEditText.setText(email);
    }

    /** Sauvegarde le nom de la crèche et l'email dans les prefs */
    private void saveProfileData() {
        String crecheName = crecheNameEditText.getText().toString().trim();
        String email      = emailEditText.getText().toString().trim();

        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        prefs.edit()
                .putString("creche_name", crecheName)
                .putString("email", email)
                .apply();
    }
}
