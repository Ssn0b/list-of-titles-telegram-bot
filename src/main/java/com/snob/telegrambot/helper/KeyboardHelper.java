package com.snob.telegrambot.helper;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static com.snob.telegrambot.constant.Constant.BTN_CANCEL;

@Component
public class KeyboardHelper {
    public ReplyKeyboardMarkup buildListMenu() {
        List<KeyboardButton> buttons1 = List.of(
                new KeyboardButton("Переглянуте"),
                new KeyboardButton("Планую дивитись"));
        List<KeyboardButton> buttons2 = List.of(
                new KeyboardButton("Дивлюся"),
                new KeyboardButton("Улюблене"));
        KeyboardRow row1 = new KeyboardRow(buttons1);
        KeyboardRow row2 = new KeyboardRow(buttons2);
        KeyboardRow row3 = new KeyboardRow(List.of(new KeyboardButton(BTN_CANCEL)));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2,row3))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildMainMenu(){
        List<KeyboardButton> buttons = List.of(
                new KeyboardButton("Добавити в список➕"),
                new KeyboardButton("Показати список\uD83D\uDCCA"));
        KeyboardRow keyboardRow = new KeyboardRow(buttons);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup buildMenuWithCancel(){
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(BTN_CANCEL);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow))
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
}
