package com.codegnan.shopnest.Service;

import java.util.Optional;

import com.codegnan.shopnest.Entity.User;

public interface UserService {

    void registerUser(User user);

    Optional<User> findByEmail(String email);

    boolean emailExists(String email);
}