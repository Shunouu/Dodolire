package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.button);
        TextView welcomeText = findViewById(R.id.textView);
        ImageView logoImage = findViewById(R.id.imageView);
        ImageView menuIcon = findViewById(R.id.icon_menu);
        ImageView profileIcon = findViewById(R.id.icon_profile);
        TextView crecheNameText = findViewById(R.id.creche_name_text);

        // Vérifier si l'utilisateur est déjà connecté
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);
        String email = sharedPreferences.getString("email", "");
        String crecheName = sharedPreferences.getString("creche_name", "");

        // Afficher le nom de la crèche si disponible
        if (!crecheName.isEmpty()) {
            crecheNameText.setText(crecheName);
        }

        // Si l'utilisateur est déjà connecté, rediriger vers FormActivity
        if (isLoggedIn && !email.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        menuIcon.setOnClickListener(v -> {
            // Utiliser le MenuHelper pour afficher le menu burger
            MenuHelper.showBurgerMenu(this, v);
        });

        // Action pour l'icône profil
        profileIcon.setOnClickListener(v -> {
            // Lancer l'activité de profil
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });
    }
}