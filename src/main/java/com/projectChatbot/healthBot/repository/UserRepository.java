package com.projectChatbot.healthBot.repository;

import com.projectChatbot.healthBot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findBySubscribedTrue();

    Optional<User> findByPhoneNumber(String phoneNumber);
}