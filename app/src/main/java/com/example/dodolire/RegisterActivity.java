package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText   crecheNameEditText;
    private EditText   emailEditText;
    private EditText   passwordEditText;
    private Button     registerButton;
    private TextView   loginLink;
    private TextView   errorMessageText;
    private ImageView  profileIcon;
    private ImageView  menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1) Récupération des vues
        crecheNameEditText = findViewById(R.id.creche_name);
        emailEditText      = findViewById(R.id.email);
        passwordEditText   = findViewById(R.id.password);
        registerButton     = findViewById(R.id.register_button);
        loginLink          = findViewById(R.id.login_link);
        errorMessageText   = findViewById(R.id.error_message);
        profileIcon        = findViewById(R.id.icon_profile);
        menuIcon           = findViewById(R.id.icon_menu);

        // Masquer le message d'erreur au départ
        errorMessageText.setVisibility(TextView.INVISIBLE);

        // 2) Header : affichage conditionnel de l'icône Profil
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        profileIcon.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        if (isLoggedIn) {
            profileIcon.setOnClickListener(v ->
                    startActivity(new Intent(RegisterActivity.this, ProfileActivity.class))
            );
        }

        // Menu burger accessible même si non connecté (ou modifiez selon vos besoins)
        menuIcon.setOnClickListener(v ->
                MenuHelper.showBurgerMenu(this, v)
        );

        // 3) Configuration des listeners
        registerButton.setOnClickListener(v -> {
            String crecheName = crecheNameEditText.getText().toString().trim();
            String email      = emailEditText.getText().toString().trim();
            String password   = passwordEditText.getText().toString().trim();

            // Validation des champs
            if (crecheName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showError("Veuillez remplir tous les champs");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showError("Veuillez entrer un email valide");
                return;
            }
            if (password.length() < 6) {
                showError("Le mot de passe doit contenir au moins 6 caractères");
                return;
            }

            // Vérifier si l'email existe déjà
            String existingEmail = prefs.getString("email", "");
            if (!existingEmail.isEmpty() && existingEmail.equals(email)) {
                showError("Cet email existe déjà");
                return;
            }

            // Sauvegarder les données et marquer connecté
            prefs.edit()
                    .putString("creche_name", crecheName)
                    .putString("email", email)
                    .putString("password", password)
                    .putBoolean("is_logged_in", true)
                    .apply();

            Toast.makeText(RegisterActivity.this,
                    "Inscription réussie", Toast.LENGTH_SHORT).show();

            // Rediriger vers FormActivity et fermer RegisterActivity
            Intent intent = new Intent(RegisterActivity.this, FormActivity.class);
            startActivity(intent);
            finish();
        });

        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    /** Affiche un message d'erreur sous le champ approprié */
    private void showError(String message) {
        errorMessageText.setText(message);
        errorMessageText.setVisibility(TextView.VISIBLE);
    }
}
