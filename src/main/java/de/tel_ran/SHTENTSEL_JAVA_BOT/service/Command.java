package de.tel_ran.SHTENTSEL_JAVA_BOT.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Command {
    HELP("/help"),
    START("/start"),
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
