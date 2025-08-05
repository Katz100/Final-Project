package com.example.final_project.database.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MovieDAO {
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    long insert(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM  movie")
    List<Movie> getAllRecords();
}
