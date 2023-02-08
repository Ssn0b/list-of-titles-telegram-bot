package com.snob.telegrambot.handler.impl.addToList;

import com.snob.telegrambot.enums.ConversationState;
import com.snob.telegrambot.handler.UserRequestHandler;
import com.snob.telegrambot.helper.KeyboardHelper;
import com.snob.telegrambot.model.ListOfTitles;
import com.snob.telegrambot.model.tools.UserRequest;
import com.snob.telegrambot.model.tools.UserSession;
import com.snob.telegrambot.repository.ListOfTitlesRepository;
import com.snob.telegrambot.service.TelegramService;
import com.snob.telegrambot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Component
public class TitleEnteredHandler extends UserRequestHandler {

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;
    private final ListOfTitlesRepository userRepository;


    public TitleEnteredHandler(TelegramService telegramService, KeyboardHelper keyboardHelper,
                               UserSessionService userSessionService, ListOfTitlesRepository userRepository) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return isTextMessage(request.getUpdate())
                && ConversationState.WAITING_FOR_TEXT.equals(request.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest dispatchRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMainMenu();
        UserSession session = dispatchRequest.getUserSession();

        String title = dispatchRequest.getUpdate().getMessage().getText();
        session.setTitle(title);

        telegramService.sendMessage(dispatchRequest.getChatId(),"'"+session.getTitle()+"' додано у список '" +
                        session.getCategory().toLowerCase() + "'.",
                replyKeyboardMarkup);
        InlineKeyboardMarkup inlineKeyboardMarkup = keyboardHelper.setMark();
        telegramService.sendMessage(dispatchRequest.getChatId(),"Поставити оцінку",inlineKeyboardMarkup);
        session.setState(ConversationState.WAITING_FOR_MARK_SET);

        ListOfTitles listOfTitles = ListOfTitles.builder()
                .userId(session.getChatId())
                .titleName(session.getTitle())
                .titleStatus(session.getCategory())
                .mark(0)
                .build();

        List<ListOfTitles> listOfTitlesList = userRepository.findAllByUser(dispatchRequest.getChatId());
        boolean check = false;
        for (ListOfTitles ofTitles : listOfTitlesList) {
            if (title.equals(ofTitles.getTitleName())) {
                userRepository.updateTitleName(session.getCategory(), dispatchRequest.getChatId(), title);
                check = true;
            }
        }
        if (!check) {
            userRepository.save(listOfTitles);
        }

        userSessionService.saveSession(dispatchRequest.getChatId(), session);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
