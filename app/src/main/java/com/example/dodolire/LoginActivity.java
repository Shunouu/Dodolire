package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerLink;
    private TextView errorMessageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Liens vers les vues
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.login_link);
        errorMessageText = findViewById(R.id.error_message);

        // Masquer le message d'erreur par défaut
        errorMessageText.setVisibility(TextView.INVISIBLE);

        setupHeaderActions();

        // Gestion du bouton de connexion
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Vérifier si les champs sont vides
            if (email.isEmpty() || password.isEmpty()) {
                showError("Veuillez remplir tous les champs.");
                return;
            }

            // Vérifier les identifiants avec les données enregistrées
            SharedPreferences userPrefs = getSharedPreferences("user_data", MODE_PRIVATE);
            String savedEmail = userPrefs.getString("email", "");
            String savedPassword = userPrefs.getString("password", "");

            // Vérification stricte de l'email ET du mot de passe
            if (savedEmail.isEmpty() || !email.equals(savedEmail)) {
                showError("Email incorrect.");
                return;
            }

            if (!password.equals(savedPassword)) {
                showError("Mot de passe incorrect.");
                return;
            }

            // Si on arrive ici, l'authentification est réussie
            // Marquer l'utilisateur comme connecté
            SharedPreferences.Editor editor = userPrefs.edit();
            editor.putBoolean("is_logged_in", true);
            editor.apply();

            // Connexion réussie
            Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show();

            // Lancer l'activité suivante (FormActivity)
            Intent intent = new Intent(LoginActivity.this, FormActivity.class);
            startActivity(intent);
            finish();
        });

        // Lien vers l'inscription
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void showError(String message) {
        errorMessageText.setText(message);
        errorMessageText.setVisibility(TextView.VISIBLE);
    }

    private void setupHeaderActions() {
        ImageView profileIcon = findViewById(R.id.icon_profile);
        ImageView menuIcon = findViewById(R.id.icon_menu);

        profileIcon.setOnClickListener(v -> {
            // Rediriger vers le profil
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        menuIcon.setOnClickListener(v -> {
            // Utiliser le MenuHelper pour afficher le menu burger
            MenuHelper.showBurgerMenu(this, v);
        });
    }
}
