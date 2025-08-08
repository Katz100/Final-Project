package com.example.final_project.database;
import androidx.annotation.NonNull;

public class UsersMovies {
    private int userId;
    private int movieId;
    private String title;
    private String genre;
    private boolean completed;

    public UsersMovies(int userId, int movieId, String title, String genre, boolean completed) {
        this.userId = userId;
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.completed = completed;
    }

    @NonNull
    @Override
    public String toString() {
        return "UsersMovies{" +
                "userId=" + userId +
                ", movieId=" + movieId +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
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
}

