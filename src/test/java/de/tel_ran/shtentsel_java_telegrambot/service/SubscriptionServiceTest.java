package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import de.tel_ran.shtentsel_java_telegrambot.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {
    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveSubscriptionIfNotExist_NewSubscription() {

        String subscriptionName = "USD:EUR";
        when(subscriptionRepository.findBySubscriptionName(subscriptionName.toUpperCase())).thenReturn(null);

        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subscription result = subscriptionService.saveSubscriptionIfNotExist(subscriptionName);

        assertNotNull(result);
        assertEquals("USD:EUR", result.getSubscriptionName());
        assertTrue(result.getIsActive());
        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void testSaveSubscriptionIfNotExist_ExistingSubscription() {

        String subscriptionName = "USD:EUR";
        Subscription existingSubscription = new Subscription();
        existingSubscription.setSubscriptionName(subscriptionName.toUpperCase());
        existingSubscription.setIsActive(false);
        when(subscriptionRepository.findBySubscriptionName(subscriptionName.toUpperCase())).thenReturn(existingSubscription);


        Subscription result = subscriptionService.saveSubscriptionIfNotExist(subscriptionName);

        assertNotNull(result);
        assertTrue(result.getIsActive());
        verify(subscriptionRepository).save(existingSubscription);
    }

    @Test
    void testAvailableForSubscriptionCurrencies_ValidCurrencies() {
        String subscriptionName = "USD:EUR";
        when(currencyService.isCurrencyExisted("USD")).thenReturn(true);
        when(currencyService.isCurrencyExisted("EUR")).thenReturn(true);

        boolean result = subscriptionService.availableForSubscriptionCurrencies(subscriptionName);

        assertTrue(result);
    }

    @Test
    void testAvailableForSubscriptionCurrencies_InvalidCurrencies() {
        String subscriptionName = "USD:EUR";
        when(currencyService.isCurrencyExisted("USD")).thenReturn(true);
        when(currencyService.isCurrencyExisted("EUR")).thenReturn(false);

        boolean result = subscriptionService.availableForSubscriptionCurrencies(subscriptionName);

        assertFalse(result);
    }

    @Test
    void testGetSubscription_SubscriptionExists() {
        String subscriptionName = "USD:EUR";
        Subscription existingSubscription = new Subscription();
        existingSubscription.setSubscriptionName(subscriptionName.toUpperCase());
        when(subscriptionRepository.findBySubscriptionName(subscriptionName.toUpperCase())).thenReturn(existingSubscription);

        Subscription result = subscriptionService.getSubscription(subscriptionName);

        assertNotNull(result);
        assertEquals(subscriptionName.toUpperCase(), result.getSubscriptionName());
    }

    @Test
    void testGetSubscription_SubscriptionDoesNotExist() {
        String subscriptionName = "USD:EUR";
        when(subscriptionRepository.findBySubscriptionName(subscriptionName.toUpperCase())).thenReturn(null);

        Subscription result = subscriptionService.getSubscription(subscriptionName);

        assertNull(result);
    }

    @Test
    void testSetActivity_SubscriptionWithNoUsers() {
        String subscriptionName = "USD:EUR";
        Subscription subscription = new Subscription();
        subscription.setIsActive(true);
        subscription.setUsers(Collections.emptySet());
        when(subscriptionRepository.findBySubscriptionName(subscriptionName)).thenReturn(subscription);

        subscriptionService.setActivity(subscriptionName);

        assertFalse(subscription.getIsActive());
        verify(subscriptionRepository).saveAndFlush(subscription);
    }

    @Test
    void testSetActivity_SubscriptionWithUsers() {
        String subscriptionName = "USD:EUR";
        Subscription subscription = new Subscription();
        subscription.setIsActive(true);
        subscription.setUsers(Collections.singleton(new User())); // Add a user
        when(subscriptionRepository.findBySubscriptionName(subscriptionName)).thenReturn(subscription);

        subscriptionService.setActivity(subscriptionName);

        assertTrue(subscription.getIsActive());
        verify(subscriptionRepository, never()).saveAndFlush(subscription);
    }

    @Test
    void testFindByIsActiveTrue() {
        Subscription activeSubscription = new Subscription();
        activeSubscription.setIsActive(true);
        when(subscriptionRepository.findByIsActiveTrue()).thenReturn(Collections.singletonList(activeSubscription));


        List<Subscription> result = subscriptionService.findByIsActiveTrue();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsActive());
    }
}
