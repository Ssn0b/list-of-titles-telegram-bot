package com.snob.telegrambot.handler.impl.selectFromList;

import com.snob.telegrambot.enums.ConversationState;
import com.snob.telegrambot.handler.UserRequestHandler;
import com.snob.telegrambot.helper.KeyboardHelper;
import com.snob.telegrambot.model.ListOfTitles;
import com.snob.telegrambot.model.User;
import com.snob.telegrambot.model.tools.UserRequest;
import com.snob.telegrambot.model.tools.UserSession;
import com.snob.telegrambot.repository.ListOfTitlesRepository;
import com.snob.telegrambot.repository.UserRepository;
import com.snob.telegrambot.service.TelegramService;
import com.snob.telegrambot.service.UserSessionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

import static com.snob.telegrambot.constant.Constant.*;
import static com.snob.telegrambot.constant.Constant.CURRENT_WATCH_LIST;

@Component
public class CategoryForSelectEnteredHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;
    private final ListOfTitlesRepository userRepository;
    private final UserRepository usersRepository;



    public CategoryForSelectEnteredHandler(TelegramService telegramService,
                                           KeyboardHelper keyboardHelper, UserSessionService userSessionService,
                                           ListOfTitlesRepository userRepository, UserRepository usersRepository) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
        this.userRepository = userRepository;
        this.usersRepository = usersRepository;
    }
    @Override
    public boolean isApplicable(UserRequest request) {
        return (isTextMessage(request.getUpdate(), WATCHED_LIST) || isTextMessage(request.getUpdate(), WISH_TO_WATCH_LIST)
                || isTextMessage(request.getUpdate(), FAVORITE_LIST) || isTextMessage(request.getUpdate(), CURRENT_WATCH_LIST)
                || isTextMessage(request.getUpdate(), ALL_LIST))
                && ConversationState.WAITING_FOR_CATEGORY_IN_SELECT.equals(request.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest dispatchRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMainMenu();
        UserSession session = dispatchRequest.getUserSession();

        String category = dispatchRequest.getUpdate().getMessage().getText();
        session.setCategory(category);
        User user = usersRepository.findUserById(dispatchRequest.getChatId());
        List<ListOfTitles> listOfTitles;
        if (category.equals(ALL_LIST)) {
            listOfTitles = userRepository.findAllByUser(dispatchRequest.getChatId());
            session.setMaxList(listOfTitles.size());
            if (user.getPage() >= ((int) Math.ceil(session.getMaxList() * 1.0 / 5))){
                usersRepository.updatePage(0,dispatchRequest.getChatId());
                user = usersRepository.findUserById(dispatchRequest.getChatId());
            }
            Pageable page = PageRequest.of(user.getPage(), 5);
            listOfTitles = userRepository.findAllByUser(dispatchRequest.getChatId(),page);
            InlineKeyboardMarkup inlineKeyboardMarkup = keyboardHelper.buildOutputList(listOfTitles,user.getPage());
            telegramService.sendMessage(dispatchRequest.getChatId(), "Ваш список: ",inlineKeyboardMarkup);
        }else {
            listOfTitles = userRepository.findAllByUserAndCategory(dispatchRequest.getChatId(),category);
            session.setMaxList(listOfTitles.size());
            if (user.getPage() > ((int) Math.ceil(session.getMaxList() * 1.0 / 5))){
                usersRepository.updatePage(0,dispatchRequest.getChatId());
                user = usersRepository.findUserById(dispatchRequest.getChatId());
            }
            Pageable page = PageRequest.of(user.getPage(), 5);
            listOfTitles = userRepository.findAllByUserAndCategory(dispatchRequest.getChatId(), category,page);
            InlineKeyboardMarkup inlineKeyboardMarkup = keyboardHelper.buildOutputList(listOfTitles,user.getPage());
            telegramService.sendMessage(dispatchRequest.getChatId(), category+": ",inlineKeyboardMarkup);
        }

        userSessionService.saveSession(dispatchRequest.getChatId(), session);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
