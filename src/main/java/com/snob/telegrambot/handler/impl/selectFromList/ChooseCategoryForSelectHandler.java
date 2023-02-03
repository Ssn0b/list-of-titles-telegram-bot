package com.snob.telegrambot.handler.impl.selectFromList;

import com.snob.telegrambot.handler.UserRequestHandler;
import com.snob.telegrambot.model.UserRequest;
import org.springframework.stereotype.Component;

import static com.snob.telegrambot.constant.Constant.ADD_TO_LIST;
import static com.snob.telegrambot.constant.Constant.SELECT_FROM_LIST;

@Component
public class ChooseCategoryForSelectHandler extends UserRequestHandler {
    @Override
    public boolean isApplicable(UserRequest request) {
        return isTextMessage(request.getUpdate(), SELECT_FROM_LIST);
    }

    @Override
    public void handle(UserRequest dispatchRequest) {

    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
