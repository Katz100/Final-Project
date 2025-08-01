package com.example.final_project.database.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.final_project.database.MovieWatchlistDatabase;

import java.util.List;

@Dao
public interface MovieWatchlistDAO {
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    void insert(MovieWatchlist movieWatchlist);

    @Delete
    void delete(MovieWatchlist movieWatchlist);

    @Query("SELECT * FROM  " + MovieWatchlistDatabase.MOVIE_WATCHLIST_TABLE)
    List<MovieWatchlist> getAllRecords();
}
