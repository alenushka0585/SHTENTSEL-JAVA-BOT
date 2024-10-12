package de.tel_ran.shtentsel_java_telegrambot.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
    HELP("/help"),
    START("/start"),
    STOP("/stop"),
    RATE("/rate"),
    SUBSCRIBE("/subscribe"),
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
