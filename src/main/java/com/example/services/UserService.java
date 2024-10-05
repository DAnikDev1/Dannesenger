package com.example.services;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final Set<String> activeUsernames = ConcurrentHashMap.newKeySet();

    public boolean addUser(String username) {
        return activeUsernames.add(username);
    }

    public void removeUser(String username) {
        activeUsernames.remove(username);
    }

    public boolean isUserExists(String username) {
        return activeUsernames.contains(username);
    }
}