package com.snob.telegrambot.repository;

import com.snob.telegrambot.model.ListOfTitles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

import java.util.List;

public interface ListOfTitlesRepository extends JpaRepository<ListOfTitles, Long>{
    //JPQL
    @Query("SELECT p FROM ListOfTitles p WHERE p.userId = ?1 AND p.titleStatus = ?2")
    List<ListOfTitles> findAllByUserAndCategory(Long id,String titleStatus);
    @Query("SELECT p FROM ListOfTitles p WHERE p.userId = ?1 AND p.titleStatus = ?2")
    List<ListOfTitles> findAllByUserAndCategory(Long id,String titleStatus, Pageable pageable);
    @Query("SELECT p FROM ListOfTitles p WHERE p.userId = ?1")
    List<ListOfTitles> findAllByUser(Long id, Pageable pageable);
    @Query("SELECT p FROM ListOfTitles p WHERE p.userId = ?1")
    List<ListOfTitles> findAllByUser(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE ListOfTitles SET titleStatus = ?1 WHERE userId = ?2 AND titleName = ?3")
    void updateTitleName(String titleStatus,Long id,String titleName);

    @Transactional
    @Modifying
    @Query("UPDATE ListOfTitles SET mark = ?1 WHERE userId = ?2 AND titleName = ?3")
    void updateTitleMark(int mark,Long id,String titleName);


}

