package com.snob.telegrambot.helper;

import com.snob.telegrambot.model.ListOfTitles;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.snob.telegrambot.constant.Constant.*;

@Component
public class KeyboardHelper {
    public ReplyKeyboardMarkup buildListMenu() {
        List<KeyboardButton> buttons1 = List.of(
                new KeyboardButton("Переглянуте"),
                new KeyboardButton("Планую дивитись"));
        List<KeyboardButton> buttons2 = List.of(
                new KeyboardButton("Дивлюся"),
                new KeyboardButton("Улюблене"));
        List<KeyboardButton> buttons3 = List.of(new KeyboardButton("Вивести все"));
        KeyboardRow row1 = new KeyboardRow(buttons1);
        KeyboardRow row2 = new KeyboardRow(buttons2);
        KeyboardRow row3 = new KeyboardRow(buttons3);
        KeyboardRow row4 = new KeyboardRow(List.of(new KeyboardButton(BTN_CANCEL)));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2,row3,row4))
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
        public InlineKeyboardMarkup buildOutputList(List<ListOfTitles> listOfTitles) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            for (int i = 0; i < listOfTitles.size(); i++) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                List<InlineKeyboardButton> row = new ArrayList<>();
                inlineKeyboardButton.setText(listOfTitles.get(i).getTitleName());
                inlineKeyboardButton.setCallbackData(listOfTitles.get(i).getTitleName());
                row.add(inlineKeyboardButton);
                rows.add(row);
            }


            inlineKeyboardMarkup.setKeyboard(rows);
            return inlineKeyboardMarkup;
    }
}
