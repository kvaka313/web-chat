package com.infopulse.repositories;

import com.infopulse.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    //@Query("SELECT msg FROM Message msg INNER JOIN ChatUser cu ON msg.receiver = cu WHERE cu.login=:login")
    List<Message> findByReceiver_Login(String login);

    @Modifying
    void deleteByReceiver_Login(String login);
}
