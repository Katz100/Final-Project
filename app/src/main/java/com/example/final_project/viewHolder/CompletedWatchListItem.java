package com.example.final_project.viewHolder;

public class CompletedWatchListItem {
    private String title;
    private String genre;

    private String rating;

    public CompletedWatchListItem(String title, String genre, String rating) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getRating() { return rating; }
}
