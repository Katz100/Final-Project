package com.example.final_project.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "user_watch_list",
        primaryKeys = {"userId", "movieId"},
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Movie.class,
                        parentColumns = "id",
                        childColumns = "movieId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("userId"),
                @Index("movieId")
        }
)
public class UserWatchList {

    public int userId;
    public int movieId;
    private boolean completed = false;
    private double rating = 0.0;

    public UserWatchList(int userId, int movieId, boolean completed, double rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.completed = completed;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
