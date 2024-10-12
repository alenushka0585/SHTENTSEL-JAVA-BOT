package de.tel_ran.shtentsel_java_telegrambot.command;

import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import de.tel_ran.shtentsel_java_telegrambot.service.*;
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
    private BotService botService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Value("${bot.name}")
    private String botName;

    public ExchangeRatesBot(
            @Value("${bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {

        String message = botService.getUpdateMessage(update);
        Long chatId = botService.getUpdateChatId(update);
        String userName = botService.getUpdateUserName(update);
        Command command = Command.fromString(message);
        userService.saveUserIfNotExist(botService.getUpdateUserName(update), botService.getUpdateChatId(update));


        switch (command) {
            case START -> sendMessage(
                    botService.setKeyboard(
                            botService.createMessage(chatId, Message.START_MESSAGE.getText()), botService.menuKeyboard()
                    )
            );
            case RATE -> sendMessage(
                    botService.setKeyboard(
                            botService.createMessage(chatId, Message.BASE_CURRENCY.getText()), botService.currencyKeyboard()
                    )
            );
            case STOP -> sendMessage(
                    botService.createMessage(
                            chatId, Message.BYE_MESSAGE.getText()
                    )
            );
            case SUBSCRIBE -> {
                sendMessage(
                        botService.createMessage(
                                chatId, Message.SUBSCRIBE_MESSAGE.getText()
                        )
                );
            }
            case SUBSCRIPTIONS -> sendMessage(
                    botService.createMessage(
                            chatId, "Не забудь добавить список подписок!!!"
                    )
            );
            default -> {
                if (currencyService.isCurrencyExisted(message) && !isBaseCurrency) {
                    baseCurrency = message;
                    isBaseCurrency = true;
                    sendMessage(botService.setKeyboard(
                                    botService.createMessage(
                                            chatId, Message.REQUIRED_CURRENCY.getText()), botService.currencyKeyboard()
                            )
                    );
                } else if (currencyService.isCurrencyExisted(message) && isBaseCurrency) {
                    isBaseCurrency = false;
                    sendMessage(botService.createMessage(chatId, currencyService.getCurrencyRate(baseCurrency, message)));
                } else if (subscriptionService.availableForSubscriptionCurrencies(message)) {
                   User user = userService.saveUserIfNotExist(userName, chatId);
                   userService.addSubscriptionToUser(user, message);
                    sendMessage(botService.createMessage(chatId, Message.SUBSCRIBED.getText() + "\n" + message));

                } else {
                    isBaseCurrency = false;
                    sendMessage(botService.createMessage(
                                    chatId, Message.UNKNOWN_COMMAND.getText()
                            )
                    );
                }
            }
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

//    public void sendMessage(Long chatId, String text) {
//        var chatIdStr = String.valueOf(chatId);
//        var sendMessage = new SendMessage(chatIdStr, text);
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//        }
//    }


//TODO обработать исключение
}
