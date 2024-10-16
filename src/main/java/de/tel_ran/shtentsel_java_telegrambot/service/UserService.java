package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.entity.Role;
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

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    RoleRepository repository;

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

    @Transactional
    public Set<Subscription> getSubscriptions(User user) {
        Set<Subscription> subscriptions = user.getSubscriptions();
        if (subscriptions == null) {
            subscriptions = new HashSet<>();
            return subscriptions;
        }
        return subscriptions;
    }

    public List<User> getAllUsersWithSubscriptions (){
        return userRepository.findBySubscriptionsIsNotEmpty();
    }

    public List<User> findByRolesRoleName (String roleName){
        List<User> users = userRepository.findByRolesRoleName(roleName);
        if (users == null || users.isEmpty()){
            log.info("There are no users with role: " + roleName);
            return null;
        }
        return users;
    }

    @Transactional
    public void deleteSubscriptionFromUser(User user, String subscriptionName){
        Set<Subscription> subscriptions = getSubscriptions(user);
        Subscription subscription = subscriptionService.getSubscription(subscriptionName);
        subscriptions.remove(subscription);
        user.setSubscriptions(subscriptions);
        userRepository.save(user);
    }

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

    public Set<Role> getRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
            return roles;
        }
        return roles;
    }
}