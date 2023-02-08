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

    public ReplyKeyboardMarkup buildListMenuForAdd() {
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
        public InlineKeyboardMarkup buildOutputList(List<ListOfTitles> listOfTitles,int page) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            for (ListOfTitles listOfTitle : listOfTitles) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                List<InlineKeyboardButton> row = new ArrayList<>();
                if (listOfTitle.getMark() != 0) {
                    inlineKeyboardButton.setText(listOfTitle.getTitleName() + " | " + listOfTitle.getMark() + "⭐");
                } else {
                    inlineKeyboardButton.setText(listOfTitle.getTitleName());
                }
                inlineKeyboardButton.setCallbackData(listOfTitle.getTitleName());
                row.add(inlineKeyboardButton);
                rows.add(row);
            }
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("⬅");
            inlineKeyboardButton.setCallbackData("назад");
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton2.setText(String.valueOf(page+1));
            inlineKeyboardButton2.setCallbackData("сторінка");
            InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
            inlineKeyboardButton3.setText("➡");
            inlineKeyboardButton3.setCallbackData("вперед");
            row.add(inlineKeyboardButton);
            row.add(inlineKeyboardButton2);
            row.add(inlineKeyboardButton3);
            rows.add(row);

            inlineKeyboardMarkup.setKeyboard(rows);
            return inlineKeyboardMarkup;
        }

    public InlineKeyboardMarkup setMark() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(String.valueOf(i+1));
            inlineKeyboardButton.setCallbackData(String.valueOf(i+1));
            row1.add(inlineKeyboardButton);
        }
        for (int i = 5; i < 10; i++) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(String.valueOf(i+1));
            inlineKeyboardButton.setCallbackData(String.valueOf(i+1));
            row2.add(inlineKeyboardButton);
        }
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Пропустити");
        inlineKeyboardButton.setCallbackData("пропустити");
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(inlineKeyboardButton);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
