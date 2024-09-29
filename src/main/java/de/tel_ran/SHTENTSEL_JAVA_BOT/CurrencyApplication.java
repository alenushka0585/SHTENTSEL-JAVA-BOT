package de.tel_ran.SHTENTSEL_JAVA_BOT;

import de.tel_ran.SHTENTSEL_JAVA_BOT.dto.CurrencyDto;
import de.tel_ran.SHTENTSEL_JAVA_BOT.entity.Currency;
import de.tel_ran.SHTENTSEL_JAVA_BOT.repository.CurrencyRepository;
import de.tel_ran.SHTENTSEL_JAVA_BOT.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class CurrencyApplication implements CommandLineRunner{

    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    CurrencyService currencyService;

    @Autowired
    CurrencyDto currencyDto;

    public static void main(String[] args) {
        SpringApplication.run(CurrencyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Map<String, CurrencyDto> rates = currencyService.getCurrencyRates("usd");

        rates.forEach((currencyCode, currencyDto) -> {
            Currency currency = Currency.builder()
                    .code(currencyDto.getCode())
                    .name(currencyDto.getName()).build();
            currencyRepository.save(currency);
        });

 //для заполнения базы данных списком доступных валют
     }
}
