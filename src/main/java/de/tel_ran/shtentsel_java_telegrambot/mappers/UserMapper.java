package de.tel_ran.shtentsel_java_telegrambot.mappers;

import de.tel_ran.shtentsel_java_telegrambot.dto.UserDto;
import de.tel_ran.shtentsel_java_telegrambot.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "chatId", source = "user.chatId")
    @Mapping(target = "subscriptions ", source = "user.subscriptions ")
    UserDto toDto(User user);
}
