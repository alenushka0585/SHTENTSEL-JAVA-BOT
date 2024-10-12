package de.tel_ran.shtentsel_java_telegrambot.service;

import de.tel_ran.shtentsel_java_telegrambot.dto.CurrencyDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.Map;



public interface CurrencyApi {
    @GET("{baseCurrency}.json")
    Call<Map<String, CurrencyDto>> getCurrencies(
            @Path("baseCurrency") String baseCurrency);
}
