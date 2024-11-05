package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.dto.CurrencyDto;
import de.tel_ran.shtentsel_java_telegrambot.entity.Currency;
import de.tel_ran.shtentsel_java_telegrambot.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CurrencyServiceTest {
    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CurrencyApi currencyApi;

    @Mock
    private Call<Map<String, CurrencyDto>> call;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyService = new CurrencyService(currencyApi, currencyRepository);

    }

    @Test
    void testGetCurrencyRate_SuccessfulResponse() throws IOException {
        Map<String, CurrencyDto> rates = new HashMap<>();
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setRate(1.123);
        currencyDto.setInverseRate(0.890);
        currencyDto.setDate("2024-11-01");
        rates.put("eur", currencyDto);

        when(currencyApi.getCurrencies("usd")).thenReturn(call);
        when(call.execute()).thenReturn(Response.success(rates));

        String result = currencyService.getCurrencyRate("USD", "EUR");

        assertNotNull(result, "Результат не должен быть null");
        assertTrue(result.contains("USD : EUR"), "Результат должен содержать 'USD : EUR'");
        assertTrue(result.contains("Курс: 1,123"), "Результат должен содержать 'Курс: 1,123'");
        assertTrue(result.contains("Обратный курс: 0,890"), "Результат должен содержать 'Обратный курс: 0,890'");
    }

    @Test
    void testIsCurrencyExisted_CurrencyExists() {
        Currency currency = new Currency();
        currency.setCode("usd");

        when(currencyRepository.findByCode("usd")).thenReturn(currency);

        System.out.println(currency);
        boolean exists = currencyService.isCurrencyExisted("usd");
        System.out.println(exists);
        assertTrue(exists);
    }


    @Test
    void testIsCurrencyExisted_CurrencyDoesNotExist() {
        when(currencyRepository.findByCode("EUR")).thenReturn(null);

        boolean exists = currencyService.isCurrencyExisted("EUR");

        assertFalse(exists);
    }
}
