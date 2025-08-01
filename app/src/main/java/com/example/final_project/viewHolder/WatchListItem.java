package com.example.final_project.viewHolder;

public class WatchListItem {
    private String title;
    private String genre;

    public WatchListItem(String title, String genre) {
        this.title = title;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }
}
