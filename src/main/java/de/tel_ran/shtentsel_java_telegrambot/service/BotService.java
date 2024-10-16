package de.tel_ran.shtentsel_java_telegrambot.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Slf4j
public class BotService {
    @Autowired
    CurrencyService currencyService;

    public String getUpdateMessage(Update update){
        String message = "";
        if (update.hasMessage() && update.getMessage().hasText()) {
            message = update.getMessage().getText();
        } else if (update.hasCallbackQuery()){
            message = update.getCallbackQuery().getData();
        } else {
            log.info("Failed to get updateMessage");
        }

        return message;
    }

    public Long getUpdateChatId(Update update){
        Long chatId = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();;
        } else if (update.hasCallbackQuery()){
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else {
            log.info("Failed to get chatId");
        }

        return chatId;
    }

    public String getUpdateUserName(Update update){
        String userName = "";
        if (update.hasMessage() && update.getMessage().hasText()) {
            userName = update.getMessage().getChat().getUserName();
        } else if (update.hasCallbackQuery()){
            userName = update.getCallbackQuery().getMessage().getChat().getUserName();
        } else {
            log.info("Failed to get UserName");
        }

        return userName;
    }

    public SendMessage createMessage(long chat_id, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(text);

        return message;
    }

    public Command checkConditions(String message, boolean isBaseCurrency, String baseCurrency, boolean isAvailableForSubscription){

            if (currencyService.isCurrencyExisted(message) && !isBaseCurrency) {
                return Command.SET_BASE_CURRENCY;
            } else if (currencyService.isCurrencyExisted(message) && isBaseCurrency) {
                return Command.SET_REQUIRED_CURRENCY;
            } else if (isAvailableForSubscription && !message.toUpperCase().contains("REMOVE")) {
                return Command.SUBSCRIBE;

            } else if (message.toUpperCase().contains("REMOVE") && isAvailableForSubscription) {
                return Command.UNSUBSCRIBE;
            } else {
                return Command.fromString(message);
            }
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

        List<InlineKeyboardButton> buttonRow1 = createButtonsRow("Посмотреть курс", Command.RATE_MESSAGE.getCommand());
        List<InlineKeyboardButton> buttonRow2 = createButtonsRow("Подписаться", Command.SUBSCRIBE_MESSAGE.getCommand());
        List<InlineKeyboardButton> buttonRow3 = createButtonsRow("Показать мои подписки", Command.SUBSCRIPTIONS.getCommand());
        List<InlineKeyboardButton> buttonRow4 = createButtonsRow("Отписаться", Command.UNSUBSCRIBE_MESSAGE.getCommand());
        List<InlineKeyboardButton> buttonRow5 = createButtonsRow("Остановить бот", Command.STOP.getCommand());


        rows.add(buttonRow1);
        rows.add(buttonRow2);
        rows.add(buttonRow3);
        rows.add(buttonRow4);
        rows.add(buttonRow5);

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
