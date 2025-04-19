package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {
    private ImageView iconMenu, iconProfile;
    private Spinner spinnerType, spinnerAge, spinnerTheme, spinnerGenre;
    private EditText editPrenom, editMotCle;
    private Button btnGenerer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // 1) Header : menu burger + icône profil conditionnelle
        iconMenu    = findViewById(R.id.icon_menu);
        iconProfile = findViewById(R.id.icon_profile);

        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        iconProfile.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        if (isLoggedIn) {
            iconProfile.setOnClickListener(v -> {
                startActivity(new Intent(FormActivity.this, ProfileActivity.class));
            });
        }
        iconMenu.setOnClickListener(v ->
                MenuHelper.showBurgerMenu(FormActivity.this, v)
        );

        // 2) Récupération des champs du formulaire
        spinnerType  = findViewById(R.id.spinner_type);
        spinnerAge   = findViewById(R.id.spinner_age);
        spinnerTheme = findViewById(R.id.spinner_theme);
        spinnerGenre = findViewById(R.id.spinner_genre);
        editPrenom   = findViewById(R.id.edit_prenom);
        editMotCle   = findViewById(R.id.edit_mot_cle);
        btnGenerer   = findViewById(R.id.btn_generer);

        // 3) Peupler les Spinners
        ArrayAdapter<CharSequence> adaptType = ArrayAdapter.createFromResource(
                this, R.array.array_type, android.R.layout.simple_spinner_item
        );
        adaptType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adaptType);

        ArrayAdapter<CharSequence> adaptAge = ArrayAdapter.createFromResource(
                this, R.array.array_age, android.R.layout.simple_spinner_item
        );
        adaptAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setAdapter(adaptAge);

        ArrayAdapter<CharSequence> adaptTheme = ArrayAdapter.createFromResource(
                this, R.array.array_theme, android.R.layout.simple_spinner_item
        );
        adaptTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(adaptTheme);

        ArrayAdapter<CharSequence> adaptGenre = ArrayAdapter.createFromResource(
                this, R.array.array_genre, android.R.layout.simple_spinner_item
        );
        adaptGenre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(adaptGenre);

        // 4) Génération d’histoire
        btnGenerer.setOnClickListener(v -> {
            String type   = spinnerType.getSelectedItem().toString();
            String age    = spinnerAge.getSelectedItem().toString();
            String theme  = spinnerTheme.getSelectedItem().toString();
            String genre  = spinnerGenre.getSelectedItem().toString();
            String prenom = editPrenom.getText().toString().trim();
            String motCle = editMotCle.getText().toString().trim();

            String prompt = String.format(
                    "Écris une courte histoire pour un enfant de %s ans, sur le thème \"%s\", " +
                            "avec un personnage %s nommé %s, incluant le mot-clé \"%s\".",
                    age, theme, genre, prenom, motCle
            );

            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    return API.generateStory(params[0]);
                }

                @Override
                protected void onPostExecute(String story) {
                    if (story == null) {
                        Toast.makeText(FormActivity.this,
                                "Échec de la génération", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(FormActivity.this, StoryActivity.class);
                    intent.putExtra("story_prompt", prompt);
                    intent.putExtra("story_text", story);
                    startActivity(intent);
                }
            }.execute(prompt);
        });
    }
}
