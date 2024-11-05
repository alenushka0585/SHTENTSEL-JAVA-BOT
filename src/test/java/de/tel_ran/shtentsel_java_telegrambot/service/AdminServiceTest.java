package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AdminServiceTest {
    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserService userService;

    @Mock
    private SubscriptionService subscriptionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMessageForAdmin_WithActiveSubscriptions() {
        // Arrange
        User user1 = new User(); // Настройте свойства пользователя
        User user2 = new User(); // Настройте свойства пользователя
        when(userService.findByRolesRoleName(RoleName.USER.getRole())).thenReturn(Arrays.asList(user1, user2));
        when(userService.getAllUsersWithSubscriptions()).thenReturn(Arrays.asList(user1));

        Subscription subscription1 = new Subscription();
        subscription1.setSubscriptionName("EUR:KZT");
        when(subscriptionService.findByIsActiveTrue()).thenReturn(Collections.singletonList(subscription1));

        // Act
        String result = adminService.messageForAdmin();

        // Assert
        String expectedMessage = Message.ACTIVE_SUBSCRIPTIONS.getText() +
                "EUR:KZT\n" +
                Message.GENERAL_NUMBER_OF_USERS.getText() + " 2\n" +
                Message.NUMBER_OF_USERS_WITH_SUBSCRIPTIONS.getText() + " 1";
        assertEquals(expectedMessage, result);
    }

    @Test
    public void testMessageForAdmin_NoActiveSubscriptions() {
        // Arrange
        when(userService.findByRolesRoleName(RoleName.USER.getRole())).thenReturn(Collections.emptyList());
        when(userService.getAllUsersWithSubscriptions()).thenReturn(Collections.emptyList());
        when(subscriptionService.findByIsActiveTrue()).thenReturn(Collections.emptyList());

        // Act
        String result = adminService.messageForAdmin();

        // Assert
        String expectedMessage = Message.ACTIVE_SUBSCRIPTIONS.getText() +
                "\n" +
                Message.GENERAL_NUMBER_OF_USERS.getText() + " 0\n" +
                Message.NUMBER_OF_USERS_WITH_SUBSCRIPTIONS.getText() + " 0";
        assertEquals(expectedMessage, result);
    }
}
