package de.tel_ran.shtentsel_java_telegrambot.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing various commands supported by the Telegram bot.
 * Each command has an associated string value that represents the user input.
 */
@Getter
@AllArgsConstructor
public enum Command {
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

    /**
     * Returns the corresponding Command enum for a given string input.
     *
     * @param text the command text received as user input.
     * @return the matching Command enum, or UNKNOWN if no match is found.
     */
    public static Command fromString(String text) {
        for (Command c : Command.values()) {
            if (c.getCommand().equalsIgnoreCase(text)) {
                return c;
            }
        }
        return UNKNOWN;
    }
}
