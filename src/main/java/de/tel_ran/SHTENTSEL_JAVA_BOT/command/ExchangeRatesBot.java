package de.tel_ran.SHTENTSEL_JAVA_BOT.command;

import de.tel_ran.SHTENTSEL_JAVA_BOT.dto.CurrencyDto;
import de.tel_ran.SHTENTSEL_JAVA_BOT.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ExchangeRatesBot extends TelegramLongPollingBot {

    private boolean isBaseCurrency;
    private String baseCurrency = "";

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyDto currencyDto;

    @Autowired
    private BotService botService;

    @Value("${bot.name}")
    private String botName;

    public ExchangeRatesBot(
            @Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            Command command = Command.fromString(message);

            switch (command) {
                case START -> sendMessage(botService.createMessage(chatId, Message.BASE_CURRENCY.getText()));
                default -> {
                    isBaseCurrency = false;
                    sendMessage(chatId, Message.UNKNOWN_COMMAND.getText());
                }
            }

        } else if (update.hasCallbackQuery() && !isBaseCurrency) {
            baseCurrency = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            isBaseCurrency = true;

            if (currencyService.isCurrencyExisted(baseCurrency)) {
                sendMessage(botService.createMessage(chatId, Message.REQUIRED_CURRENCY.getText()));
            }

        } else if (update.hasCallbackQuery() && isBaseCurrency) {
            String requiredCurrency = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            isBaseCurrency = false;
            sendMessage(chatId, currencyService.getCurrencyRate(baseCurrency, requiredCurrency));
        }
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    public void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
        }
    }

    public void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
        }
    }

    //TODO обработать исключение
}
