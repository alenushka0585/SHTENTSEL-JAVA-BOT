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


/**
 * Service class that handles bot operations such as processing user updates,
 * creating messages, and managing custom keyboards.
 */
@Service
@Getter
@Slf4j
public class BotService {
    @Autowired
    CurrencyService currencyService;

    /**
     * Extracts the text message from an incoming update.
     *
     * @param update the update received from a Telegram user.
     * @return the message as a string or an empty string if none is found.
     */
    public String getUpdateMessage(Update update) {
        String message = "";
        if (update.hasMessage() && update.getMessage().hasText()) {
            message = update.getMessage().getText();
        } else if (update.hasCallbackQuery()) {
            message = update.getCallbackQuery().getData();
        } else {
            log.info("Failed to get updateMessage");
        }

        return message;
    }

    /**
     * Extracts the chat ID from an incoming update.
     *
     * @param update the update received from a Telegram user.
     * @return the chat ID or null if none is found.
     */
    public Long getUpdateChatId(Update update) {
        Long chatId = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            ;
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else {
            log.info("Failed to get chatId");
        }

        return chatId;
    }

    /**
     * Extracts the username from an incoming update.
     *
     * @param update the update received from a Telegram user.
     * @return the username as a string or an empty string if none is found.
     */
    public String getUpdateUserName(Update update) {
        String userName = "";
        if (update.hasMessage() && update.getMessage().hasText()) {
            userName = update.getMessage().getChat().getUserName();
        } else if (update.hasCallbackQuery()) {
            userName = update.getCallbackQuery().getMessage().getChat().getUserName();
        } else {
            log.info("Failed to get UserName");
        }

        return userName;
    }

    /**
     * Creates a SendMessage object for sending a text message to a specified chat.
     *
     * @param chat_id the ID of the chat to which the message will be sent.
     * @param text    the text of the message to be sent.
     * @return a SendMessage object.
     */
    public SendMessage createMessage(long chat_id, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(text);

        return message;
    }

    /**
     * Checks the conditions of a received message and determines the appropriate command.
     *
     * @param message                    the text message from the user.
     * @param isBaseCurrency             a flag indicating if a base currency has been set.
     * @param baseCurrency               the base currency code.
     * @param isAvailableForSubscription a flag indicating if the user can subscribe.
     * @return the determined Command object.
     */
    public Command checkConditions(String message, boolean isBaseCurrency, String baseCurrency, boolean isAvailableForSubscription) {

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

    /**
     * Attaches an inline keyboard to a SendMessage object.
     *
     * @param message  the SendMessage object.
     * @param keyboard the InlineKeyboardMarkup to be attached.
     * @return the updated SendMessage object with the keyboard.
     */
    public SendMessage setKeyboard(SendMessage message, InlineKeyboardMarkup keyboard) {
        message.setReplyMarkup(keyboard);

        return message;
    }

    /**
     * Creates an inline keyboard with buttons representing different currency options.
     *
     * @return an InlineKeyboardMarkup object with currency buttons.
     */
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

    /**
     * Creates an inline keyboard with main menu options.
     *
     * @return an InlineKeyboardMarkup object representing the main menu.
     */
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

    /**
     * Creates a row of currency buttons.
     *
     * @param currencyCode1 the code for the first currency.
     * @param currencyCode2 the code for the second currency.
     * @param currencyCode3 the code for the third currency.
     * @param currencyCode4 the code for the fourth currency.
     * @return a list of InlineKeyboardButton objects.
     */
    @NotNull
    public List<InlineKeyboardButton> createCurrencyButtonRow(
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

    /**
     * Creates a row of buttons with custom callback data.
     *
     * @param button       the button text.
     * @param callbackData the callback data for the button.
     * @return a list of InlineKeyboardButton objects.
     */
    @NotNull
    public List<InlineKeyboardButton> createButtonsRow(
            String button, String callbackData) {
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

        buttonRow.add(createButton(button, callbackData));

        return buttonRow;
    }

    /**
     * Creates an inline keyboard button with a given name.
     *
     * @param buttonName the text for the button.
     * @return an InlineKeyboardButton object.
     */
    @NotNull
    public InlineKeyboardButton createButton(String buttonName) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonName);
        return button;
    }

    /**
     * Creates an inline keyboard button with custom callback data.
     *
     * @param buttonName   the text for the button.
     * @param callbackData the callback data for the button.
     * @return an InlineKeyboardButton object.
     */
    @NotNull
    public InlineKeyboardButton createButton(String buttonName, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(callbackData);
        return button;
    }
}
