package com.example.dodolire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class StoryActivity extends AppCompatActivity {

    private ViewPager2 storyViewPager;
    private StoryPagerAdapter pagerAdapter;
    private final List<StoryItem> stories = new ArrayList<>();
    private ImageView prevButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // Initialize views
        storyViewPager = findViewById(R.id.story_view_pager);
        prevButton = findViewById(R.id.prev_story_button);
        nextButton = findViewById(R.id.next_story_button);

        // Configure header actions
        setupHeaderActions();

        // Get story data
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("STORY_TITLE");
            String content = intent.getStringExtra("STORY_CONTENT");
            String theme = intent.getStringExtra("STORY_THEME");
            String character = intent.getStringExtra("STORY_CHARACTER");

            if (title != null && content != null) {
                // Add current story
                stories.add(new StoryItem(title, content, theme, character));

                // Load other stories from preferences
                loadStoriesFromPreferences();

                // Setup ViewPager
                setupViewPager();
            }
        }

        // Setup navigation buttons
        setupNavigationButtons();
    }

    private void setupHeaderActions() {
        ImageView menuIcon = findViewById(R.id.icon_menu);
        ImageView profileIcon = findViewById(R.id.icon_profile);
        ImageView backIcon = findViewById(R.id.icon_back);

        menuIcon.setOnClickListener(v -> {
            // Show burger menu
            MenuHelper.showBurgerMenu(this, v);
        });

        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(StoryActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        backIcon.setOnClickListener(v -> finish());
    }

    private void loadStoriesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("stories_data", MODE_PRIVATE);
        int storyCount = sharedPreferences.getInt("story_count", 0);

        for (int i = 0; i < storyCount; i++) {
            String title = sharedPreferences.getString("story_title_" + i, "");
            String content = sharedPreferences.getString("story_content_" + i, "");
            String theme = sharedPreferences.getString("story_theme_" + i, "");
            String character = sharedPreferences.getString("story_character_" + i, "");

            // Don't add current story again
            if (!title.equals(stories.get(0).getTitle())) {
                stories.add(new StoryItem(title, content, theme, character));
            }
        }
    }

    private void setupViewPager() {
        pagerAdapter = new StoryPagerAdapter(this, stories);
        storyViewPager.setAdapter(pagerAdapter);

        // Set page transformer for slide effect
        storyViewPager.setPageTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setAlpha(0.25f + r * 0.75f);
            page.setScaleX(0.85f + r * 0.15f);
        });

        // Add page change listener to stop audio when changing pages
        storyViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Stop audio when changing page
                pagerAdapter.stopAllAudio();
                updateNavigationButtonsVisibility(position);
            }
        });
    }

    private void setupNavigationButtons() {
        prevButton.setOnClickListener(v -> {
            if (storyViewPager.getCurrentItem() > 0) {
                // Stop audio before changing page
                pagerAdapter.stopAllAudio();
                storyViewPager.setCurrentItem(storyViewPager.getCurrentItem() - 1);
            }
        });

        nextButton.setOnClickListener(v -> {
            if (storyViewPager.getCurrentItem() < stories.size() - 1) {
                // Stop audio before changing page
                pagerAdapter.stopAllAudio();
                storyViewPager.setCurrentItem(storyViewPager.getCurrentItem() + 1);
            }
        });

        // Update button visibility based on position
        storyViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateNavigationButtonsVisibility(position);
            }
        });

        // Initialize button visibility
        updateNavigationButtonsVisibility(0);
    }

    private void updateNavigationButtonsVisibility(int position) {
        prevButton.setVisibility(position > 0 ? View.VISIBLE : View.INVISIBLE);
        nextButton.setVisibility(position < stories.size() - 1 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pagerAdapter != null) {
            pagerAdapter.shutdown();
        }
    }
}