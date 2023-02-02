package com.snob.telegrambot.handler.impl;

import com.snob.telegrambot.enums.ConversationState;
import com.snob.telegrambot.handler.UserRequestHandler;
import com.snob.telegrambot.helper.KeyboardHelper;
import com.snob.telegrambot.model.UserRequest;
import com.snob.telegrambot.model.UserSession;
import com.snob.telegrambot.service.TelegramService;
import com.snob.telegrambot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Component
    public class ChooseCategoryHandler extends UserRequestHandler {

    public static String ADD_TO_LIST = "Добавити в список";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public INeedHelpHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return isTextMessage(request.getUpdate(), ADD_TO_LIST);
    }

    @Override
    public void handle(UserRequest dispatchRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildListMenu();
        telegramService.sendMessage(dispatchRequest.getChatId(),"Оберіть категорію яку хочете поповнити⤵️", replyKeyboardMarkup);

        UserSession userSession = dispatchRequest.getUserSession();
        userSession.setState(ConversationState.WAITING_FOR_CITY);
        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
