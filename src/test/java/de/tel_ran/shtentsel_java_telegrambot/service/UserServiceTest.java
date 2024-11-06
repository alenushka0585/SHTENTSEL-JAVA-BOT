package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.entity.Role;
import de.tel_ran.shtentsel_java_telegrambot.entity.RoleName;
import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import de.tel_ran.shtentsel_java_telegrambot.repository.RoleRepository;
import de.tel_ran.shtentsel_java_telegrambot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(roleRepository.findByRoleName(RoleName.USER.getRole())).thenReturn(role);
    }


    @Test
    void testGetOrSaveUser_NewUser() {
        String userName = "John Doe";
        Long chatId = 123L;
        when(userRepository.findByChatId(chatId)).thenReturn(null);

        User result = userService.getOrSaveUser(userName, chatId);

        assertNotNull(result);
        assertEquals(userName, result.getUserName());
        assertEquals(chatId, result.getChatId());
        assertTrue(result.getRoles().contains(role));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetOrSaveUser_ExistingUser() {
        Long chatId = 123L;
        User existingUser = new User();
        existingUser.setChatId(chatId);
        existingUser.setUserName("John Doe");
        when(userRepository.findByChatId(chatId)).thenReturn(existingUser);

        User result = userService.getOrSaveUser("John Doe", chatId);

        assertNotNull(result);
        assertEquals(existingUser, result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAddSubscriptionToUser_NewSubscription() {
        User user = new User();
        Set<Subscription> subscriptions = new HashSet<>();
        user.setSubscriptions(subscriptions);

        String subscriptionName = "USD:EUR";
        Subscription newSubscription = Subscription.builder()
                .subscriptionName(subscriptionName)
                .baseCurrency("USD")
                .requiredCurrency("EUR")
                .isActive(true)
                .build();

        when(subscriptionService.saveSubscriptionIfNotExist(subscriptionName)).thenReturn(newSubscription);

        userService.addSubscriptionToUser(user, subscriptionName);

        assertEquals(1, user.getSubscriptions().size());
        assertTrue(user.getSubscriptions().contains(newSubscription));
        verify(userRepository).save(user);
    }

    @Test
    void testGetAllUsersWithSubscriptions() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findBySubscriptionsIsNotEmpty()).thenReturn(users);

        List<User> result = userService.getAllUsersWithSubscriptions();

        assertEquals(users.size(), result.size());
        verify(userRepository).findBySubscriptionsIsNotEmpty();
    }

    @Test
    void testDeleteSubscriptionFromUser() {

        User user = new User();
        Set<Subscription> subscriptions = new HashSet<>();
        String subscriptionName = "USD:EUR";
        Subscription subscription = Subscription.builder()
                .subscriptionName(subscriptionName)
                .baseCurrency("USD")
                .requiredCurrency("EUR")
                .isActive(true)
                .build();
        subscriptions.add(subscription);
        user.setSubscriptions(subscriptions);

        when(subscriptionService.getSubscription(subscriptionName)).thenReturn(subscription);


        userService.deleteSubscriptionFromUser(user, subscriptionName);

        assertFalse(user.getSubscriptions().contains(subscription));
        verify(userRepository).save(user);
    }
}
