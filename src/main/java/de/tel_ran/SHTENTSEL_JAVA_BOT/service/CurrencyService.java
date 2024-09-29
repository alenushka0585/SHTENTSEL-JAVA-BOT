package de.tel_ran.SHTENTSEL_JAVA_BOT.service;

import de.tel_ran.SHTENTSEL_JAVA_BOT.dto.CurrencyDto;
import de.tel_ran.SHTENTSEL_JAVA_BOT.entity.Currency;
import de.tel_ran.SHTENTSEL_JAVA_BOT.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;


@Service
public class CurrencyService {
    private static final String BASE_URL = "https://www.floatrates.com/daily/";

    private CurrencyApi currencyApi;
    @Autowired
    CurrencyRepository currencyRepository;

    public CurrencyService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        currencyApi = retrofit.create(CurrencyApi.class);
    }

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

        }

        return "" + currencyDto;
    }

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

        }
        return rates;
    }

    public boolean isCurrencyExisted (String currency){
        Currency c = currencyRepository.findByCode(currency);
        return c != null;
    }



    //TODO добавить обработку ошибок и logger
}
