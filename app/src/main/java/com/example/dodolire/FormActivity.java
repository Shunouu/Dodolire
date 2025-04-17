package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
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

    private Spinner spinnerType, spinnerAge, spinnerTheme, spinnerGenre;
    private EditText editPrenom, editMotCle;
    private Button btnGenerer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        initViews();
        setupSpinners();
        setupButton();
        setupHeaderActions();
    }

    private void initViews() {
        spinnerType = findViewById(R.id.spinner_type);
        spinnerAge = findViewById(R.id.spinner_age);
        spinnerTheme = findViewById(R.id.spinner_theme);
        spinnerGenre = findViewById(R.id.spinner_genre);
        editPrenom = findViewById(R.id.edit_prenom);
        editMotCle = findViewById(R.id.edit_mot_cle);
        btnGenerer = findViewById(R.id.btn_generer);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(
                this, R.array.type_generation, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapterAge = ArrayAdapter.createFromResource(
                this, R.array.age_enfant, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapterTheme = ArrayAdapter.createFromResource(
                this, R.array.theme_histoire, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapterGenre = ArrayAdapter.createFromResource(
                this, R.array.genre_personnage, R.layout.spinner_item);

        adapterType.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapterAge.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapterTheme.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapterGenre.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinnerType.setAdapter(adapterType);
        spinnerAge.setAdapter(adapterAge);
        spinnerTheme.setAdapter(adapterTheme);
        spinnerGenre.setAdapter(adapterGenre);

        spinnerType.setSelection(0, false);
        spinnerAge.setSelection(0, false);
        spinnerTheme.setSelection(0, false);
        spinnerGenre.setSelection(0, false);
    }

    private void setupHeaderActions() {
        ImageView profileIcon = findViewById(R.id.icon_profile);
        ImageView menuIcon = findViewById(R.id.icon_menu);

        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(FormActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        menuIcon.setOnClickListener(v -> {
            // Utiliser le MenuHelper pour afficher le menu burger
            MenuHelper.showBurgerMenu(this, v);
        });
    }

    private void setupButton() {
        btnGenerer.setOnClickListener(v -> {
            String type = spinnerType.getSelectedItem().toString();
            String age = spinnerAge.getSelectedItem().toString();
            String theme = spinnerTheme.getSelectedItem().toString();
            String genre = spinnerGenre.getSelectedItem().toString();
            String prenom = editPrenom.getText().toString().trim();
            String motCle = editMotCle.getText().toString().trim();

            if (prenom.isEmpty() || motCle.isEmpty()) {
                Toast.makeText(this, "Remplis tous les champs !", Toast.LENGTH_SHORT).show();
                return;
            }

            // Générer l'histoire basée sur les paramètres
            generateStory(type, age, theme, genre, prenom, motCle);
        });
    }

    private void generateStory(String type, String age, String theme, String genre, String prenom, String motCle) {
        // Générer un titre d'histoire basé sur les paramètres
        String title = generateTitle(theme, prenom, motCle);

        // Générer le contenu de l'histoire
        String content = generateContent(type, age, theme, genre, prenom, motCle);

        // Sauvegarder l'histoire dans les préférences pour pouvoir l'archiver plus tard
        saveStoryToPreferences(title, content, theme);

        // Lancer l'activité Story avec les données générées
        Intent intent = new Intent(FormActivity.this, StoryActivity.class);
        intent.putExtra("STORY_TITLE", title);
        intent.putExtra("STORY_CONTENT", content);
        intent.putExtra("STORY_THEME", theme);
        intent.putExtra("STORY_CHARACTER", prenom);
        startActivity(intent);
    }

    private String generateTitle(String theme, String prenom, String motCle) {
        String[] titleTemplates = {
                prenom + " et l'aventure " + motCle,
                "La merveilleuse histoire de " + prenom,
                prenom + " découvre " + motCle,
                "Le voyage de " + prenom + " au pays de " + motCle,
                "Les aventures magiques de " + prenom
        };

        // Sélectionner un titre aléatoire
        int randomIndex = (int) (Math.random() * titleTemplates.length);
        return titleTemplates[randomIndex];
    }

    private String generateContent(String type, String age, String theme, String genre, String prenom, String motCle) {
        StringBuilder content = new StringBuilder();

        // Introduction basée sur le thème
        if (theme.equals("Aventure")) {
            content.append("Il était une fois, dans un monde lointain, un");
            if (genre.equals("Fille")) {
                content.append("e jeune fille nommée ");
            } else if (genre.equals("Garçon")) {
                content.append(" jeune garçon nommé ");
            } else {
                content.append(" petit animal nommé ");
            }
            content.append(prenom).append(". ");
            content.append(prenom).append(" adorait partir à l'aventure et découvrir de nouveaux endroits. ");
        } else if (theme.equals("Animaux")) {
            content.append("Dans une forêt magique vivait ");
            if (genre.equals("Animal")) {
                content.append("un adorable animal nommé ");
            } else {
                content.append(genre.equals("Fille") ? "une petite fille nommée " : "un petit garçon nommé ");
            }
            content.append(prenom).append(" qui adorait les animaux. ");
        } else if (theme.equals("Nature")) {
            content.append("Au cœur d'un jardin merveilleux, ");
            content.append(genre.equals("Fille") ? "une petite fille nommée " : "un petit garçon nommé ");
            content.append(prenom).append(" aimait observer la nature. ");
        } else { // Héros
            content.append("Dans un royaume pas comme les autres, vivait ");
            if (genre.equals("Fille")) {
                content.append("une héroïne nommée ");
            } else if (genre.equals("Garçon")) {
                content.append("un héros nommé ");
            } else {
                content.append("un animal héroïque nommé ");
            }
            content.append(prenom).append(". ");
        }

        // Corps de l'histoire basé sur le mot-clé
        content.append("Un jour, ").append(prenom).append(" découvrit ").append(motCle).append(". ");
        content.append("C'était quelque chose de vraiment spécial qui allait changer sa vie. ");

        // Adapter le contenu à l'âge
        if (age.equals("1 ans")) {
            // Histoire très simple pour les tout-petits
            content.append(prenom).append(" était très content").append(genre.equals("Fille") ? "e" : "").append(". ");
            content.append("\"").append(motCle).append("!\" s'exclama ").append(prenom).append(" avec joie. ");
        } else if (age.equals("2 ans")) {
            // Histoire simple avec répétitions
            content.append(prenom).append(" joua avec ").append(motCle).append(". ");
            content.append("\"J'aime beaucoup ").append(motCle).append("!\" dit ").append(prenom).append(". ");
            content.append(motCle).append(" rendait ").append(prenom).append(" très heureu").append(genre.equals("Fille") ? "se" : "x").append(". ");
        } else { // 3 ans
            // Histoire plus élaborée
            content.append("Grâce à ").append(motCle).append(", ").append(prenom);
            content.append(" vécut de nombreuses aventures extraordinaires. ");
            content.append("Chaque jour apportait son lot de surprises et de découvertes. ");
            content.append(prenom).append(" apprit beaucoup de choses importantes sur l'amitié et le courage. ");
        }

        // Conclusion
        content.append("À la fin de la journée, ").append(prenom).append(" était très fier");
        content.append(genre.equals("Fille") ? "e" : "").append(" de ses aventures. ");

        if (type.equals("Comptine")) {
            // Ajouter une petite comptine à la fin
            content.append("\n\nPetite comptine pour ").append(prenom).append(" :\n");
            content.append(prenom).append(" par-ci, ").append(prenom).append(" par-là,\n");
            content.append("Avec ").append(motCle).append(" on s'amusera,\n");
            content.append("Un, deux, trois, quatre,\n");
            content.append("L'aventure ne fait que commencer !");
        } else {
            // Fin classique pour une histoire
            content.append("Et c'est ainsi que se termine cette belle aventure, mais ").append(prenom);
            content.append(" savait que beaucoup d'autres l'attendaient encore !");
        }

        return content.toString();
    }

    private void saveStoryToPreferences(String title, String content, String theme) {
        SharedPreferences sharedPreferences = getSharedPreferences("stories_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Obtenir le nombre d'histoires déjà sauvegardées
        int storyCount = sharedPreferences.getInt("story_count", 0);

        // Sauvegarder la nouvelle histoire
        editor.putString("story_title_" + storyCount, title);
        editor.putString("story_content_" + storyCount, content);
        editor.putString("story_theme_" + storyCount, theme);
        editor.putLong("story_date_" + storyCount, System.currentTimeMillis());

        // Incrémenter le compteur d'histoires
        editor.putInt("story_count", storyCount + 1);

        editor.apply();
    }
}
