package de.tel_ran.shtentsel_java_telegrambot.network;

import de.tel_ran.shtentsel_java_telegrambot.dto.CurrencyDto;
import de.tel_ran.shtentsel_java_telegrambot.entity.Currency;
import de.tel_ran.shtentsel_java_telegrambot.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for handling currency rate operations.
 * Uses Retrofit to fetch data from an external API and interacts with the repository for currency data management.
 */
@Service
@Slf4j
public class CurrencyService {
    private static final String BASE_URL = "https://www.floatrates.com/daily/";

    private final CurrencyApi currencyApi;
    @Autowired
    CurrencyRepository currencyRepository;

    /**
     * Constructor for initializing Retrofit instance and setting up the currency API interface.
     */
    public CurrencyService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        currencyApi = retrofit.create(CurrencyApi.class);
    }

    /**
     * Constructor for JUnit tests.
     */
    public CurrencyService(CurrencyApi currencyApi, CurrencyRepository currencyRepository) {
        this.currencyApi = currencyApi;
        this.currencyRepository = currencyRepository;
    }

    /**
     * Retrieves the exchange rate between the base currency and the required currency.
     *
     * @param baseCurrency     the base currency code (e.g., "USD").
     * @param requiredCurrency the target currency code (e.g., "EUR").
     * @return a formatted string containing the exchange rate details, or an error message if retrieval fails.
     */
    public String getCurrencyRate(
            String baseCurrency,
            String requiredCurrency) {
        Call<Map<String, CurrencyDto>> call = currencyApi.getCurrencies(baseCurrency.toLowerCase());
        CurrencyDto currencyDto = new CurrencyDto();
        try {
            Response<Map<String, CurrencyDto>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                Map<String, CurrencyDto> rates = response.body();
                currencyDto = rates.get(requiredCurrency.toLowerCase());
            }
        } catch (IOException e) {
            log.info("Failed to get CurrencyRate json file", e);
        }

        return currencyDto.getDate() + "\n" +
                baseCurrency.toUpperCase() + " : " + requiredCurrency.toUpperCase() + "\n" +
                "Курс: " + String.format("%.3f", currencyDto.getRate()) + "\n" +
                "Обратный курс: " + String.format("%.3f", currencyDto.getInverseRate());
    }

    /**
     * Retrieves a map of all currency rates based on the provided base currency.
     *
     * @param baseCurrency the base currency code.
     * @return a map containing currency codes as keys and corresponding {@link CurrencyDto} as values.
     */
    // TODO Метод создавался для заполнения базы данных списком доступных валют, удалить???
    public Map<String, CurrencyDto> getCurrencyRates(
            String baseCurrency) {
        Call<Map<String, CurrencyDto>> call = currencyApi.getCurrencies(baseCurrency.toLowerCase());
        Map<String, CurrencyDto> rates = new HashMap<>();
        try {
            Response<Map<String, CurrencyDto>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                rates = response.body();

            }
        } catch (IOException e) {
            log.info("Failed to get CurrencyRate json file", e);
        }
        return rates;
    }

    /**
     * Checks if the provided currency code exists in the database.
     *
     * @param currency the currency code to check.
     * @return {@code true} if the currency exists, {@code false} otherwise.
     */
    public boolean isCurrencyExisted(String currency) {
        Currency c = currencyRepository.findByCode(currency);
        return c != null;
    }
}
