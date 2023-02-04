package com.snob.telegrambot.handler.impl.selectFromList;

import com.snob.telegrambot.enums.ConversationState;
import com.snob.telegrambot.handler.UserRequestHandler;
import com.snob.telegrambot.helper.KeyboardHelper;
import com.snob.telegrambot.model.ListOfTitles;
import com.snob.telegrambot.model.UserRequest;
import com.snob.telegrambot.model.UserSession;
import com.snob.telegrambot.repository.ListOfTitlesRepository;
import com.snob.telegrambot.service.TelegramService;
import com.snob.telegrambot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

import static com.snob.telegrambot.constant.Constant.*;
import static com.snob.telegrambot.constant.Constant.CURRENT_WATCH_LIST;

@Component
public class CategoryForSelectEnteredHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;
    private final ListOfTitlesRepository userRepository;


    public CategoryForSelectEnteredHandler(TelegramService telegramService,
                                           KeyboardHelper keyboardHelper, UserSessionService userSessionService, ListOfTitlesRepository userRepository) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
        this.userRepository = userRepository;
    }
    @Override
    public boolean isApplicable(UserRequest request) {
        return (isTextMessage(request.getUpdate(), WATCHED_LIST) || isTextMessage(request.getUpdate(), WISH_TO_WATCH_LIST)
                || isTextMessage(request.getUpdate(), FAVORITE_LIST) || isTextMessage(request.getUpdate(), CURRENT_WATCH_LIST))
                && ConversationState.WAITING_FOR_CATEGORY_IN_SELECT.equals(request.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest dispatchRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMainMenu();
        UserSession session = dispatchRequest.getUserSession();

        String category = dispatchRequest.getUpdate().getMessage().getText();
        session.setCategory(category);
        telegramService.sendMessage(dispatchRequest.getChatId(), "Ваш список: ",replyKeyboardMarkup);

        List<ListOfTitles> listOfTitles = userRepository.findAllProductsByUserAndCategory(dispatchRequest.getChatId(),category);
        listOfTitles.forEach(list -> telegramService.sendMessage(dispatchRequest.getChatId(),list.getTitleName()));

        session.setState(ConversationState.CONVERSATION_STARTED);

        userSessionService.saveSession(dispatchRequest.getChatId(), session);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
