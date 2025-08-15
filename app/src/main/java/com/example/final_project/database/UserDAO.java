package com.example.final_project.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import com.example.final_project.database.entities.User;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Delete()
    void delete(User user);

    @Query("DELETE from user_table")
    void deleteAll();

    @Query("SELECT * FROM " + "user_table" + " WHERE username == :username")
    User getUserByUserName(String username);

    @Query("SELECT * FROM " + "user_table" + " ORDER BY username")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user_table WHERE isAdmin == false")
    LiveData<List<User>> getAllNonAdmins();

    @Query("SELECT * FROM user_table WHERE isAdmin == true")
    LiveData<List<User>> getAllAdmins();

    @Update
    int update(User user);

    @Query("UPDATE user_table SET isAdmin = 1 WHERE username == :username")
    void promoteUser(String username);

    @Query("UPDATE user_table SET isAdmin = 0 WHERE username == :username")
    void demoteUser(String username);


/*
    @Query("SELECT * FROM " + MovieWatchListDatabase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE from " + MovieWatchListDatabase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * FROM " + MovieWatchListDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM " + MovieWatchListDatabase.USER_TABLE + " WHERE id == :userId")
    LiveData<User> getUserByUserId(int userId);
 */

}
