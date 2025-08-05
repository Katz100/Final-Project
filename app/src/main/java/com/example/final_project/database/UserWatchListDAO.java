package com.example.final_project.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.final_project.database.entities.UserWatchList;

import java.util.List;

@Dao
public interface UserWatchListDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserWatchList userWatchList);

    @Update
    void update(UserWatchList userWatchList);

    @Delete
    void delete(UserWatchList userWatchList);

    @Query("DELETE FROM user_watch_list WHERE userId = :userId")
    void deleteAllWatchlistItems(int userId);

    @Query("SELECT * FROM user_watch_list WHERE userId = :userId AND completed = true")
    List<UserWatchList> getCompletedMoviesWithRatings(int userId);

    @Query("select movie.title, movie.genre " +
            "from user_watch_list " +
            "join movie on user_watch_list.movieId = movie.id " +
            "where user_watch_list.userId = :userId AND completed = false")
    LiveData<List<UsersMovies>> getUncompletedWatchlistItems(int userId);

    @Query("SELECT * FROM user_watch_list WHERE userId = :userId")
    List<UserWatchList> getAllWatchlistItems(int userId);

}
