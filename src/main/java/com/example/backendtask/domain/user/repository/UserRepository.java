package com.example.backendtask.domain.user.repository;

import com.example.backendtask.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private List<User> userList = new ArrayList<>();

    public User insert(User user) {
        this.userList.add(user);
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return userList.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public Boolean existByUsername(String email) {
        return userList.stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    public List<User> selectAll() {
        return userList;
    }
}
