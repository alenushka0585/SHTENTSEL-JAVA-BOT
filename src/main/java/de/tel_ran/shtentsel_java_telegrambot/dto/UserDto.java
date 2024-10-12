package de.tel_ran.shtentsel_java_telegrambot.dto;

import de.tel_ran.shtentsel_java_telegrambot.entity.Subscription;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class UserDto {
    private String userName;
    private Long chatId;
    private Set<Subscription> subscriptions = new HashSet<>();
}
