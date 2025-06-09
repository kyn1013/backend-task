package com.example.backendtask.domain.user.service;

import com.example.backendtask.domain.user.entity.User;
import com.example.backendtask.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.selectAll();
    }
}
