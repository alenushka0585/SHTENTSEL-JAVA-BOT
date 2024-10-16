package de.tel_ran.shtentsel_java_telegrambot.config;

import de.tel_ran.shtentsel_java_telegrambot.command.ExchangeRatesBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Configuration
@Slf4j
public class BotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(ExchangeRatesBot exchangeRatesBot){
        TelegramBotsApi api = null;
        try {
            api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(exchangeRatesBot);
        } catch (TelegramApiException e) {
            log.info("Failed to register Bot", e);
        }

        return api;
    }
}




// TODO создать исключенние, обработать loggerom