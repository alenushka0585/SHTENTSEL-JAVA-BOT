package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.entity.Role;
import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import de.tel_ran.shtentsel_java_telegrambot.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SubscriptionService {
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    CurrencyService currencyService;

    @Transactional
    public Subscription saveSubscriptionIfNotExist(String subscriptionName) {
        Subscription subscription = subscriptionRepository.findBySubscriptionName(subscriptionName);
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
        }
        return subscription;
    }

    public boolean availableForSubscriptionCurrencies(String subscriptionName) {
        String[] name = subscriptionName.split(":");
        if (name.length == 2 &&
                currencyService.isCurrencyExisted(name[0]) &&
                currencyService.isCurrencyExisted(name[1])) {

            return true;
        }
        return false;
    }
}
