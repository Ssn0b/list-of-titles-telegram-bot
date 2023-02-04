package com.snob.telegrambot.handler.impl.selectFromList;

import com.snob.telegrambot.enums.ConversationState;
import com.snob.telegrambot.handler.UserRequestHandler;
import com.snob.telegrambot.helper.KeyboardHelper;
import com.snob.telegrambot.model.UserRequest;
import com.snob.telegrambot.model.UserSession;
import com.snob.telegrambot.service.TelegramService;
import com.snob.telegrambot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.snob.telegrambot.constant.Constant.SELECT_FROM_LIST;

@Component
public class ChooseCategoryForSelectHandler extends UserRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public ChooseCategoryForSelectHandler(TelegramService telegramService, KeyboardHelper keyboardHelper,
                                          UserSessionService userSessionService) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }
    @Override
    public boolean isApplicable(UserRequest request) {
        return isTextMessage(request.getUpdate(), SELECT_FROM_LIST);
    }

    @Override
    public void handle(UserRequest dispatchRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildListMenu();
        telegramService.sendMessage(dispatchRequest.getChatId(),"Оберіть категорію яку хочете вивести⤵️", replyKeyboardMarkup);

        UserSession userSession = dispatchRequest.getUserSession();
        userSession.setState(ConversationState.WAITING_FOR_CATEGORY_IN_SELECT);
        userSessionService.saveSession(userSession.getChatId(), userSession);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
