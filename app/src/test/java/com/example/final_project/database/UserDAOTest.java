package com.example.final_project.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.final_project.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class UserDAOTest {

    private MovieWatchlistDatabase db;
    private UserDAO userDao;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MovieWatchlistDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDao = db.userDAO();
    }

    @After
    public void tearDown() throws IOException {
        db.close();
    }


    @Test
    public void testInsertAndGetUserByUserName() {
        User user = new User("testuser", "testpassword", false);
        userDao.insert(user);

        User retrieved = userDao.getUserByUserName("testuser");
        assertNotNull(retrieved);
        assertEquals("testuser", retrieved.getUsername());
        assertEquals("testpassword", retrieved.getPassword());
    }

    @Test
    public void testDeleteUser() {
        User user = new User("deleteuser", "pass123", false);
        userDao.insert(user);

        User insertedUser = userDao.getUserByUserName("deleteuser");

        userDao.delete(insertedUser);

        User deleted = userDao.getUserByUserName("deleteuser");
        assertNull(deleted);
    }

    @Test
    public void testUpdateUser(){
        User user = new User("updateuser", "oldpass", false);
        userDao.insert(user);

        User existing = userDao.getUserByUserName("updateuser");
        assertNotNull(existing);

        existing.setPassword("newpass");
        int updatedRows = userDao.update(existing);

        assertEquals(1, updatedRows);

        User updated = userDao.getUserByUserName("updateuser");
        assertNotNull(updated);
        assertEquals("newpass", updated.getPassword());
    }

    @Test
    public void testDeleteAllUsers() {
        userDao.insert(new User("user1", "pass1", false));
        userDao.insert(new User("user2", "pass2", false));

        userDao.deleteAll();

        User user1 = userDao.getUserByUserName("user1");
        User user2 = userDao.getUserByUserName("user2");

        assertNull(user1);
        assertNull(user2);
    }

}