package com.snob.telegrambot.repository;

import com.snob.telegrambot.model.ListOfTitles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListOfTitlesRepository extends JpaRepository<ListOfTitles, Long> {
}

