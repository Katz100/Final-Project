package com.example.final_project.database;

public class UserCompletedMovies {
    private int userId;
    private int movieId;

    private double rating;
    private String title;
    private String genre;

    public UserCompletedMovies(int userId, int movieId, double rating, String title, String genre) {
        this.userId = userId;
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "UserCompletedMovies{" +
                "userId=" + userId +
                ", movieId=" + movieId +
                ", rating=" + rating +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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
