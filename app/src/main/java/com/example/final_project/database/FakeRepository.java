package com.example.final_project.database;

import com.example.final_project.database.entities.IUserRepository;
import com.example.final_project.database.entities.User;

public class FakeRepository implements IUserRepository {
    @Override
    public User getUserByUsername(String username) {
        if ("testuser1".equals(username)) {
            return new User("testuser1", "testuser1", false);
        } else if ("admin1".equals(username)) {
            return new User("admin1", "admin1", true);
        }
        return null;
    }

}
