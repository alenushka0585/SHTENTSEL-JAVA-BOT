package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.entity.Role;
import de.tel_ran.shtentsel_java_telegrambot.entity.RoleName;
import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import de.tel_ran.shtentsel_java_telegrambot.repository.RoleRepository;
import de.tel_ran.shtentsel_java_telegrambot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class for handling operations related to users.
 * This class includes methods for managing users, their roles, and their subscriptions.
 */
@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    RoleRepository repository;

    /**
     * Retrieves an existing user by chat ID or saves a new user if not found.
     *
     * @param userName The name of the user.
     * @param chatId   The chat ID of the user.
     * @return The existing or newly saved User object.
     */
    @Transactional
    public User getOrSaveUser(String userName, Long chatId) {
        User user = userRepository.findByChatId(chatId);
        Role role = repository.findByRoleName(RoleName.USER.getRole());
        if (user == null) {
            user = User.builder().userName(userName).chatId(chatId).build();
            Set<Role> roles = getRoles(user);
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        }
        return user;
    }

    /**
     * Adds a subscription to the user if not already present.
     *
     * @param user             The User object.
     * @param subscriptionName The name of the subscription in "BASE:REQUIRED" format.
     */
    @Transactional
    public void addSubscriptionToUser(User user, String subscriptionName) {
        Set<Subscription> subscriptions = getSubscriptions(user);
        Subscription subscription = subscriptionService.saveSubscriptionIfNotExist(subscriptionName);
        if (subscriptions.contains(subscription)) {
            return;
        }
        subscriptions.add(subscription);
        user.setSubscriptions(subscriptions);
        userRepository.save(user);
    }

    /**
     * Retrieves subscriptions of a user.
     *
     * @param user The User object.
     * @return A set of Subscription objects associated with the user.
     */
    @Transactional
    public Set<Subscription> getSubscriptions(User user) {
        Set<Subscription> subscriptions = user.getSubscriptions();
        if (subscriptions == null) {
            subscriptions = new HashSet<>();
            return subscriptions;
        }
        return subscriptions;
    }

    /**
     * Retrieves all users who have active subscriptions.
     *
     * @return A list of User objects with active subscriptions.
     */
    public List<User> getAllUsersWithSubscriptions() {
        return userRepository.findBySubscriptionsIsNotEmpty();
    }

    /**
     * Finds users by role name.
     *
     * @param roleName The name of the role.
     * @return A list of users with the specified role, or null if none found.
     */
    public List<User> findByRolesRoleName(String roleName) {
        List<User> users = userRepository.findByRolesRoleName(roleName);
        if (users == null || users.isEmpty()) {
            log.info("There are no users with role: " + roleName);
            return null;
        }
        return users;
    }

    /**
     * Deletes a subscription from a user.
     *
     * @param user             The User object.
     * @param subscriptionName The name of the subscription to be removed.
     */
    @Transactional
    public void deleteSubscriptionFromUser(User user, String subscriptionName) {
        Set<Subscription> subscriptions = getSubscriptions(user);
        Subscription subscription = subscriptionService.getSubscription(subscriptionName);
        subscriptions.remove(subscription);
        user.setSubscriptions(subscriptions);
        userRepository.save(user);
    }

    /**
     * Constructs a message containing the user's active subscriptions.
     *
     * @param subscriptions The set of subscriptions.
     * @return A formatted string message with subscription details.
     */
    public String getMessageWithSubscriptions(Set<Subscription> subscriptions) {
        String message = "У Вас нет активных подписок";
        String activeSubscriptions = "";
        if (subscriptions != null) {
            for (Subscription subscription : subscriptions) {
                if (subscription.getIsActive()) {
                    activeSubscriptions += subscription.getSubscriptionName() + "\n";
                }
            }
            if (activeSubscriptions.isEmpty()) {
                return message;
            } else {
                message = "Вы подписаны на: " + "\n" + activeSubscriptions;
            }

            return message;
        }

        return message;
    }

    /**
     * Retrieves roles assigned to a user.
     *
     * @param user The User object.
     * @return A set of Role objects associated with the user.
     */
    public Set<Role> getRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
            return roles;
        }
        return roles;
    }
}