package de.tel_ran.shtentsel_java_telegrambot.repository;

import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findBySubscriptionName(String subscriptionName);

    List<Subscription> findByIsActiveTrue();
}
