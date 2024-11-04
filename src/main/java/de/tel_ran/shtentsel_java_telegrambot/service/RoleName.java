package de.tel_ran.shtentsel_java_telegrambot.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing different roles for users of the Telegram bot.
 * These roles define the level of access or permissions assigned to a user.
 */
@Getter
@AllArgsConstructor
public enum RoleName {
    ADMIN("admin"),
    USER("user");
    private final String role;
}
