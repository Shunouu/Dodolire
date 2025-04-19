package com.example.dodolire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StoryPagerAdapter extends RecyclerView.Adapter<StoryPagerAdapter.StoryViewHolder> {

    private final List<StoryItem> stories;
    private final Context context;
    private TextToSpeech textToSpeech;
    private boolean isSpeaking = false;
    private int currentPlayingPosition = -1;

    public StoryPagerAdapter(Context context, List<StoryItem> stories) {
        this.context = context;
        this.stories = stories;

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(context, status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.FRENCH);
            }
        });
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_page_item, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition == RecyclerView.NO_POSITION) {
            return;
        }

        StoryItem story = stories.get(adapterPosition);

        // Set title and content
        holder.storyTitle.setText(story.getTitle());
        holder.storyContent.setText(story.getContent());

        // Configure main image based on theme
        setThemeImage(holder.mainStoryImage, story.getTheme());

        // Configure image thumbnails
        setupImageScroll(holder, story.getTheme());

        // Configure audio button
        holder.audioButton.setOnClickListener(v -> {
            if (isSpeaking && currentPlayingPosition == adapterPosition) {
                stopSpeaking();
                holder.audioButton.setImageResource(R.drawable.ic_audio_play);
                isSpeaking = false;
                currentPlayingPosition = -1;
            } else {
                // Stop any currently playing audio first
                stopAllAudio();

                // Start playing this story
                startSpeaking(story.getContent());
                holder.audioButton.setImageResource(R.drawable.ic_audio_pause);
                isSpeaking = true;
                currentPlayingPosition = adapterPosition;
            }
        });

        // Reset audio button icon (important when recycling views)
        if (isSpeaking && currentPlayingPosition == adapterPosition) {
            holder.audioButton.setImageResource(R.drawable.ic_audio_pause);
        } else {
            holder.audioButton.setImageResource(R.drawable.ic_audio_play);
        }

        // Configure action buttons
        holder.quitButton.setOnClickListener(v -> {
            stopSpeaking();
            ((StoryActivity) context).finish();
        });

        holder.restartButton.setOnClickListener(v -> {
            stopSpeaking();
            Intent intent = new Intent(context, FormActivity.class);
            context.startActivity(intent);
            ((StoryActivity) context).finish();
        });

        holder.archiveButton.setOnClickListener(v -> {
            archiveStory(story);
            holder.archiveButton.setText(R.string.histoire_archivee);
            holder.archiveButton.setEnabled(false);
            Toast.makeText(context, R.string.histoire_archivee, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    private void setupImageScroll(StoryViewHolder holder, String theme) {
        holder.imageContainer.removeAllViews();

        // Generate 5 different images based on theme
        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    context.getResources().getDimensionPixelSize(R.dimen.story_image_width),
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(8, 0, 8, 0);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Generate a unique image for each thumbnail
            generateThemeImage(imageView, theme, i);

            imageView.setContentDescription(context.getString(R.string.image_histoire) + " " + (i + 1));

            imageView.setOnClickListener(v -> {
                // Update main image
                holder.mainStoryImage.setImageDrawable(imageView.getDrawable());
                holder.mainStoryImage.setBackgroundColor(imageView.getSolidColor());
            });

            holder.imageContainer.addView(imageView);
        }
    }

    private void setThemeImage(ImageView imageView, String theme) {
        // Set background color based on theme
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

        imageView.setBackgroundColor(ContextCompat.getColor(context, colorRes));

        // Use appropriate icon based on theme
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
        imageView.setColorFilter(ContextCompat.getColor(context, android.R.color.white));
    }

    private void generateThemeImage(ImageView imageView, String theme, int index) {
        // Create a unique color for each image based on theme and index
        Random random = new Random(theme.hashCode() + (long)index * 100L);

        int baseColor;
        int iconRes;

        // Determine base color and icon based on theme
        switch (theme.toLowerCase()) {
            case "aventure":
                baseColor = Color.rgb(30, 144, 255); // DodgerBlue
                iconRes = android.R.drawable.ic_menu_compass;
                break;
            case "animaux":
                baseColor = Color.rgb(50, 205, 50); // LimeGreen
                iconRes = android.R.drawable.ic_menu_myplaces;
                break;
            case "nature":
                baseColor = Color.rgb(46, 139, 87); // SeaGreen
                iconRes = android.R.drawable.ic_menu_gallery;
                break;
            case "héros":
                baseColor = Color.rgb(220, 20, 60); // Crimson
                iconRes = android.R.drawable.ic_menu_send;
                break;
            default:
                baseColor = Color.rgb(255, 165, 0); // Orange
                iconRes = android.R.drawable.ic_menu_view;
                break;
        }

        // Slightly vary color for each image
        int red = Color.red(baseColor);
        int green = Color.green(baseColor);
        int blue = Color.blue(baseColor);

        // Add random variation to each color component
        red = Math.max(0, Math.min(255, red + random.nextInt(61) - 30));
        green = Math.max(0, Math.min(255, green + random.nextInt(61) - 30));
        blue = Math.max(0, Math.min(255, blue + random.nextInt(61) - 30));

        int color = Color.rgb(red, green, blue);

        imageView.setBackgroundColor(color);
        imageView.setImageResource(iconRes);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setColorFilter(ContextCompat.getColor(context, android.R.color.white));

        // Store color to retrieve it later
        imageView.setTag(color);
    }

    private void startSpeaking(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void stopSpeaking() {
        if (textToSpeech != null && textToSpeech.isSpeaking()) {
            textToSpeech.stop();
        }
    }

    public void stopAllAudio() {
        stopSpeaking();
        isSpeaking = false;
        // Reset audio button states - use more specific notifier
        int oldPosition = currentPlayingPosition;
        currentPlayingPosition = -1;
        if (oldPosition >= 0) {
            notifyItemChanged(oldPosition);
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private void archiveStory(StoryItem story) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("archived_stories", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int archivedCount = sharedPreferences.getInt("archived_count", 0);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        editor.putString("archived_title_" + archivedCount, story.getTitle());
        editor.putString("archived_content_" + archivedCount, story.getContent());
        editor.putString("archived_theme_" + archivedCount, story.getTheme());
        editor.putString("archived_character_" + archivedCount, story.getCharacter());
        editor.putString("archived_date_" + archivedCount, currentDate);
        editor.putInt("archived_count", archivedCount + 1);

        editor.apply();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        TextView storyTitle;
        TextView storyContent;
        ImageView mainStoryImage;
        LinearLayout imageContainer;
        Button quitButton;
        Button restartButton;
        Button archiveButton;
        ImageButton audioButton;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            storyTitle = itemView.findViewById(R.id.story_title);
            storyContent = itemView.findViewById(R.id.story_content);
            mainStoryImage = itemView.findViewById(R.id.story_image);
            imageContainer = itemView.findViewById(R.id.image_container);
            quitButton = itemView.findViewById(R.id.button_quit);
            restartButton = itemView.findViewById(R.id.button_restart);
            archiveButton = itemView.findViewById(R.id.button_archive);
            audioButton = itemView.findViewById(R.id.audio_button);
        }
    }
}