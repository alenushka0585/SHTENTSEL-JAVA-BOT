package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.entity.Role;
import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import de.tel_ran.shtentsel_java_telegrambot.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SubscriptionService subscriptionService;

    @Transactional
    public User saveUserIfNotExist(String userName, Long chatId) {
        User user = userRepository.findByChatId(chatId);
        if (user == null) {
            user = User.builder().userName(userName).chatId(chatId).build();
            Role role = Role.builder().roleName(RoleName.USER.getRole()).build();
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
        Hibernate.initialize(user.getSubscriptions());
        System.out.println(subscriptions.contains(subscription) + "____________________________________________________________");
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

    public Set<Role> getRoles(User user) {
        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
            return roles;
        }
        return roles;
    }
}
