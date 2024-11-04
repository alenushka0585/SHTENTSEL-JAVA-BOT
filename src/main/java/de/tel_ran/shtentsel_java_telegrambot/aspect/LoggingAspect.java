package de.tel_ran.shtentsel_java_telegrambot.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The LoggingAspect class provides aspect-oriented logging for methods
 * in the ExchangeRatesBot and BotService classes. It logs method names,
 * return values, and method arguments for better traceability and debugging.
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Value("${logging.tag:ASPECT}")
    private String tag;

    /**
     * Logs method execution details for the ExchangeRatesBot class after
     * a method successfully returns.
     *
     * @param point       The join point of the method being executed.
     * @param returnValue The return value of the method.
     */
    @AfterReturning(
            value = "execution(* de.tel_ran.shtentsel_java_telegrambot.command.ExchangeRatesBot..*(..))",
            returning = "returnValue"
    )
    public void afterExchangeRatesBot(
            JoinPoint point, Object returnValue
    ) {
        Object[] args = point.getArgs();
        log.info("{} method: {}", tag, point.getSignature().getName());
        log.info("{} return value: {}", tag, returnValue);
        for (Object o : args) {
            log.info("{} arg: {}", tag, o);
        }
    }

    /**
     * Logs method execution details for the BotService class after
     * a method successfully returns.
     *
     * @param point       The join point of the method being executed.
     * @param returnValue The return value of the method.
     */
    @AfterReturning(
            value = "execution(* de.tel_ran.shtentsel_java_telegrambot.service.BotService..*(..))",
            returning = "returnValue"
    )
    public void afterBotService(
            JoinPoint point, Object returnValue
    ) {
        log.info("{} method: {}", tag, point.getSignature().getName());
        log.info("{} return value: {}", tag, returnValue);
    }
}
