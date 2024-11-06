package de.tel_ran.shtentsel_java_telegrambot.network;

import de.tel_ran.shtentsel_java_telegrambot.dto.CurrencyDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.Map;


/**
 * Interface representing the API for fetching currency data.
 * Uses Retrofit to facilitate HTTP requests.
 */
public interface CurrencyApi {
    /**
     * Retrieves a map of currency data based on the given base currency.
     *
     * @param baseCurrency the base currency code (e.g., "USD", "EUR").
     * @return a {@link Call} object that contains a response map where the key is a string representing the currency code,
     * and the value is a {@link CurrencyDto} object containing currency details.
     */
    @GET("{baseCurrency}.json")
    Call<Map<String, CurrencyDto>> getCurrencies(
            @Path("baseCurrency") String baseCurrency);
}
