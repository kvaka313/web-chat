package com.infopulse.repositories;

import com.infopulse.entities.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    ChatUser findByLogin(String login);
}
