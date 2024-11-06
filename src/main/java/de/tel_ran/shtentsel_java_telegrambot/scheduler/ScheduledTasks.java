package de.tel_ran.shtentsel_java_telegrambot.scheduler;

import de.tel_ran.shtentsel_java_telegrambot.controller.ExchangeRatesBot;
import de.tel_ran.shtentsel_java_telegrambot.entity.RoleName;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import de.tel_ran.shtentsel_java_telegrambot.network.CurrencyService;
import de.tel_ran.shtentsel_java_telegrambot.service.AdminService;
import de.tel_ran.shtentsel_java_telegrambot.service.BotService;
import de.tel_ran.shtentsel_java_telegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ScheduledTasks is a service that contains methods responsible for sending scheduled updates
 * to users of the Telegram bot. These updates include administrative messages and currency rate updates.
 *
 * This service leverages Spring's scheduling capabilities to periodically send messages to users,
 * such as administrative notifications and updates related to the exchange rates of subscribed currencies.
 * The methods are scheduled to run at fixed intervals or cron expressions as defined.
 */
@Service
public class ScheduledTasks {
    @Autowired
    private ExchangeRatesBot exchangeRatesBot;

    @Autowired
    private UserService userService;

    @Autowired
    private BotService botService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CurrencyService currencyService;


    /**
     * Sends administrative information to users with admin roles at fixed intervals.
     */
    // @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(fixedRate = 300000)
    public void sendAdminInfo() {
        List<User> admins = userService.findByRolesRoleName(RoleName.ADMIN.getRole());

        admins.parallelStream()
                .forEach(admin ->
                        exchangeRatesBot.sendMessage(botService.createMessage(admin.getChatId(), adminService.messageForAdmin()), admin.getChatId()));

    }

    /**
     * Sends scheduled updates for currency rates to users with active subscriptions.
     * This method runs at fixed intervals.
     */
    // @Scheduled(cron = "0 0 8 * * ?")
    @Scheduled(fixedRate = 150000)
    public void sendCurrencyUpdates() {
        userService.getAllUsersWithSubscriptions().stream()
                .parallel()  // Запускаем обработку пользователей в параллельных потоках
                .forEach(user -> {
                    user.getSubscriptions().stream()
                            .forEach(subscription -> {
                                String message = currencyService.getCurrencyRate(subscription.getBaseCurrency(), subscription.getRequiredCurrency());
                                exchangeRatesBot.sendMessage(botService.createMessage(user.getChatId(), message), user.getChatId());
                            });
                });
    }
}
