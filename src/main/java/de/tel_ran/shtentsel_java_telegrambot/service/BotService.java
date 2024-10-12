package de.tel_ran.shtentsel_java_telegrambot.service;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class BotService {

    public String getUpdateMessage(Update update){
        String message = "";
        if (update.hasMessage() && update.getMessage().hasText()) {
            message = update.getMessage().getText();
        } else if (update.hasCallbackQuery()){
            message = update.getCallbackQuery().getData();
        }

        return message;
    }

    public Long getUpdateChatId(Update update){
        Long chatId = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();;
        } else if (update.hasCallbackQuery()){
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        return chatId;
    }

    public String getUpdateUserName(Update update){
        String userName = "";
        if (update.hasMessage() && update.getMessage().hasText()) {
            userName = update.getMessage().getChat().getUserName();
        } else if (update.hasCallbackQuery()){
            userName = update.getCallbackQuery().getMessage().getChat().getUserName();
        }

        return userName;
    }

    public SendMessage createMessage(long chat_id, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(text);

        return message;
    }

    public SendMessage setKeyboard(SendMessage message, InlineKeyboardMarkup keyboard) {
        message.setReplyMarkup(keyboard);

        return message;
    }

    public InlineKeyboardMarkup currencyKeyboard() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> buttonRow1 = createCurrencyButtonRow("EUR", "USD", "JPY", "GBP");
        List<InlineKeyboardButton> buttonRow2 = createCurrencyButtonRow("AUD", "RUB", "KZT", "UAH");

        rows.add(buttonRow1);
        rows.add(buttonRow2);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    public InlineKeyboardMarkup menuKeyboard() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> buttonRow1 = createButtonsRow("Посмотреть курс", Command.RATE.getCommand());
        List<InlineKeyboardButton> buttonRow2 = createButtonsRow("Подписаться", Command.SUBSCRIBE.getCommand());
        List<InlineKeyboardButton> buttonRow3 = createButtonsRow("Показать мои подписки", Command.SUBSCRIPTIONS.getCommand());
        List<InlineKeyboardButton> buttonRow4 = createButtonsRow("Остановить бот", Command.STOP.getCommand());


        rows.add(buttonRow1);
        rows.add(buttonRow2);
        rows.add(buttonRow3);
        rows.add(buttonRow4);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    @NotNull
    private List<InlineKeyboardButton> createCurrencyButtonRow(
            String currencyCode1,
            String currencyCode2,
            String currencyCode3,
            String currencyCode4) {
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

        buttonRow.add(createButton(currencyCode1));
        buttonRow.add(createButton(currencyCode2));
        buttonRow.add(createButton(currencyCode3));
        buttonRow.add(createButton(currencyCode4));

        return buttonRow;
    }

    @NotNull
    private List<InlineKeyboardButton> createButtonsRow(
            String button, String callbackData) {
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

        buttonRow.add(createButton(button, callbackData));

        return buttonRow;
    }

    @NotNull
    public InlineKeyboardButton createButton(String buttonName) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonName);
        return button;
    }

    @NotNull
    public InlineKeyboardButton createButton(String buttonName, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(callbackData);
        return button;
    }

}
