package de.tel_ran.shtentsel_java_telegrambot.repository;

import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findBySubscriptionName(String subscriptionName);
}
