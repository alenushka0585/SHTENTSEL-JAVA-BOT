package de.tel_ran.SHTENTSEL_JAVA_BOT.service;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class BotService {

    public SendMessage createMessage(long chat_id, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(text);
        message.setReplyMarkup(createKeyboard());

        return message;
    }

    public InlineKeyboardMarkup createKeyboard(){
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> buttonRow1 = createButtonRow("EUR", "USD", "JPY", "GBP");
        List<InlineKeyboardButton> buttonRow2 = createButtonRow("AUD", "RUB", "KZT", "UAH");

        rows.add(buttonRow1);
        rows.add(buttonRow2);

        keyboard.setKeyboard(rows);
        return  keyboard;
    }

    @NotNull
    private List<InlineKeyboardButton> createButtonRow(
            String currencyCode1,
            String currencyCode2,
            String currencyCode3,
            String currencyCode4)
    {
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

        buttonRow.add(createButton(currencyCode1));
        buttonRow.add(createButton(currencyCode2));
        buttonRow.add(createButton(currencyCode3));
        buttonRow.add(createButton(currencyCode4));

        return buttonRow;
    }

    @NotNull
    public InlineKeyboardButton createButton(String currencyCode) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(currencyCode);
        button.setCallbackData(currencyCode);
        return button;
    }
}
