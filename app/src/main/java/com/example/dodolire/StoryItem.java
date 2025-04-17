package com.example.dodolire;

/**
 * Classe représentant une histoire
 */
public class StoryItem {
    private String title;
    private String content;
    private String theme;
    private String character;
    private boolean isArchived;

    public StoryItem(String title, String content, String theme, String character) {
        this.title = title;
        this.content = content;
        this.theme = theme;
        this.character = character;
        this.isArchived = false;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTheme() {
        return theme;
    }

    public String getCharacter() {
        return character;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
