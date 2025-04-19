package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private ImageView menuIcon, profileIcon;
    private TextView crecheNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1) Récupération des vues
        startButton    = findViewById(R.id.button);
        menuIcon       = findViewById(R.id.icon_menu);
        profileIcon    = findViewById(R.id.icon_profile);
        crecheNameText = findViewById(R.id.creche_name_text); // si utilisé

        // 2) Affichage conditionnel du bouton Profil
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        profileIcon.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        if (isLoggedIn) {
            profileIcon.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        // 3) Action du bouton "Commencer" (connexion / formulaire...)
        startButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LoginActivity.class))
        );

        // 4) Menu burger
        menuIcon.setOnClickListener(v ->
                MenuHelper.showBurgerMenu(MainActivity.this, v)
        );
    }
}
