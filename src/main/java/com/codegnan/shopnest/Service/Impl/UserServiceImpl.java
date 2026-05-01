package com.codegnan.shopnest.Service.Impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codegnan.shopnest.Entity.Role;
import com.codegnan.shopnest.Entity.User;
import com.codegnan.shopnest.Repository.UserRepository;
import com.codegnan.shopnest.Service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void registerUser(User user) {
		if (emailExists(user.getEmail())) {
			throw new RuntimeException("Email already registered: " + user.getEmail());
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.USER);
		userRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public boolean emailExists(String email) {
		return userRepository.existsByEmail(email);
	}
}