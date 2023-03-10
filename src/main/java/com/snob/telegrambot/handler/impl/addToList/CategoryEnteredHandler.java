package com.snob.telegrambot.handler.impl.addToList;

import com.snob.telegrambot.enums.ConversationState;
import com.snob.telegrambot.handler.UserRequestHandler;
import com.snob.telegrambot.helper.KeyboardHelper;
import com.snob.telegrambot.model.tools.UserRequest;
import com.snob.telegrambot.model.tools.UserSession;
import com.snob.telegrambot.service.TelegramService;
import com.snob.telegrambot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.snob.telegrambot.constant.Constant.*;

@Component
public class CategoryEnteredHandler extends UserRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public CategoryEnteredHandler(TelegramService telegramService,
                                  KeyboardHelper keyboardHelper, UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return (isTextMessage(request.getUpdate(), WATCHED_LIST) || isTextMessage(request.getUpdate(), WISH_TO_WATCH_LIST)
                || isTextMessage(request.getUpdate(), FAVORITE_LIST) || isTextMessage(request.getUpdate(), CURRENT_WATCH_LIST))
                && ConversationState.WAITING_FOR_CATEGORY.equals(request.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest dispatchRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        telegramService.sendMessage(dispatchRequest.getChatId(),"Напишіть назву фільму",replyKeyboardMarkup);


        String category = dispatchRequest.getUpdate().getMessage().getText();

        UserSession session = dispatchRequest.getUserSession();
        session.setCategory(category);
        session.setState(ConversationState.WAITING_FOR_TEXT);
        userSessionService.saveSession(dispatchRequest.getChatId(), session);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
