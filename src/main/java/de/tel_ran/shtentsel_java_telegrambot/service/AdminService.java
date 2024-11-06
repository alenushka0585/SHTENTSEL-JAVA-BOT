package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.dto.Message;
import de.tel_ran.shtentsel_java_telegrambot.entity.RoleName;
import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Service class for managing administrative operations related to users and subscriptions.
 */
@Service
public class AdminService {
    @Autowired
    UserService userService;

    @Autowired
    SubscriptionService subscriptionService;

    /**
     * Generates a message for the admin containing information about users and active subscriptions.
     *
     * @return a formatted string containing:
     * - The names of all active subscriptions.
     * - The total number of users.
     * - The number of users with active subscriptions.
     */
    public String messageForAdmin() {

        List<User> users = userService.findByRolesRoleName(RoleName.USER.getRole());
        List<User> usersWithSubscribes = userService.getAllUsersWithSubscriptions();
        long amountOfUsers = users.size();
        long amountOfUsersWithSubscriptions = usersWithSubscribes.size();

        List<Subscription> activeSubscriptions = subscriptionService.findByIsActiveTrue();


        String subscriptionsMessage = activeSubscriptions.stream()
                .map(Subscription::getSubscriptionName)
                .collect(Collectors.joining("\n"));

        String finalMessage = Message.ACTIVE_SUBSCRIPTIONS.getText() +
                subscriptionsMessage + "\n" +
                Message.GENERAL_NUMBER_OF_USERS.getText() + " " + amountOfUsers + "\n" +
                Message.NUMBER_OF_USERS_WITH_SUBSCRIPTIONS.getText() + " " + amountOfUsersWithSubscriptions;


        return finalMessage;
    }
}
