package com.snob.telegrambot.handler;

import com.snob.telegrambot.model.tools.UserRequest;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class UserRequestHandler {

    public abstract boolean isApplicable(UserRequest request);
    public abstract void handle(UserRequest dispatchRequest);
    public abstract boolean isGlobal();

    public boolean isCommand(Update update,String command){
        return update.hasMessage() && update.getMessage().isCommand()
                && update.getMessage().getText().equals(command);
    }

    public boolean isTextMessage(Update update){
        return update.hasMessage() && update.getMessage().hasText();
    }

    public boolean isTextMessage(Update update,String text){
        return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals(text);
    }

    public boolean isCallbackQuery(Update update){return update.hasCallbackQuery();}
    public boolean isCallbackQuery(Update update,String text){return update.hasCallbackQuery() &&
            update.getCallbackQuery().getData().equals(text);}

}
