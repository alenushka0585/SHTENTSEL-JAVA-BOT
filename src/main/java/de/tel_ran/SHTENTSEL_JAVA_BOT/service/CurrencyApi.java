package de.tel_ran.SHTENTSEL_JAVA_BOT.service;

import de.tel_ran.SHTENTSEL_JAVA_BOT.dto.CurrencyDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.Map;



public interface CurrencyApi {
    @GET("{baseCurrency}.json")
    Call<Map<String, CurrencyDto>> getCurrencies(
            @Path("baseCurrency") String baseCurrency);
}
