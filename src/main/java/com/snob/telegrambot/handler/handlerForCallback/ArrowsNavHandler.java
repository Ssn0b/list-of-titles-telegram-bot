package com.snob.telegrambot.handler.handlerForCallback;

import com.snob.telegrambot.enums.ConversationState;
import com.snob.telegrambot.handler.UserRequestHandler;
import com.snob.telegrambot.helper.KeyboardHelper;
import com.snob.telegrambot.model.ListOfTitles;
import com.snob.telegrambot.model.User;
import com.snob.telegrambot.model.tools.UserRequest;
import com.snob.telegrambot.repository.ListOfTitlesRepository;
import com.snob.telegrambot.repository.UserRepository;
import com.snob.telegrambot.service.TelegramService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Objects;

import static com.snob.telegrambot.constant.Constant.ALL_LIST;

@Component
public class ArrowsNavHandler extends UserRequestHandler {
    private final UserRepository userRepository;
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final ListOfTitlesRepository usersRepository;


    public ArrowsNavHandler(UserRepository userRepository, TelegramService telegramService, KeyboardHelper keyboardHelper, ListOfTitlesRepository usersRepository) {
        this.userRepository = userRepository;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean isApplicable(UserRequest request) {
        return isCallbackQuery(request.getUpdate())
                && ConversationState.WAITING_FOR_CATEGORY_IN_SELECT.equals(request.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest dispatchRequest) {
        User user = userRepository.findUserById(dispatchRequest.getChatId());
        if (Objects.equals(dispatchRequest.getUpdate().getCallbackQuery().getData(), "назад")) {
            if (user.getPage() != 0) {
                userRepository.updatePage(user.getPage()-1,dispatchRequest.getChatId());
            }else {
                userRepository.updatePage(((int)
                        Math.ceil(dispatchRequest.getUserSession().getMaxList() * 1.0 / 5))-1,dispatchRequest.getChatId());
            }
        }else if (Objects.equals(dispatchRequest.getUpdate().getCallbackQuery().getData(), "вперед")){
            if (user.getPage() >= ((int) Math.ceil(dispatchRequest.getUserSession().getMaxList() * 1.0 / 5))-1){
                userRepository.updatePage(0,dispatchRequest.getChatId());
            }else if (user.getPage() >= ((int) Math.ceil(dispatchRequest.getUserSession().getMaxList() * 1.0 / 5))){
                userRepository.updatePage(0,dispatchRequest.getChatId());
            }
            else {
                userRepository.updatePage(user.getPage()+1,dispatchRequest.getChatId());
            }
        }
        user = userRepository.findUserById(dispatchRequest.getChatId());
        Pageable page = PageRequest.of(user.getPage(), 5);
        List<ListOfTitles> listOfTitles;
        if (Objects.equals(dispatchRequest.getUserSession().getCategory(), ALL_LIST)){
            listOfTitles = usersRepository.findAllByUser(dispatchRequest.getChatId(),page);
        }else {
            listOfTitles = usersRepository.findAllByUserAndCategory(dispatchRequest.getChatId(),
                    dispatchRequest.getUserSession().getCategory(),page);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = keyboardHelper.buildOutputList(listOfTitles,user.getPage());
        telegramService.sendMessage(dispatchRequest.getChatId(), "Ваш список: ",inlineKeyboardMarkup);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
