package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText crecheNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private TextView loginLink;
    private TextView errorMessageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialisation des éléments de la vue
        initViews();
        setupHeaderActions();
        setupListeners();
    }

    private void initViews() {
        crecheNameEditText = findViewById(R.id.creche_name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);
        loginLink = findViewById(R.id.login_link);
        errorMessageText = findViewById(R.id.error_message);

        // Masquer le message d'erreur par défaut
        errorMessageText.setVisibility(TextView.INVISIBLE);
    }

    private void setupHeaderActions() {
        ImageView profileIcon = findViewById(R.id.icon_profile);
        ImageView menuIcon = findViewById(R.id.icon_menu);

        if (profileIcon != null && menuIcon != null) {
            profileIcon.setOnClickListener(v -> {
                // Action pour l'icône de profil - sur l'écran d'inscription, rediriger vers login
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            });

            menuIcon.setOnClickListener(v -> {
                // Utiliser le MenuHelper pour afficher le menu burger
                MenuHelper.showBurgerMenu(this, v);
            });
        }
    }

    private void setupListeners() {
        // Action lors du clic sur le bouton "S'inscrire"
        registerButton.setOnClickListener(v -> {
            // Récupérer les valeurs des champs
            String crecheName = crecheNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Vérification des entrées utilisateur
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
            SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
            String existingEmail = sharedPreferences.getString("email", "");
            if (!existingEmail.isEmpty() && existingEmail.equals(email)) {
                showError("Cet email existe déjà");
                return;
            }

            // Sauvegarde des données utilisateur dans SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("creche_name", crecheName);
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putBoolean("is_logged_in", true);
            editor.apply();

            // Message de succès d'inscription
            Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();

            // Redirection vers la page principale
            Intent intent = new Intent(RegisterActivity.this, FormActivity.class);
            startActivity(intent);
            finish(); // Ferme l'activité d'inscription après redirection
        });

        // Action lors du clic sur le lien "Se connecter"
        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish(); // Ferme l'activité d'inscription
        });
    }

    private void showError(String message) {
        errorMessageText.setText(message);
        errorMessageText.setVisibility(TextView.VISIBLE);
    }
}
