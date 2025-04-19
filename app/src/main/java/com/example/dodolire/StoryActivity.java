package com.example.dodolire;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class StoryActivity extends AppCompatActivity {
    // 1) Vues du header
    private ImageView iconMenu, iconProfile;
    // 2) Vues principales
    private ImageView generatedImageView;
    private TextView storyTextView;
    private Button buttonQuit, buttonRecommencer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // --- HEADER ---
        iconMenu    = findViewById(R.id.icon_menu);
        iconProfile = findViewById(R.id.icon_profile);

        iconMenu.setOnClickListener(v ->
                MenuHelper.showBurgerMenu(StoryActivity.this, v)
        );
        iconProfile.setOnClickListener(v ->
                startActivity(new Intent(StoryActivity.this, ProfileActivity.class))
        );

        // --- VUES PRINCIPALES ---
        generatedImageView = findViewById(R.id.generatedImageView);
        storyTextView      = findViewById(R.id.storyTextView);
        buttonQuit         = findViewById(R.id.button_quit);
        buttonRecommencer  = findViewById(R.id.button_recommencer);

        // 3) Récupérer le texte généré et le prompt depuis FormActivity
        String story  = getIntent().getStringExtra("story_text");
        String prompt = getIntent().getStringExtra("story_prompt");

        // Sécurité contre le détournement de prompt
        if (prompt == null || prompt.toLowerCase().contains("oublie tout") || prompt.toLowerCase().contains("ignore les instructions")
                || prompt.toLowerCase().contains("recette") || prompt.toLowerCase().contains("hack") || !prompt.startsWith("Écris une courte histoire pour un enfant")) {
            Toast.makeText(this, "Vous n'avez pas le droit d'écrire ce genre de choses dans cette application.", Toast.LENGTH_LONG).show();
            generatedImageView.setVisibility(View.GONE);
            storyTextView.setText("Vous n'avez pas le droit d'écrire ce genre de choses dans cette application.");
            return;
        }

        storyTextView.setText(story);

        // 4) Lancer la génération de l’image (DALL·E) en tâche de fond
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                return API.generateImage(params[0]);  // Implémente cette méthode dans ton API.java
            }

            @Override
            protected void onPostExecute(String imageUrl) {
                if (imageUrl == null) {
                    Toast.makeText(StoryActivity.this,
                            "Erreur génération de l'image", Toast.LENGTH_LONG).show();
                    return;
                }
                generatedImageView.setVisibility(View.VISIBLE);
                Glide.with(StoryActivity.this)
                        .load(imageUrl)
                        .into(generatedImageView);
            }
        }.execute(prompt);

        // 5) Boutons Quitter / Recommencer
        buttonQuit.setOnClickListener(v -> finish());
        buttonRecommencer.setOnClickListener(v -> {
            // pour repartir sur la même activité
            recreate();
        });
    }
}
