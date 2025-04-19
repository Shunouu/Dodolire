package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

/**
 * Affiche la liste des histoires archivées
 */
public class ArchiveActivity extends AppCompatActivity {

    private LinearLayout archiveContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        archiveContainer = findViewById(R.id.archive_container);

        setupHeaderActions();
        loadArchivedStories();
    }

    private void setupHeaderActions() {
        ImageView profileIcon = findViewById(R.id.profile);
        ImageView menuIcon    = findViewById(R.id.icon_menu);
        ImageView backIcon    = findViewById(R.id.icon_back);

        // Afficher/masquer l'icône Profil
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        profileIcon.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        if (isLoggedIn) {
            profileIcon.setOnClickListener(v ->
                    startActivity(new Intent(ArchiveActivity.this, ProfileActivity.class))
            );
        }

        // Menu burger
        menuIcon.setOnClickListener(v ->
                MenuHelper.showBurgerMenu(this, v)
        );

        // Retour en arrière
        backIcon.setOnClickListener(v -> finish());
    }

    private void loadArchivedStories() {
        // ... votre code existant pour charger et afficher les cartes
    }

    private void addStoryCard(String title, String content, String theme,
                              String character, String date, int position) {
        // ... votre code existant pour créer et styliser chaque CardView
    }
}
