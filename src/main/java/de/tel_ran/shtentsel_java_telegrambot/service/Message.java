package de.tel_ran.shtentsel_java_telegrambot.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing various predefined messages for the Telegram bot.
 * These messages cover different interaction points with the user, including instructions,
 * status updates, and responses to commands.
 */
@Getter
@AllArgsConstructor
public enum Message {
    START_MESSAGE("""
            Добро пожаловать в бот курсов валют!
            Здесь Вы сможете:
            - узнать официальные курсы валют на сегодня;
            - подписаться на рассылку интересующих Вас курсов валют;
            - посмотреть свои подписки
                            
            """),

    BASE_CURRENCY("""
            Выберите БАЗОВУЮ валюту,
            либо введите код валюты самостоятельно:
                            
            """),
    REQUIRED_CURRENCY("""
            Выберите валюту,
            либо введите самостоятельно код валюты,
            курс которой желаете узнать:
                            
            """),
    SUBSCRIBE_MESSAGE("""
            Введите валютную пару,
            на которую желаете подписаться
            в следующем формате:
                            
            EUR:KZT
                            
            """),
    UNSUBSCRIBE_MESSAGE("""
            Введите валютную пару,
            от которой желаете отписаться
            в следующем формате:
                            
            remove(EUR:KZT)
                            
            """),

    SUBSCRIBED("""
            Вы успешно подписались на:
                          
            """),
    UNSUBSCRIBED("""
            Вы успешно отписались от:
                          
            """),
    BYE_MESSAGE("""
            Спасибо, что воспользовались нашим сервисом!
            Не забудьте подписаться на рассылку интересующих вас курсов валют!
                         
                            
            """),
    ACTIVE_SUBSCRIPTIONS("""
            Список активных подписок:
                         
            """),
    GENERAL_NUMBER_OF_USERS("""
            Количество пользователей составляет:
                         
            """),
    NUMBER_OF_USERS_WITH_SUBSCRIPTIONS("""
            Количество пользователей c подписками составляет:
                         
            """),
    UNKNOWN_COMMAND("""
            Не удалось распознать команду!
            Убедитесь в корректности написания команды!
            Для рестарта бота введите пожалуйста /start
            """);

    private final String text;

}
