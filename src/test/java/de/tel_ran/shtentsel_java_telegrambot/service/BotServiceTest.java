package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.dto.Command;
import de.tel_ran.shtentsel_java_telegrambot.network.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class BotServiceTest {
    @InjectMocks
    private BotService botService;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private CallbackQuery callbackQuery;

    @Mock
    private Chat chat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUpdateMessageWithTextMessage() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("Hello");

        String result = botService.getUpdateMessage(update);

        assertEquals("Hello", result);
    }

    @Test
    void testGetUpdateMessageWithCallbackQuery() {
        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getData()).thenReturn("Callback Data");

        String result = botService.getUpdateMessage(update);

        assertEquals("Callback Data", result);
    }

    @Test
    void testGetUpdateChatIdWithTextMessage() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getChatId()).thenReturn(12345L);

        Long chatId = botService.getUpdateChatId(update);

        assertEquals(12345L, chatId);
    }

    @Test
    void testGetUpdateChatIdWithCallbackQuery() {
        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(67890L);

        Long chatId = botService.getUpdateChatId(update);

        assertEquals(67890L, chatId);
    }

    @Test
    void testGetUpdateUserNameWithTextMessage() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getChat()).thenReturn(chat);
        when(chat.getUserName()).thenReturn("USER1");

        String userName = botService.getUpdateUserName(update);

        assertEquals("USER1", userName);
    }

    @Test
    void testGetUpdateUserNameWithCallbackQuery() {
        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getMessage()).thenReturn(message);
        when(message.getChat()).thenReturn(chat);
        when(chat.getUserName()).thenReturn("USER1");

        String userName = botService.getUpdateUserName(update);

        assertEquals("USER1", userName);
    }
    @Test
    void testCreateMessage() {
        long chatId = 12345L;
        String text = "Test message";

        SendMessage sendMessage = botService.createMessage(chatId, text);

        assertEquals(Long.valueOf(chatId), Long.valueOf(sendMessage.getChatId()));
        assertEquals(text, sendMessage.getText());
    }

    @Test
    void testCheckConditionsSetBaseCurrency() {
        when(currencyService.isCurrencyExisted("EUR")).thenReturn(true);
        Command command = botService.checkConditions("EUR", false, null, false);
        assertEquals(Command.SET_BASE_CURRENCY, command);
    }

    @Test
    void testCheckConditionsSetRequiredCurrency() {
        when(currencyService.isCurrencyExisted("EUR")).thenReturn(true);
        Command command = botService.checkConditions("EUR", true, null, false);
        assertEquals(Command.SET_REQUIRED_CURRENCY, command);
    }

    @Test
    void testCheckConditionsSubscribe() {
        when(currencyService.isCurrencyExisted("GBP:KZT")).thenReturn(false);
        Command result = botService.checkConditions("GBP:KZT", false, "null", true);
        assertEquals(Command.SUBSCRIBE, result);
    }

    @Test
    void testCheckConditionsUnsubscribe() {
        Command command = botService.checkConditions("remove", true, "EUR", true);
        assertEquals(Command.UNSUBSCRIBE, command);
    }

    @Test
    void testCheckConditionsUnknownCommand() {
        Command command = botService.checkConditions(".efwp", false, null, false);
        assertEquals(Command.fromString("UNKNOWN"), command);
    }

    @Test
    void testSetKeyboard() {
        SendMessage message = new SendMessage();
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        SendMessage result = botService.setKeyboard(message, keyboard);

        assertNotNull(result);
        assertEquals(keyboard, result.getReplyMarkup());
    }

    @Test
    void testCurrencyKeyboard() {
        InlineKeyboardMarkup keyboard = botService.currencyKeyboard();

        assertNotNull(keyboard);
        assertEquals(2, keyboard.getKeyboard().size()); // Проверяем количество строк в клавиатуре
    }

    @Test
    void testMenuKeyboard() {
        InlineKeyboardMarkup keyboard = botService.menuKeyboard();

        assertNotNull(keyboard);
        assertEquals(5, keyboard.getKeyboard().size()); // Проверяем количество строк в меню
    }

    @Test
    void testCreateCurrencyButtonRow() {
        List<InlineKeyboardButton> buttonRow = botService.createCurrencyButtonRow("EUR", "USD", "JPY", "GBP");

        assertNotNull(buttonRow);
        assertEquals(4, buttonRow.size());
        assertEquals("EUR", buttonRow.get(0).getText());
        assertEquals("USD", buttonRow.get(1).getText());
        assertEquals("JPY", buttonRow.get(2).getText());
        assertEquals("GBP", buttonRow.get(3).getText());
    }

    @Test
    void testCreateButtonsRow() {
        String buttonText = "Test Button";
        String callbackData = "callback_data";

        List<InlineKeyboardButton> buttonRow = botService.createButtonsRow(buttonText, callbackData);

        assertNotNull(buttonRow, "The button row should not be null");
        assertEquals(1, buttonRow.size(), "The button row should contain 1 button");

        InlineKeyboardButton button = buttonRow.get(0);
        assertEquals(buttonText, button.getText(), "Button text should match");
        assertEquals(callbackData, button.getCallbackData(), "Callback data should match");
    }

    @Test
    void testCreateButton() {
        String buttonName = "Sample Button";

        InlineKeyboardButton button = botService.createButton(buttonName);

        assertNotNull(button, "Button should not be null");
        assertEquals(buttonName, button.getText(), "Button text should match input");
        assertEquals(buttonName, button.getCallbackData(), "Callback data should match input");
    }

    @Test
    void testCreateButtonWithCallbackData() {
        String buttonName = "Sample Button";
        String callbackData = "sample_callback";

        InlineKeyboardButton button = botService.createButton(buttonName, callbackData);

        assertNotNull(button, "Button should not be null");
        assertEquals(buttonName, button.getText(), "Button text should match input");
        assertEquals(callbackData, button.getCallbackData(), "Callback data should match input");
    }
}
