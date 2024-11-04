package de.tel_ran.shtentsel_java_telegrambot.command;

import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import de.tel_ran.shtentsel_java_telegrambot.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * The ExchangeRatesBot class represents a Telegram bot for providing currency exchange rates.
 * It handles incoming updates, manages user interactions, and sends scheduled updates for currency rates.
 */
@Component
@Slf4j
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

    @Autowired
    private AdminService adminService;

    @Value("${bot.name}")
    private String botName;

    /**
     * Constructor for the bot, initializing with the bot token.
     *
     * @param botToken The token for the Telegram bot.
     */
    public ExchangeRatesBot(
            @Value("${bot.token}") String botToken) {
        super(botToken);
    }

    /**
     * Handles incoming updates from Telegram.
     *
     * @param update The update received from Telegram.
     */
    @Override
    public void onUpdateReceived(Update update) {

        String message = botService.getUpdateMessage(update);
        Long chatId = botService.getUpdateChatId(update);
        String userName = botService.getUpdateUserName(update);
        User user = userService.getOrSaveUser(userName, chatId);
        boolean isAvailableForSubscription = subscriptionService
                .availableForSubscriptionCurrencies(message.toUpperCase().replaceAll("REMOVE\\(|\\)", ""));
        Command command = botService.checkConditions(message, isBaseCurrency, baseCurrency, isAvailableForSubscription);


        switch (command) {
            case START -> sendMessage(
                    botService.setKeyboard(
                            botService.createMessage(chatId, Message.START_MESSAGE.getText()), botService.menuKeyboard()
                    ), chatId
            );
            case RATE_MESSAGE -> sendMessage(
                    botService.setKeyboard(
                            botService.createMessage(chatId, Message.BASE_CURRENCY.getText()), botService.currencyKeyboard()
                    ), chatId
            );
            case SET_BASE_CURRENCY -> {
                baseCurrency = message;
                isBaseCurrency = true;
                sendMessage(botService.setKeyboard(
                                botService.createMessage(
                                        chatId, Message.REQUIRED_CURRENCY.getText()), botService.currencyKeyboard()
                        ), chatId
                );
            }
            case SET_REQUIRED_CURRENCY -> {
                isBaseCurrency = false;
                sendMessage(
                        botService.createMessage(
                                chatId, currencyService.getCurrencyRate(baseCurrency, message)
                        ), chatId
                );
            }
            case STOP -> sendMessage(
                    botService.createMessage(
                            chatId, Message.BYE_MESSAGE.getText()
                    ), chatId
            );
            case SUBSCRIBE_MESSAGE -> sendMessage(
                    botService.createMessage(
                            chatId, Message.SUBSCRIBE_MESSAGE.getText()
                    ), chatId
            );
            case SUBSCRIBE -> {
                userService.addSubscriptionToUser(user, message);
                sendMessage(botService.createMessage(chatId, Message.SUBSCRIBED.getText() + "\n" + message.toUpperCase()), chatId);
            }

            case UNSUBSCRIBE_MESSAGE -> sendMessage(
                    botService.createMessage(
                            chatId, Message.UNSUBSCRIBE_MESSAGE.getText()
                    ), chatId
            );
            case UNSUBSCRIBE -> {
                String subscription = message.toUpperCase().replaceAll("REMOVE\\(|\\)", "");
                userService.deleteSubscriptionFromUser(user, subscription);
                subscriptionService.setActivity(subscription);
                sendMessage(
                        botService.createMessage(
                                chatId, Message.UNSUBSCRIBED.getText() + "\n" + subscription)
                        , chatId);
            }

            case SUBSCRIPTIONS -> sendMessage(
                    botService.createMessage(
                            chatId, userService.getMessageWithSubscriptions(
                                    userService.getSubscriptions(user)
                            )
                    ), chatId
            );
            default -> {
                isBaseCurrency = false;
                sendMessage(botService.createMessage(
                                chatId, Message.UNKNOWN_COMMAND.getText()
                        ), chatId
                );
            }
        }
    }

    /**
     * Returns the username of the bot.
     *
     * @return The bot's username.
     */
    @Override
    public String getBotUsername() {
        return this.botName;
    }

    /**
     * Sends a message to the specified chat ID.
     *
     * @param sendMessage The message to be sent.
     * @param chatId      The chat ID of the recipient.
     */
    public void sendMessage(SendMessage sendMessage, Long chatId) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to user with chatId {}: {}", chatId, e.getMessage());
        }
    }

    /**
     * Sends scheduled updates for currency rates to users with active subscriptions.
     * This method runs at fixed intervals.
     */
    // @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(fixedRate = 300000)
    public void sendCurrencyUpdates() {
        userService.getAllUsersWithSubscriptions().stream()
                .parallel()  // Запускаем обработку пользователей в параллельных потоках
                .forEach(user -> {
                    user.getSubscriptions().stream()
                            .forEach(subscription -> {
                                String message = currencyService.getCurrencyRate(subscription.getBaseCurrency(), subscription.getRequiredCurrency());
                                sendMessage(botService.createMessage(user.getChatId(), message), user.getChatId());
                            });
                });
    }

    /**
     * Sends administrative information to users with admin roles at fixed intervals.
     */
    // @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(fixedRate = 300000)
    public void sendAdminInfo() {
        List<User> admins = userService.findByRolesRoleName(RoleName.ADMIN.getRole());

        admins.parallelStream()
                .forEach(admin ->
                        sendMessage(botService.createMessage(admin.getChatId(), adminService.messageForAdmin()), admin.getChatId()));

    }
}
