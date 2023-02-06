package com.snob.telegrambot.handler.impl;

import com.snob.telegrambot.handler.UserRequestHandler;
import com.snob.telegrambot.helper.KeyboardHelper;
import com.snob.telegrambot.model.ListOfTitles;
import com.snob.telegrambot.model.User;
import com.snob.telegrambot.model.tools.UserRequest;
import com.snob.telegrambot.repository.UserRepository;
import com.snob.telegrambot.service.TelegramService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

@Component
public class StartCommandHandler extends UserRequestHandler {
    private static String command = "/start";

    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserRepository userRepository;


    public StartCommandHandler(TelegramService telegramService, KeyboardHelper keyboardHelper, UserRepository userRepository) {
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userRepository = userRepository;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(),command);
    }

    @Override
    public void handle(UserRequest request) {
        Long chatId = request.getChatId();
        List<User> users = userRepository.findAll();
        boolean check = false;
        for (User user : users) {
            if (chatId.equals(user.getChatId())) {
                check = true;
                break;
            }
        }
        if (!check) {
            User user = User.builder()
                    .firstName(request.getUpdate().getMessage().getFrom().getFirstName())
                    .lastName(request.getUpdate().getMessage().getFrom().getLastName())
                    .userName(request.getUpdate().getMessage().getFrom().getUserName())
                    .chatId(chatId)
                    .build();
            userRepository.save(user);
        }
        ReplyKeyboard replyKeyboard = keyboardHelper.buildMainMenu();
        telegramService.sendMessage(request.getChatId(),
                "\uD83D\uDC4BПривіт! За допомогою цього чат-бота ви зможете створити свої" +
                        " власні списки фільмів, серіалів які ви переглянули, плануєте переглянути, або переглядаєте.",
                replyKeyboard);
        telegramService.sendMessage(request.getChatId(),
                "Обирайте з меню нижче ⤵️");
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
