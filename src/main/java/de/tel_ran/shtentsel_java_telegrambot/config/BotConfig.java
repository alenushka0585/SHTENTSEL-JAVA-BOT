package de.tel_ran.shtentsel_java_telegrambot.config;

import de.tel_ran.shtentsel_java_telegrambot.command.ExchangeRatesBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Configuration class for setting up the Telegram bot.
 * This class defines the TelegramBotsApi bean and registers
 * the ExchangeRatesBot with the Telegram Bots API.
 */
@Configuration
@Slf4j
public class BotConfig {

    /**
     * Creates and registers the TelegramBotsApi with the ExchangeRatesBot.
     *
     * @param exchangeRatesBot the instance of the ExchangeRatesBot to register.
     * @return the TelegramBotsApi instance.
     */
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