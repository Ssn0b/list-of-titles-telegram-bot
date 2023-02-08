package com.snob.telegrambot.repository;

import com.snob.telegrambot.model.ListOfTitles;
import com.snob.telegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE User SET page  = ?1 WHERE chatId = ?2")
    void updatePage(int page,Long id);

    @Query("SELECT p FROM User p WHERE p.chatId = ?1")
    User findUserById(Long id);
}
