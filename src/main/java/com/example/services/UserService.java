package com.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final Set<String> activeUsernames = ConcurrentHashMap.newKeySet();

    public boolean addUser(String username) {
        boolean added = activeUsernames.add(username);
        if (added) {
            notifyUserListUpdate();
        }
        return added;
    }

    public void removeUser(String username) {
        activeUsernames.remove(username);
        notifyUserListUpdate();
    }

    public boolean isUserExists(String username) {
        return activeUsernames.contains(username);
    }
    public Set<String> getActiveUsernames() {
        return activeUsernames;
    }
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyUserListUpdate() {
        messagingTemplate.convertAndSend("/topic/users", getActiveUsernames());
    }
}