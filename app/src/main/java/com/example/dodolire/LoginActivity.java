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

public class LoginActivity extends AppCompatActivity {

    private EditText  emailEditText;
    private EditText  passwordEditText;
    private Button    loginButton;
    private TextView  registerLink;
    private TextView  errorMessageText;
    private ImageView profileIcon;
    private ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1) Récupération des vues
        emailEditText    = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton      = findViewById(R.id.login_button);
        registerLink     = findViewById(R.id.login_link);
        errorMessageText = findViewById(R.id.error_message);
        profileIcon      = findViewById(R.id.icon_profile);
        menuIcon         = findViewById(R.id.icon_menu);

        // Masquer le message d'erreur par défaut
        errorMessageText.setVisibility(TextView.INVISIBLE);

        // 2) Header : afficher le menu burger, icône Profil ONLY si déjà connecté
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        // Profil invisible si non connecté
        profileIcon.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        if (isLoggedIn) {
            profileIcon.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
            });
        }

        // Menu burger toujours accessible
        menuIcon.setOnClickListener(v ->
                MenuHelper.showBurgerMenu(LoginActivity.this, v)
        );

        // 3) Gestion du bouton Connexion
        loginButton.setOnClickListener(v -> {
            String email    = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Champs non vides
            if (email.isEmpty() || password.isEmpty()) {
                showError("Veuillez remplir tous les champs.");
                return;
            }

            // Validation email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showError("Veuillez entrer un email valide.");
                return;
            }

            // Lecture des prefs
            SharedPreferences userPrefs   = prefs;
            String savedEmail             = userPrefs.getString("email", "");
            String savedPassword          = userPrefs.getString("password", "");

            // Vérification
            if (savedEmail.isEmpty() || !email.equals(savedEmail)) {
                showError("Email incorrect.");
                return;
            }
            if (!password.equals(savedPassword)) {
                showError("Mot de passe incorrect.");
                return;
            }

            // Succès : marquer connecté
            userPrefs.edit()
                    .putBoolean("is_logged_in", true)
                    .apply();

            Toast.makeText(this,
                    "Connexion réussie !", Toast.LENGTH_SHORT).show();

            // Aller vers FormActivity
            Intent intent = new Intent(LoginActivity.this, FormActivity.class);
            startActivity(intent);
            finish();
        });

        // 4) Lien vers inscription
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    /** Affiche un message d’erreur sous les champs */
    private void showError(String message) {
        errorMessageText.setText(message);
        errorMessageText.setVisibility(TextView.VISIBLE);
    }
}
