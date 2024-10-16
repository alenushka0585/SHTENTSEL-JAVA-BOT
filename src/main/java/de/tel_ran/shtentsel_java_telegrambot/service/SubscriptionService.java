package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import de.tel_ran.shtentsel_java_telegrambot.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class SubscriptionService {
    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    CurrencyService currencyService;

    @Transactional
    public Subscription saveSubscriptionIfNotExist(String subscriptionName) {
        subscriptionName = subscriptionName.toUpperCase();
        Subscription subscription = getSubscription(subscriptionName);
        if (subscription == null) {
            String[] currencies = subscriptionName.split(":");
            Subscription newSubscription = Subscription
                    .builder()
                    .subscriptionName(subscriptionName)
                    .baseCurrency(currencies[0])
                    .requiredCurrency(currencies[1])
                    .isActive(true)
                    .build();
            return subscriptionRepository.save(newSubscription);
        } else {
            subscription.setIsActive(true);
            subscriptionRepository.save(subscription);
        }
        return subscription;
    }

    public boolean availableForSubscriptionCurrencies(String subscriptionName) {
        String[] name = subscriptionName.split(":");
        return name.length == 2 &&
                currencyService.isCurrencyExisted(name[0]) &&
                currencyService.isCurrencyExisted(name[1]);
    }

    @Transactional
    public Subscription getSubscription(String subscriptionName) {
        subscriptionName = subscriptionName.toUpperCase();
        Subscription subscription = subscriptionRepository.findBySubscriptionName(subscriptionName);
        if (subscription == null){
            log.info("There is no Subscription with the name: " + subscriptionName);
        }
        return subscription;
    }

    @Transactional
    public void setActivity(String subscriptionName) {
        Subscription subscription = subscriptionRepository.findBySubscriptionName(subscriptionName);
        if (subscription == null){
            log.info("There is no Subscription with the name: " + subscriptionName);
            return;
        }
        Set<User> users = subscription.getUsers();
        if (users == null || users.isEmpty()) {
            subscription.setIsActive(false);
            subscriptionRepository.saveAndFlush(subscription);
        }
    }

    public List<Subscription> findByIsActiveTrue() {
        return subscriptionRepository.findByIsActiveTrue();
    }

}
