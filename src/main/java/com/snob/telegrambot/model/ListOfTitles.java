package com.snob.telegrambot.model;

import lombok.Builder;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@Table(name = "list_of_titles")
public class ListOfTitles implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String titleName;
    private String titleStatus;
    private int mark;

    @Tolerate
    public ListOfTitles() {}
}
