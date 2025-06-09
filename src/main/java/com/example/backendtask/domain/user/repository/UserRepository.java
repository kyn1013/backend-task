package com.example.backendtask.domain.user.repository;

import com.example.backendtask.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private List<User> userList = new ArrayList<>();

    public User insert(User user) {
        this.userList.add(user);
        return user;
    }

    public Boolean existByUsername(String username) {
        return userList.stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    public List<User> selectAll() {
        return userList;
    }
}
