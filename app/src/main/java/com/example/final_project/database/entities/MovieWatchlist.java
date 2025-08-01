package com.example.final_project.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.final_project.database.MovieWatchlistDatabase;

@Entity(tableName = MovieWatchlistDatabase.MOVIE_WATCHLIST_TABLE)
public class MovieWatchlist {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String genre;
    private boolean watched;
    public MovieWatchlist(String title, String genre, boolean watched) {
        this.title = title;
        this.genre = genre;
        this.watched = watched;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}
