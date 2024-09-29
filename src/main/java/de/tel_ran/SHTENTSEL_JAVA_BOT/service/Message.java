package de.tel_ran.SHTENTSEL_JAVA_BOT.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {
    BASE_CURRENCY("""
                Добро пожаловать в бот курсов валют!
                Здесь Вы сможете узнать официальные курсы валют на сегодня.
                
                Выберите БАЗОВУЮ валюту:
                
                """),
    REQUIRED_CURRENCY("""
                Выберите валюту курс которой желаете узнать:
                
                """),
    UNKNOWN_COMMAND("""
            Не удалось распознать команду!
            Для рестарта бота введите пожалуйста /start
            """);

    private final String text;

}
