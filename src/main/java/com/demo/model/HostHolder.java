package com.demo.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private ThreadLocal<User> userHolder = new ThreadLocal<>();

    public User getUser() {
        return userHolder.get();
    }

    public void setUser(User user) {
        userHolder.set(user);
    }

    public void clear() {
        userHolder.remove();
    }
}
