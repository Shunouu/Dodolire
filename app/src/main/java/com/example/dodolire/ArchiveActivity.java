package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;

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
        ImageView menuIcon = findViewById(R.id.icon_menu);
        ImageView backIcon = findViewById(R.id.icon_back);

        profileIcon.setOnClickListener(v -> startActivity(new Intent(ArchiveActivity.this, ProfileActivity.class)));

        menuIcon.setOnClickListener(v -> {
            // Utiliser le MenuHelper pour afficher le menu burger
            MenuHelper.showBurgerMenu(this, v);
        });

        backIcon.setOnClickListener(v -> finish());
    }

    private void loadArchivedStories() {
        // Vider le conteneur avant de charger les histoires
        archiveContainer.removeAllViews();

        SharedPreferences sharedPreferences = getSharedPreferences("archived_stories", MODE_PRIVATE);
        int archivedCount = sharedPreferences.getInt("archived_count", 0);

        if (archivedCount == 0) {
            // Aucune histoire archivée
            TextView emptyText = new TextView(this);
            emptyText.setText(R.string.no_archives);
            emptyText.setTextSize(16);
            emptyText.setPadding(16, 32, 16, 16);
            emptyText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            archiveContainer.addView(emptyText);
        } else {
            // Afficher les histoires archivées
            for (int i = archivedCount - 1; i >= 0; i--) {
                String title = sharedPreferences.getString("archived_title_" + i, "");
                String content = sharedPreferences.getString("archived_content_" + i, "");
                String theme = sharedPreferences.getString("archived_theme_" + i, "");
                String character = sharedPreferences.getString("archived_character_" + i, "");
                String date = sharedPreferences.getString("archived_date_" + i, "");

                addStoryCard(title, content, theme, character, date, i);
            }
        }
    }

    private void addStoryCard(String title, String content, String theme, String character, String date, final int position) {
        // Créer une nouvelle carte d'histoire archivée
        CardView cardView = new CardView(this);
        cardView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Use standard dimension values instead of resource references that might be missing
        cardView.setRadius(16); // Standard corner radius value
        cardView.setCardElevation(4); // Standard elevation value

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cardView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 16); // Standard margin bottom value
        cardView.setLayoutParams(layoutParams);

        // Créer le contenu de la carte
        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        contentLayout.setPadding(16, 16, 16, 16); // Standard padding values

        // Ajouter l'image
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                100, // Standard width value
                100  // Standard height value
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setContentDescription(getString(R.string.image_histoire));

        // Générer une image basée sur le thème
        setThemeImage(imageView, theme);

        // Créer le conteneur pour le texte
        LinearLayout textContainer = new LinearLayout(this);
        textContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        textContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textContainerParams = (LinearLayout.LayoutParams) textContainer.getLayoutParams();
        textContainerParams.setMarginStart(16); // Standard margin start value
        textContainer.setLayoutParams(textContainerParams);

        // Ajouter le titre
        TextView titleText = new TextView(this);
        titleText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        titleText.setText(title);
        titleText.setTextSize(18);
        titleText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        titleText.setMaxLines(2);
        titleText.setEllipsize(android.text.TextUtils.TruncateAt.END);

        // Ajouter la date
        TextView dateText = new TextView(this);
        dateText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        dateText.setText(date);
        dateText.setTextSize(14);
        dateText.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        LinearLayout.LayoutParams dateParams = (LinearLayout.LayoutParams) dateText.getLayoutParams();
        dateParams.topMargin = 8; // Standard top margin value
        dateText.setLayoutParams(dateParams);

        // Ajouter le texte "Appuyer pour lire"
        TextView tapToReadText = new TextView(this);
        tapToReadText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        tapToReadText.setText(R.string.tap_to_read);
        tapToReadText.setTextSize(12);
        tapToReadText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        tapToReadText.setTypeface(null, Typeface.ITALIC);
        LinearLayout.LayoutParams tapParams = (LinearLayout.LayoutParams) tapToReadText.getLayoutParams();
        tapParams.topMargin = 8; // Standard top margin value
        tapToReadText.setLayoutParams(tapParams);

        // Créer le bouton de suppression
        ImageView deleteButton = new ImageView(this);
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                48, // Standard width value
                48  // Standard height value
        ));
        deleteButton.setImageResource(android.R.drawable.ic_menu_delete);
        deleteButton.setContentDescription(getString(R.string.delete_archive));
        deleteButton.setBackgroundResource(android.R.drawable.btn_default);
        deleteButton.setPadding(8, 8, 8, 8);

        // Ajouter un clic sur le bouton de suppression
        deleteButton.setOnClickListener(v -> {
            // Afficher une boîte de dialogue de confirmation
            new AlertDialog.Builder(ArchiveActivity.this)
                    .setTitle(R.string.delete_confirmation_title)
                    .setMessage(R.string.delete_confirmation_message)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Supprimer l'histoire archivée
                        deleteArchivedStory(position);
                        // Recharger les histoires archivées
                        loadArchivedStories();
                        // Afficher un message de confirmation
                        Toast.makeText(ArchiveActivity.this, R.string.story_deleted, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });

        // Assembler tous les éléments
        textContainer.addView(titleText);
        textContainer.addView(dateText);
        textContainer.addView(tapToReadText);

        contentLayout.addView(imageView);
        contentLayout.addView(textContainer);
        contentLayout.addView(deleteButton);

        cardView.addView(contentLayout);

        // Ajouter un clic sur la carte pour ouvrir l'histoire
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(ArchiveActivity.this, StoryActivity.class);
            intent.putExtra("STORY_TITLE", title);
            intent.putExtra("STORY_CONTENT", content);
            intent.putExtra("STORY_THEME", theme);
            intent.putExtra("STORY_CHARACTER", character);
            startActivity(intent);
        });

        archiveContainer.addView(cardView);
    }

    private void setThemeImage(ImageView imageView, String theme) {
        // Définir une couleur de fond basée sur le thème
        int colorRes;

        switch (theme.toLowerCase()) {
            case "aventure":
                colorRes = android.R.color.holo_blue_light;
                break;
            case "animaux":
                colorRes = android.R.color.holo_green_light;
                break;
            case "nature":
                colorRes = android.R.color.holo_green_dark;
                break;
            case "héros":
                colorRes = android.R.color.holo_red_light;
                break;
            default:
                colorRes = android.R.color.holo_orange_light;
                break;
        }

        imageView.setBackgroundColor(ContextCompat.getColor(this, colorRes));

        // Utiliser une icône appropriée basée sur le thème
        int iconRes;

        switch (theme.toLowerCase()) {
            case "aventure":
                iconRes = android.R.drawable.ic_menu_compass;
                break;
            case "animaux":
                iconRes = android.R.drawable.ic_menu_myplaces;
                break;
            case "nature":
                iconRes = android.R.drawable.ic_menu_gallery;
                break;
            case "héros":
                iconRes = android.R.drawable.ic_menu_send;
                break;
            default:
                iconRes = android.R.drawable.ic_menu_view;
                break;
        }

        imageView.setImageResource(iconRes);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
    }

    private void deleteArchivedStory(int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("archived_stories", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int archivedCount = sharedPreferences.getInt("archived_count", 0);

        // Déplacer toutes les histoires après la position supprimée
        for (int i = position; i < archivedCount - 1; i++) {
            String nextTitle = sharedPreferences.getString("archived_title_" + (i + 1), "");
            String nextContent = sharedPreferences.getString("archived_content_" + (i + 1), "");
            String nextTheme = sharedPreferences.getString("archived_theme_" + (i + 1), "");
            String nextCharacter = sharedPreferences.getString("archived_character_" + (i + 1), "");
            String nextDate = sharedPreferences.getString("archived_date_" + (i + 1), "");

            editor.putString("archived_title_" + i, nextTitle);
            editor.putString("archived_content_" + i, nextContent);
            editor.putString("archived_theme_" + i, nextTheme);
            editor.putString("archived_character_" + i, nextCharacter);
            editor.putString("archived_date_" + i, nextDate);
        }

        // Supprimer la dernière histoire (maintenant dupliquée)
        editor.remove("archived_title_" + (archivedCount - 1));
        editor.remove("archived_content_" + (archivedCount - 1));
        editor.remove("archived_theme_" + (archivedCount - 1));
        editor.remove("archived_character_" + (archivedCount - 1));
        editor.remove("archived_date_" + (archivedCount - 1));

        // Mettre à jour le compteur
        editor.putInt("archived_count", archivedCount - 1);

        editor.apply();
    }
}
