package com.example.final_project.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.final_project.database.entities.UserWatchList;

import java.util.List;

@Dao
public interface UserWatchListDAO {
    @Insert
    void insert(UserWatchList userWatchList);

    @Update
    void update(UserWatchList userWatchList);

    @Delete
    void delete(UserWatchList userWatchList);

    @Query("SELECT * FROM user_watch_list WHERE id = :id AND completed = false")
    List<UserWatchList> getUncompletedWatchlistItems(int id);

    @Query("SELECT * FROM user_watch_list WHERE id = :id")
    List<UserWatchList> getAllWatchlistItems();

    }
