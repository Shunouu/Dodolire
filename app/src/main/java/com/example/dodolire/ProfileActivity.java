package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText crecheNameEditText;
    private EditText emailEditText;
    private TextView logoutButton;
    private ImageButton backButton;
    private ImageView menuButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialisation des vues
        initViews();

        // Charger les données du profil
        loadProfileData();

        // Configurer les écouteurs
        setupListeners();
    }

    private void initViews() {
        crecheNameEditText = findViewById(R.id.profile_creche_name);
        emailEditText = findViewById(R.id.profile_email);
        logoutButton = findViewById(R.id.logout_button);
        backButton = findViewById(R.id.back_button);
        menuButton = findViewById(R.id.menu_button);
        saveButton = findViewById(R.id.save_button);
    }

    private void loadProfileData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        // Récupérer les données sauvegardées
        String crecheName = sharedPreferences.getString("creche_name", "");
        String email = sharedPreferences.getString("email", "");

        // Afficher les données dans les champs
        crecheNameEditText.setText(crecheName);
        emailEditText.setText(email);
    }

    private void setupListeners() {
        // Bouton de retour
        backButton.setOnClickListener(v -> finish());

        // Bouton de menu
        menuButton.setOnClickListener(v -> {
            // Utiliser le MenuHelper pour afficher le menu burger
            MenuHelper.showBurgerMenu(this, v);
        });

        // Bouton de sauvegarde
        saveButton.setOnClickListener(v -> {
            saveProfileData();
            Toast.makeText(this, "Profil mis à jour", Toast.LENGTH_SHORT).show();
        });

        // Bouton de déconnexion
        logoutButton.setOnClickListener(v -> {
            // Afficher une boîte de dialogue de confirmation
            new AlertDialog.Builder(this)
                    .setTitle("Déconnexion")
                    .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        // Effacer le statut de connexion
                        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("is_logged_in", false);
                        editor.apply();

                        // Rediriger vers l'écran de connexion
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });
    }

    private void saveProfileData() {
        String crecheName = crecheNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("creche_name", crecheName);
        editor.putString("email", email);

        editor.apply();
    }
}
