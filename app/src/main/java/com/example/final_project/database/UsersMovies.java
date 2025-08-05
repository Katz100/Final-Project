package com.example.final_project.database;

public class UsersMovies {
    private int userId;
    private int movieId;
    private String title;
    private String genre;

    public UsersMovies(int userId, int movieId, String title, String genre) {
        this.userId = userId;
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "UsersMovies{" +
                "userId=" + userId +
                ", movieId=" + movieId +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                '}';
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

