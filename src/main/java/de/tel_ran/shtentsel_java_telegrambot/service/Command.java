package de.tel_ran.shtentsel_java_telegrambot.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
    HELP("/help"),
    START("/start"),
    STOP("/stop"),
    RATE_MESSAGE("/rateMessage"),
    SET_BASE_CURRENCY("/setBaseCurrency"),
    SET_REQUIRED_CURRENCY("/setRequiredCurrency"),
    SUBSCRIBE_MESSAGE("/subscribeMessage"),
    SUBSCRIBE("/subscribe"),
    UNSUBSCRIBE("/unsubscribe"),
    UNSUBSCRIBE_MESSAGE("/unsubscribeMessage"),
    SUBSCRIPTIONS("/subscriptions"),
    UNKNOWN("");
    private final String command;

    public static Command fromString(String text) {
        for (Command c : Command.values()) {
            if (c.getCommand().equalsIgnoreCase(text)) {
                return c;
            }
        }
        return UNKNOWN;
    }
}
