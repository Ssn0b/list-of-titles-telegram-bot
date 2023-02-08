package com.snob.telegrambot.handler.handlerForCallback;

import com.snob.telegrambot.enums.ConversationState;
import com.snob.telegrambot.handler.UserRequestHandler;
import com.snob.telegrambot.helper.KeyboardHelper;
import com.snob.telegrambot.model.tools.UserRequest;
import com.snob.telegrambot.model.tools.UserSession;
import com.snob.telegrambot.repository.ListOfTitlesRepository;
import com.snob.telegrambot.service.TelegramService;
import com.snob.telegrambot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Objects;

@Component
public class SetMarkHandler extends UserRequestHandler {
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final ListOfTitlesRepository userRepository;

    public SetMarkHandler(TelegramService telegramService, KeyboardHelper keyboardHelper,
                          ListOfTitlesRepository userRepository) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userRepository = userRepository;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return isCallbackQuery(request.getUpdate())
                && ConversationState.WAITING_FOR_MARK_SET.equals(request.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest dispatchRequest) {
        UserSession session = dispatchRequest.getUserSession();
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMainMenu();
        for (int i = 0; i < 10; i++) {
            if(Objects.equals(dispatchRequest.getUpdate().getCallbackQuery().getData(), String.valueOf(i+1))){
                userRepository.updateTitleMark(i+1, dispatchRequest.getChatId(), session.getTitle());
                telegramService.sendMessage(dispatchRequest.getChatId(),"Оцінку поставлено", replyKeyboardMarkup);
            }
        }
        if (Objects.equals(dispatchRequest.getUpdate().getCallbackQuery().getData(), "пропустити")) {
            telegramService.sendMessage(dispatchRequest.getChatId(),"Оберіть дію", replyKeyboardMarkup);
        }
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
