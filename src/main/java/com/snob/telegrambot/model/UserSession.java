package com.snob.telegrambot.model;

import com.snob.telegrambot.enums.ConversationState;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserSession  {
    private Long chatId;
    private ConversationState state;
    private String category;
    private String title;
}
